package GJ.helpinghand;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class group extends Fragment{

	private ListView group_list;
	private TextView selected_group_name,selected_group_owner;
	private SharedPreferences data;
	private SwipeRefreshLayout refresh;
	private EditText gName,gMember;
	private Button privateB,publicB,create;
	private AlertDialog newG;
	private int privateG=1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.group_layout, null);
		group_list = (ListView) view.findViewById(R.id.groups);
		refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
		refresh.setColorSchemeResources(R.color.material_blue_grey_800,R.color.my_primary);
		
		data = getActivity().getSharedPreferences(getString(R.string.user_detail), 
				 											Context.MODE_PRIVATE);
		 
		 
		 new get_group_list().execute(new Server_Interaction());
		
		 	 
		 group_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				View selected = group_list.getChildAt(position-group_list.getFirstVisiblePosition());
				selected_group_name = (TextView) selected.findViewById(R.id.title);
				selected_group_owner = (TextView) selected.findViewById(R.id.date);
				
				Intent gDetail = new Intent(getActivity(),Group_Detail.class);
				gDetail.putExtra("group", selected_group_name.getText().toString());
				gDetail.putExtra("owner", selected_group_owner.getText().toString());
				gDetail.putExtra("user_id",data.getString(getString(R.string.shared_user_id),"") );
				startActivity(gDetail);
				
			}
		 });
		 
		 //FAB
		FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
		fab.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Create ProgressDialog here..............	
				
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				View view = getActivity().getLayoutInflater().inflate(R.layout.new_group_alert, null);
				alert.setView(view);
				
				gName = (EditText) view.findViewById(R.id.name);
				gMember = (EditText) view.findViewById(R.id.members);
				privateB = (Button) view.findViewById(R.id.privateButton);
				publicB = (Button) view.findViewById(R.id.publicButton);
				create = (Button) view.findViewById(R.id.create);
				
				privateB.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						privateB.setBackgroundResource(R.drawable.group_selected);
						publicB.setBackgroundResource(R.drawable.group_not_selected);
						privateG=1;
					}
				});
				
				publicB.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						publicB.setBackgroundResource(R.drawable.group_selected);
						privateB.setBackgroundResource(R.drawable.group_not_selected);
						privateG=0;
					}
				});
				
				create.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(!gName.getText().toString().equals("")){
							new create_group().execute(new Server_Interaction());
						}
						else
							Toast.makeText(getActivity(), "Enter Group Name", Toast.LENGTH_LONG).show();
					}
				});
				
				newG=alert.show();
			}
		});
		
		group_list.setOnScrollListener(new  OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				int topRowVerticalPosition = 
					      (group_list == null || group_list.getChildCount() == 0) ? 
					        0 : group_list.getChildAt(0).getTop();
				Log.d("value","" + topRowVerticalPosition);
				refresh.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
				
			}
		});
		
		refresh.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new get_group_list().execute(new Server_Interaction());
			}
		});
		 
		 return view;
	}

	class create_group extends AsyncTask<Server_Interaction, String ,String>{

		String nameParam[];
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			nameParam=gMember.getText().toString().split(",");
		}
		@Override
		protected String doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("u_id",
					data.getString(getString(R.string.shared_user_id),"")));
			arg.add(new BasicNameValuePair("group_name",gName.getText().toString()));
			arg.add(new BasicNameValuePair("private",""+privateG));
			arg.add(new BasicNameValuePair("no", ""+nameParam.length));
			for(int i=0;i<nameParam.length;i++)
				arg.add(new BasicNameValuePair("String"+i, nameParam[i]));
	
			params[0].create_group(arg);
		return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			newG.dismiss();
			refresh.setRefreshing(true);
			new get_group_list().execute(new Server_Interaction());
		}
		
	}
	
	class get_group_list extends AsyncTask<Server_Interaction, JSONArray, JSONArray>{

		ProgressDialog load;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			load = new ProgressDialog(getActivity());
			load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			load.setTitle("Loading");
			load.setMessage("Fetching Your Group Data...");
			if(!refresh.isRefreshing()){
				load.show();
			}
			
		}
	
		@Override
		protected JSONArray doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub

			ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("u_id", data.getString(getString(R.string.shared_user_id),"")));
			
			return  params[0].my_group(arg);
			
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			// TODO Auto-generated method stub
			load.dismiss();
			refresh.setRefreshing(false);
			Custom_content_list adap;
			if(result!=null){
				adap = new Custom_content_list(getActivity(),result,1);
				group_list.setAdapter(adap);
			}
			else{
			}
			super.onPostExecute(result);
		
		}
	}
}

