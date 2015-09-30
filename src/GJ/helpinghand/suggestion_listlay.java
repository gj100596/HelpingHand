package GJ.helpinghand;

import android.app.Activity;
import android.support.v7.appcompat.R.color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class suggestion_listlay extends ArrayAdapter<String>{
	
	String group[],owner[];
	Activity act;
	
	public suggestion_listlay(Activity context, String group[], String owner[]) {
		super(context, android.R.layout.simple_list_item_2,group);
		// TODO Auto-generated constructor stub
		this.act=context;
		this.group=group;
		this.owner=owner;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		LayoutInflater inflater = act.getLayoutInflater();
		View rowView= inflater.inflate(	android.R.layout.simple_list_item_2, null, true);
		
		TextView gTitle = (TextView) rowView.findViewById(android.R.id.text1);
		TextView gOwner = (TextView) rowView.findViewById(android.R.id.text2);
		gTitle.setTextColor(rowView.getResources().getColor(R.color.my_primary_text));
		gOwner.setTextColor(rowView.getResources().getColor(R.color.my_primary_text));
		
		gTitle.setText(group[position]);
		Log.d("d","adasf"+group[position]);
		gOwner.setText(owner[position]);
		
		return rowView;
	}
	
	
	
}