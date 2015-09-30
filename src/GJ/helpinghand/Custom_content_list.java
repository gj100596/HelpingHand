package GJ.helpinghand;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class Custom_content_list extends ArrayAdapter<String> {

	private final Activity context;
	private final JSONArray main;
	private int mode;		//0 for my file & 1 for data file 2 for Share file & 3 for share folder 
	
	public Custom_content_list(Activity context,JSONArray jarry,int m){
		
		super(context, R.layout.listlay, new String[jarry.length()]);
		Log.d("length",""+jarry.length());
		this.context = context;
		mode=m;
		this.main = jarry;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.listlay	, null, true);
		
		TextView Title = (TextView) rowView.findViewById(R.id.title);
		TextView mdate = (TextView) rowView.findViewById(R.id.date);
		TextView type = (TextView) rowView.findViewById(R.id.file_type);
		ImageView image = (ImageView) rowView.findViewById(R.id.img);
		
		JSONObject values;
		try {
			values = main.getJSONObject(position);
			String title=values.getString("fname");
			String[] part = title.split("/");
			Title.setText(part[part.length - 1]);
			if(mode==0){
				mdate.setText(values.getString("mdate"));
			}
			else 
				mdate.setText(values.getString("owner"));
			type.setText(values.getString("type"));
			switch(values.getString("type").toLowerCase()){
			case "pdf":
				image.setImageResource(R.drawable.pdf);
				break;
			case "doc":
				image.setImageResource(R.drawable.doc);
				break;
			case "jpg":
			case "png":
			case "gif":
				image.setImageResource(R.drawable.jpg);
				break;
			case "txt":
				image.setImageResource(R.drawable.txt);
				break;
			case "folder":
				image.setImageResource(R.drawable.folder);
				break;
			case "sharedfolder":
				image.setImageResource(R.drawable.shared);
				break;
			case "group":
				image.setImageResource(R.drawable.group);
				break;
			default:
				image.setImageResource(R.drawable.file);
				break;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowView;
	}
}

