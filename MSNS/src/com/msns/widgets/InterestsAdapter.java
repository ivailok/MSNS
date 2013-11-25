package com.msns.widgets;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.msns.R;
import com.msns.models.Interest;

public class InterestsAdapter extends ArrayAdapter<Interest> {
	private LayoutInflater inflater;
	
	public InterestsAdapter(Context context, int textViewResourceId, ArrayList<Interest> objects) {
        super(context, textViewResourceId, objects);
        this.inflater = LayoutInflater.from(context);
    }
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		Interest interest = this.getItem(position);
       
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.interests_list_item, parent, false);
            TextView labelView = (TextView) convertView.findViewById(R.id.interestKey);
            labelView.setFocusable(false);
            labelView.setFocusableInTouchMode(false);
            labelView.setText(interest.Key);
        }
        
        return convertView ;
    }
	
	@Override
    public int getViewTypeCount() {
        return 1;
    }
   
    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
