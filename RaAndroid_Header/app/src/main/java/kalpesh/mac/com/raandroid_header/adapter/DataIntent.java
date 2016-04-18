package kalpesh.mac.com.raandroid_header.adapter;

/**
 * Created by TAE on 18/04/2016.
 */

//Incomplete class due to not realizing it was part of the requirements.
    /*
public class DataIntent extends IntentService {

    private double[] lat, lon;
    private String[] title, address, postcode;
    protected List<Address> addresses;
    private Geocoder geocoder;

    public DataIntent(String name) {
        super(name);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            title = intent.getStringArrayExtra("title");
            address = intent.getStringArrayExtra("address");
            postcode = intent.getStringArrayExtra("postcode");

            int i = 0;
            while (i != title.length) {
                address[i] = address + " " + postcode;
                addresses = geocoder.getFromLocationName(address[i], title.length);

                if (addresses.size() > 0) {
                    lat[i] = addresses.get(0).getLatitude();
                    lon[i] = addresses.get(0).getLongitude();
                }
                i++;
            }
        } catch (IOException e) {

        }
    }
}
*/