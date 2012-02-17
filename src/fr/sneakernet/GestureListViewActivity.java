package fr.sneakernet;

import java.util.ArrayList;
import java.util.List;

import fr.sneakernet.adapter.GestureListViewAdapter;
import fr.sneakernet.model.Sample;
import android.app.ListActivity;
import android.os.Bundle;

public class GestureListViewActivity extends ListActivity
{
	private List<Sample> mList;

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
    }
}