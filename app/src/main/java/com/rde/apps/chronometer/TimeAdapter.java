package com.rde.apps.chronometer;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TimeAdapter extends ArrayAdapter<ChronoLap>{

    // View lookup cache
    private static class ViewHolder {
        TextView tvSpit;
        TextView tvLap;
    }
	
	
	
	
    public TimeAdapter(Context context, ArrayList<ChronoLap> laps) {
        super(context, R.layout.list_item , laps);
    }
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ChronoLap aLap = getItem(position); 
    	ViewHolder viewHolder; 
    	if(convertView == null) {
        	viewHolder = new ViewHolder();
    		LayoutInflater inflater = LayoutInflater.from(getContext());
    		convertView = inflater.inflate(R.layout.list_item, parent, false);
    		viewHolder.tvLap = (TextView) convertView.findViewById(R.id.tvLap);
    		viewHolder.tvSpit = (TextView) convertView.findViewById(R.id.tvSplit);
    		convertView.setTag(viewHolder);
    		
    	} else {
    		viewHolder = (ViewHolder) convertView.getTag();
    	}
    	viewHolder.tvSpit.setText("Split " + Integer.toString(this.getCount() - position));
    	viewHolder.tvLap.setText(aLap.elapsedTime + "    " + aLap.startTime  );
    	return convertView;
    }

    
    
}
