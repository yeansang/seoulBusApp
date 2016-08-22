package com.example.nemus.seoulbusapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nemus on 2016-08-19.
 */
public class CustomAdapter extends ArrayAdapter<JSONObject> {

    private ArrayList<JSONObject> item;

    public CustomAdapter(Context context, int resource, ArrayList<JSONObject> objects) {
        super(context, resource, objects);
        this.item = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View out = convertView;
        if (out == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            out = vi.inflate(R.layout.list_row, null);
        }
        JSONObject j = item.get(position);
        if (j != null) {
            TextView top = (TextView) out.findViewById(R.id.toptext);
            TextView bott = (TextView) out.findViewById(R.id.bottomtext);
            try {
                if (top != null) {
                    top.setText(j.getString("rtNm"));
                }
                if (bott != null) {
                    bott.setText(j.getString("arrmsg1"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return out;
    }

}
