package fr.sneakernet;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import fr.sneakernet.adapter.GestureListViewAdapter;
import fr.sneakernet.model.Sample;

public class GestureListViewActivity extends ListActivity
{
	private static final String TAG = "GestureListViewActivity";
	private List<Sample> mList;

	static final int NONE = 0;
	static final int ZOOM = 1;
	int mode = NONE;

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

		getListView().setOnTouchListener(new OnTouchListener()
		{
			private float aboveFingerPosition;
			private float belowFingerPosition;
			private float oldDist;
			private View extensible;
			private Integer position = null;
			private float previousAboveDiffSpan = 0;
			private float previousBelowDiffSpan = 0;

			@Override
			public boolean onTouch(View view, MotionEvent event)
			{
				float firstPointerY;
				float secondPointerY;
				
				switch (event.getAction() & MotionEvent.ACTION_MASK)
				{
					case MotionEvent.ACTION_POINTER_DOWN:
						firstPointerY = event.getY(0);
						secondPointerY = event.getY(1);
						
						if (firstPointerY < secondPointerY)
						{
							this.aboveFingerPosition = firstPointerY;
							this.belowFingerPosition = secondPointerY;
						}
						else
						{
							this.aboveFingerPosition = secondPointerY;
							this.belowFingerPosition = firstPointerY;
						}
						Log.d(TAG, "aboveFingerPosition=" + this.aboveFingerPosition);
						Log.d(TAG, "belowFingerPosition=" + this.belowFingerPosition);
						
						if (belowFingerPosition - aboveFingerPosition > 50)
							mode = ZOOM;
						
						if (mode == ZOOM)
						{
							
							for (int i = getListView().getFirstVisiblePosition(); i < getListView().getLastVisiblePosition(); i++)
					    	{
					    		// Get the items one by one
					    		View v = getListView().getChildAt(i-getListView().getFirstVisiblePosition());
	
					    		// Get the height of the item view
								float height = v.getHeight();
	
								// Get its y position on the screen
					    		int [] coordinates = new int[2];
					    		v.getLocationOnScreen(coordinates);
					    		
					    		float startY = coordinates[1];
					    		float focus = this.aboveFingerPosition + (this.belowFingerPosition - this.aboveFingerPosition) / 2;
					    		float endY = startY + height + 1;
	
					    		// If focus point is between this item and the previous one
					    		if (startY <= focus && focus <= endY)
								{
					    			// Create a new item and add it to the list below the current item view
									Sample c = new Sample("new", 100, true);
									this.position = new Integer(i+1);
									mList.add(i+1, c);
	
									// Notify the list adapter that dataset has changed
							    	((GestureListViewAdapter)getListAdapter()).notifyDataSetChanged();
								}
							}
						}
						return true;
	
					case MotionEvent.ACTION_MOVE:
						if (mode == ZOOM && this.position != null)
				    	{
				    		// Get the view that has just been inserted
					    	this.extensible = getListView().getChildAt(this.position.intValue()-getListView().getFirstVisiblePosition());
							
							firstPointerY = event.getY(0);
							secondPointerY = event.getY(1);
							
							float currentAboveFingerPosition;
							float currentBelowFingerPosition;
							
							if (firstPointerY < secondPointerY)
							{
								currentAboveFingerPosition = firstPointerY;
								currentBelowFingerPosition = secondPointerY;
							}
							else
							{
								currentAboveFingerPosition = secondPointerY;
								currentBelowFingerPosition = firstPointerY;
							}
							
							LayoutParams params = (LayoutParams) this.extensible.getLayoutParams();

							int scroll = 0;
							int height = params.height;
							
				    		int [] coordinates = new int[2];
				    		getListView().getLocationOnScreen(coordinates);
							
							if (aboveFingerPosition > currentAboveFingerPosition && currentAboveFingerPosition > coordinates[1])
							{
								float aboveDiffSpan = aboveFingerPosition - currentAboveFingerPosition;
								
								scroll = (int)(aboveDiffSpan - previousAboveDiffSpan);
								height += scroll;
					    		getListView().scrollBy(0, scroll);

					    		this.previousAboveDiffSpan = aboveDiffSpan;
							}

							if (currentBelowFingerPosition > belowFingerPosition)
							{
								float belowDiffSpan = currentBelowFingerPosition - belowFingerPosition;
								
								height += (int)(belowDiffSpan - previousBelowDiffSpan );
								
								this.previousBelowDiffSpan = belowDiffSpan;
							}
					    	
					    	if (height <= 0)
					    		height = 1;

					    	params.height = height;
					    	this.extensible.setLayoutParams(params);

							Log.d(TAG, "height =" + height);

							return true;
						}
						break;
					case MotionEvent.ACTION_POINTER_UP:
						if (mode == ZOOM && this.position != null)
						{
							mode = NONE;

							mList.remove(this.position.intValue());

					    	((GestureListViewAdapter)getListAdapter()).notifyDataSetChanged();

							return true;
						}
						break;
				}

				return view.onTouchEvent(event);
			}
		});
	}
}