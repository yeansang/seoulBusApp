package com.example.nemus.seoulbusapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nemus on 2016-08-22.
 */
public class PathListAdapter extends ArrayAdapter<PathInfoData> {

    private ArrayList<PathInfoData> items;

    public PathListAdapter(Context context, int resource, ArrayList<PathInfoData> objects) {
        super(context, resource, objects);
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View out = convertView;
        if (out == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            out = vi.inflate(R.layout.list_row, null);
        }

        PathInfoData temp = items.get(position);

        TextView top = (TextView) out.findViewById(R.id.toptext);
        TextView bott = (TextView) out.findViewById(R.id.bottomtext);

        String topText = "";
        String bottText = "";
        topText += temp.paths.get(0).busNum;
        for(int i=1;i<temp.paths.size();i++){
            topText += " -> "+temp.paths.get(i).busNum;
        }

        bottText += (temp.paths.size()-1)+"번 환승 , "+temp.distance+" 미터";

        top.setText(topText);
        bott.setText(bottText);

        return out;
    }
}
