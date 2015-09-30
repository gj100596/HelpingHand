package GJ.helpinghand;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Custom_rest_list extends ArrayAdapter<String>{
	
	Activity act;
	String val[];
	int img[];
	
	public Custom_rest_list(Activity context, int resource[], String objects[]) {
		super(context, R.layout.setting_laylist, objects);
		// TODO Auto-generated constructor stub
		this.act = context;
		this.img=resource;
		this.val=objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		LayoutInflater inflater = act.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.setting_laylist	, null, true);
		
		TextView Title = (TextView) rowView.findViewById(R.id.name);
		ImageView image = (ImageView) rowView.findViewById(R.id.icon);
		
		Title.setText(val[position]);
		image.setImageResource(img[position]);
		
		return rowView;
	}
	
	
	
}
