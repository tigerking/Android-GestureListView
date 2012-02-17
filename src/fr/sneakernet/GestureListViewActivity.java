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
        
        mList = new ArrayList<Sample>();
        
        for (int i = 0; i<20; i++)
        {
        	Sample sample = new Sample("Sample "+i, i, false);
        	mList.add(sample);
        }
        
        setListAdapter(new GestureListViewAdapter(this, mList));
        
		mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
		
		getListView().setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				mScaleDetector.onTouchEvent(event);

		        if (!mScaleDetector.isInProgress()) {
		        	return v.onTouchEvent(event);
		        }
		        else
		        {
			        getListView().setSelector(android.R.drawable.screen_background_dark);
		        }
		        
		        return true;
			}
		});
    }

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
	{
		private View view;
		private float previousScan = 0;
		private Integer position = null;
		private float previousDiffSpan = 0;

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector)
		{
	    	Log.d(TAG, "onScaleBegin");
	    	
	    	float focusY = detector.getFocusY();
	    	
	    	for (int i = getListView().getFirstVisiblePosition(); i < getListView().getLastVisiblePosition(); i++)
	    	{
	    		View v = getListView().getChildAt(i-getListView().getFirstVisiblePosition());
				float height = v.getHeight();
	    		
	    		int [] coordinates = new int[2];

	    		v.getLocationOnScreen(coordinates);
	    		float y = coordinates[1];
	    		
	    		if (y-(height/2) <= focusY && focusY <= y+(height/2))
				{
					Sample c = new Sample("new", 100, true);
		    		this.position = new Integer(i+1);
					mList.add(i+1, c);
			    	((GestureListViewAdapter)getListAdapter()).notifyDataSetChanged();
				}
			}
	    	this.previousScan = detector.getCurrentSpan();

	        return true;
		}

	    @Override
	    public boolean onScale(ScaleGestureDetector detector)
	    {
	    	if(this.position != null)
	    	{
		    	this.view = getListView().getChildAt(this.position.intValue()-getListView().getFirstVisiblePosition());
		    	if(this.view.getTag().equals("extensible"))
		    	{
			    	float diffSpan = detector.getCurrentSpan() - this.previousScan;
			    	if(diffSpan > 0)
			    	{
				    	if(diffSpan > this.previousDiffSpan)
				    	{
				    		getListView().scrollTo(0, getListView().getScrollY()+(int)(diffSpan-this.previousDiffSpan)/4);
				    	}
				    	else
				    	{
				    		getListView().scrollTo(0, getListView().getScrollY()+-(int)(diffSpan-this.previousDiffSpan)/4);
				    	}
				    	this.previousDiffSpan = diffSpan;
				    	
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