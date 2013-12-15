package com.msns.navdrawer;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


public class OnItemClickListener implements OnClickListener {

	private int mPosition;
    OnItemClickListener(int position){
        mPosition = position;
    }
    
    @Override
    public void onClick(View arg0) {
        Log.d("MSNS", "onItemClick at position" + mPosition);          
    }      

}
