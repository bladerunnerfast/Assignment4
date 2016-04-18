package kalpesh.mac.com.raandroid_header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kalpesh.mac.com.raandroid_header.GoogleMaps.MapsActivity;
import kalpesh.mac.com.raandroid_header.adapter.RecyclerAdapter;
import kalpesh.mac.com.raandroid_header.model.Example;
import kalpesh.mac.com.raandroid_header.model.Restaurant;
import kalpesh.mac.com.raandroid_header.observables.IRestaurant;
import kalpesh.mac.com.raandroid_header.services.Services;
import kalpesh.mac.com.raandroid_header.utilities.RxUtils;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    //Composite Subscription
    private IRestaurant _api;
    private List<Restaurant> mRestaurantList;
    private ProgressDialog pDialog;
    private static int size = 0;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    protected RecyclerAdapter adapter;
    /**
     * Subscription that represents a group of Subscriptions that are unsubscribed together.
     */
    private CompositeSubscription _subscriptions = new CompositeSubscription();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = (RecyclerView)findViewById(R.id.myList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        _api = Services._createRestruentshubApi();
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        pattern();

    }
    @Override
    public void onResume() {
        super.onResume();
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(_subscriptions);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    public void pattern(){

        _subscriptions.add(_api.getRestraurent()
//http://docs.couchbase.com/developer/java-2.0/observables.html
                .timeout(5000, TimeUnit.MILLISECONDS)
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Example>>() {
                    @Override
                    public Observable<? extends Example> call(Throwable throwable) {
                        Toast.makeText(getBaseContext(), "Error ", Toast.LENGTH_SHORT).show();
                        Log.i("ERROR RX","NO MSG" );
                        return Observable.error(throwable);
                    }
                })

                          .retry()
                         .distinct()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Example>() {
                    @Override
                    public void onCompleted() {
                        hidePDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hidePDialog();
                    }

                    @Override
                    public void onNext(Example example) {
                        mRestaurantList = example.getRestaurants();
                        System.out.println("Got: " + " (" + Thread.currentThread().getName() + ")");
                        //Adapter adapt = new Adapter(getApplicationContext(), R.layout.row, mRestaurantList);
                        //  Log.i("REST", "" );
                       //listActivity.setListAdapter(adapt);
                        adapter = new RecyclerAdapter(getApplicationContext(), R.layout.row, example);
                        recyclerView.setAdapter(adapter);
                    }
                }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            createLocations();
            return true;
        }
        else if(id == R.id.action_search){
            createLocations();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createLocations(){
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        size = mRestaurantList.size();
        double[] lat, lon;
        String[] shops;
        lat = new double[size];
        lon = new double[size];
        shops = new String[size];
        String[] addr = new String[size];

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Downloading Restaurants, please wait");
        pDialog.show();
        try {
            System.out.println("createLocations data size: "+size);

            int i = 0;
            while(i != size){
                //System.out.println("Restaurant address "+mRestaurantList.get(i).getAddress()+" index "+i);
               // System.out.println("Restaurant postcode "+mRestaurantList.get(i).getPostcode()+" index "+i);
                 addr[i] =  mRestaurantList.get(i).getPostcode();
                addresses = geocoder.getFromLocationName(addr[i], size);

                if(addresses.size() > 0) {
                    lat[i] = addresses.get(0).getLatitude();
                    lon[i] = addresses.get(0).getLongitude();
                    shops[i] = mRestaurantList.get(i).getName();
                    System.out.println("createLocations latitude: " + lat[i]);
                }
                i++;
             }

            hidePDialog();
             Intent intent = new Intent(this, MapsActivity.class);
             intent.putExtra("longitude", lon);
             intent.putExtra("latitude", lat);
             intent.putExtra("title", shops);
             intent.putExtra("size", size);
             startActivity(intent);

        }catch(IOException e){
            Log.wtf("createLocations error: ", e.getCause());
        }
    }
}
