package fr.sneakernet;

import java.util.ArrayList;
import java.util.List;

import fr.sneakernet.adapter.GestureListViewAdapter;
import fr.sneakernet.model.Sample;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;

public class GestureListViewActivity extends ListActivity
{
	private static final String TAG = "GestureListViewActivity";
	private List<Sample> mList;
	private ScaleGestureDetector mScaleDetector;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Populate array of data
        mList = new ArrayList<Sample>();
        for (int i = 0; i<20; i++)
        {
        	Sample sample = new Sample("Sample "+i, i, false);
        	mList.add(sample);
        }
        
        // Set listview adapter
        setListAdapter(new GestureListViewAdapter(this, mList));
        
        // Instanciate a new ScaleGestureDetector
		mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
		
		// Bind onTouch listener to listview
		getListView().setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				// Pass the motion event to the scale gesture detector
				mScaleDetector.onTouchEvent(event);

				// If scale gesture is not in progress
		        if (!mScaleDetector.isInProgress())
		        {
		        	// return 'normal' onTouch actions (listview scrolling, fling, ...)
		        	return v.onTouchEvent(event);
		        }
		        else
		        {
		        	// Set dark selector to mark separation between new item and siblings
			        getListView().setSelector(android.R.drawable.screen_background_dark);
		        }
		        
		        return true;
			}
		});
    }

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
	{
		private View view;
		private float previousSpan = 0;
		private Integer position = null;
		private float previousDiffSpan = 0;

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector)
		{
	    	Log.d(TAG, "onScaleBegin");
	    	
	    	// Get the y of focus point between the two fingers
	    	float focusY = detector.getFocusY();
	    	
	    	// For each visible item in the listview
	    	for (int i = getListView().getFirstVisiblePosition(); i < getListView().getLastVisiblePosition(); i++)
	    	{
	    		// Get the items one by one
	    		View v = getListView().getChildAt(i-getListView().getFirstVisiblePosition());
	    		
	    		// Get the height of the item view
				float height = v.getHeight();
	    		
				// Get its y position on the screen
	    		int [] coordinates = new int[2];
	    		v.getLocationOnScreen(coordinates);
	    		float y = coordinates[1];
	    		
	    		// If focus point is between this item and the previous one
	    		// NOTE: Various positioning test might be done here
	    		// I kept this one after many attemps of detecting the position where
	    		// the new item must be inserted
	    		// Feel free to try other ways in your own branch
	    		if (y-(height/2) <= focusY && focusY <= y+(height/2))
				{
	    			// Create a new item and add it to the list below the current item view
					Sample c = new Sample("new", 100, true);
		    		this.position = new Integer(i+1);
					mList.add(i+1, c);
					
					// Notify the list adapter that dataset has changed
			    	((GestureListViewAdapter)getListAdapter()).notifyDataSetChanged();
				}
			}
	    	
	    	// Keep the distance between the two fingers at the begining of the scale gesture
	    	this.previousSpan = detector.getCurrentSpan();

	        return true;
		}

	    @Override
	    public boolean onScale(ScaleGestureDetector detector)
	    {
	    	if(this.position != null)
	    	{
	    		// Get the view that has just been inserted
		    	this.view = getListView().getChildAt(this.position.intValue()-getListView().getFirstVisiblePosition());
		    	
		    	// If the view is tagged as "extensible" we are sure of handling the good one
		    	if(this.view.getTag().equals("extensible"))
		    	{
		    		// Calculate the difference between the current distance between the two fingers,
		    		// and the one calculated at the begining of the scale gesture
			    	float diffSpan = detector.getCurrentSpan() - this.previousSpan;
			    	
			    	if(diffSpan > 0)
			    	{
			    		// If the user is pinch zooming
				    	if(diffSpan > this.previousDiffSpan)
				    	{
				    		// Scroll down the list of a quarter of the fingers move
				    		getListView().scrollTo(0, getListView().getScrollY()+(int)(diffSpan-this.previousDiffSpan)/4);
				    	}
			    		// If the user is pinch unzooming
				    	else
				    	{
				    		// Scroll up the list of a quarter of the fingers move
				    		getListView().scrollTo(0, getListView().getScrollY()+-(int)(diffSpan-this.previousDiffSpan)/4);
				    	}
				    	
				    	// Keep diff span for next run
				    	this.previousDiffSpan = diffSpan;
				    	
				    	// Increase or decrease the size of the new item by half the fingers move
				    	LayoutParams params = (LayoutParams) this.view.getLayoutParams();
				    	params.height = (int)diffSpan/2;
				    	this.view.setLayoutParams(params);
			    	}
		    	}
	    	}
	    	
	    	return true;
	    }
	    
	    @Override
	    public void onScaleEnd(ScaleGestureDetector detector)
	    {
	    	Log.d(TAG, "onScaleEnd");

	    	// Here I delete the item just to keep my list clear and better see what I'm doing
	    	// Feel free to change this behavior in your own branch
	    	if(this.position != null)
	    	{
	    		mList.remove(this.position.intValue());
	    		((GestureListViewAdapter)getListAdapter()).notifyDataSetChanged();
	    		this.position = null;
	    	}
	    	super.onScaleEnd(detector);
	    }
	}
}