package kalpesh.mac.com.raandroid_header.adapter;

        import android.app.Activity;
        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.squareup.picasso.Picasso;

        import kalpesh.mac.com.raandroid_header.R;
        import kalpesh.mac.com.raandroid_header.model.Example;

/**
 * Created by TAE on 18/04/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    protected Example example;
    private Context context;
    private int row;

    public RecyclerAdapter(Context cont, int Row, Example example){
        this.context = cont;
        this.row = Row;
        this.example = example;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(example.getRestaurants().get(position).getName());
        Picasso.with(context)
                .load(example.getRestaurants().get(position).getLogo().get(0).getStandardResolutionURL())
                .resize(100, 100)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return example == null? 0:  example.getRestaurants().size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView name;
        private ImageView image;
        public ViewHolder(View v) {
            super(v);
            name = (TextView)itemView.findViewById(R.id.title);
            image = (ImageView)itemView.findViewById(R.id.thumbnail);
            itemView.setTag(itemView);

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
