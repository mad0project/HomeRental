package com.example.shahboz.homerental.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.shahboz.homerental.MyApp;
import com.example.shahboz.homerental.R;
import com.example.shahboz.homerental.data.Apartment;
import com.parse.ParseFile;
import com.parse.ParseImageView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;



/**
 * Created by shahboz on 21/06/2015.
 */
public class ListItemAdapter extends ParseQueryAdapter<Apartment> {
    public ListItemAdapter(Context context) {
        // Use the QueryFactory to construct a PQA that will only show
        // Todos marked as high-pri
        super(context, new ParseQueryAdapter.QueryFactory<Apartment>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Apartment");
                query.whereEqualTo(MyApp.APARTMENTS, true);
                return query;
            }
        });
    }

    @Override
    public View getItemView(Apartment apartment, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_item, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ParseImageView) convertView.findViewById(R.id.ivIcon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }



        ParseFile photoFile = apartment.getPhotos().get(0);
        if(photoFile != null){
            viewHolder.ivIcon.setParseFile(photoFile);
            viewHolder.ivIcon.loadInBackground();
        }

        viewHolder.tvTitle.setText(apartment.getCity() + ", " + apartment.getStreetName() + ", " + apartment.getHomeNumber());
        viewHolder.tvDescription.setText(apartment.getDescription());


        return convertView;
    }

    /**
     * The view holder design pattern prevents using findViewById()
     * repeatedly in the getView() method of the adapter.
     *
     * @see "http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder"
     */
    private static class ViewHolder {
        ParseImageView ivIcon;
        TextView tvTitle;
        TextView tvDescription;
    }
}
