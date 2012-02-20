package fr.sneakernet.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import fr.sneakernet.R;
import fr.sneakernet.model.Sample;

public class GestureListViewAdapter extends BaseAdapter
{
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Sample> mList;

	public GestureListViewAdapter(Context context, List<Sample> list)
	{
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mList = list;
	}

	@Override
	public int getCount()
	{
		return mList.size();
	}

	@Override
	public Sample getItem(int position)
	{
		return mList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Sample item = getItem(position);
		
		if(item.isFlagged())
		{
			convertView = mInflater.inflate(R.layout.extensible_list_item, null);
		}
		else
		{
			ViewHolder holder;
	
			if (convertView == null || convertView.getTag().equals("extensible"))
			{
				convertView = mInflater.inflate(R.layout.list_sample_item, null);
	
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.list_item_name);
				holder.count = (TextView) convertView.findViewById(R.id.list_item_count);
				
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
	
			holder.name.setText(item.getName());
			holder.count.setText(""+item.getCount());
		}
			
		return convertView;
	}

	static class ViewHolder
	{
		TextView name;
		TextView count;
	}
}
