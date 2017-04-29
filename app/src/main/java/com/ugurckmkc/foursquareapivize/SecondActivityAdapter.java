package com.ugurckmkc.foursquareapivize;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SecondActivityAdapter extends RecyclerView.Adapter<SecondActivityAdapter.ViewHolder> {
    private Context context;
    private List<FoursquareResults> results;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView address;
        TextView distance;
        String id;
        double latitude;
        double longitude;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            name = (TextView)itemView.findViewById(R.id.placePickerItemName);
            address = (TextView)itemView.findViewById(R.id.placePickerItemAddress);
            distance = (TextView)itemView.findViewById(R.id.placePickerItemDistance);

        }

        @Override
        public void onClick(View view) {

            Context context = name.getContext();
            Intent i = new Intent(context, MapsActivty.class);
            i.putExtra("name", name.getText());
            i.putExtra("ID", id);
            i.putExtra("latitude", latitude);
            i.putExtra("longitude", longitude);

            context.startActivity(i);
        }
    }
    public SecondActivityAdapter(Context context, List<FoursquareResults> results) {
        this.context = context;
        this.results = results;
    }
    @Override
    public SecondActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SecondActivityAdapter.ViewHolder holder, int position) {
        holder.name.setText(results.get(position).venue.name);
        holder.address.setText(results.get(position).venue.location.address);
        holder.distance.setText(Integer.toString(results.get(position).venue.location.distance) + " Metre");

        holder.id = results.get(position).venue.id;
        holder.latitude = results.get(position).venue.location.lat;
        holder.longitude = results.get(position).venue.location.lng;
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}