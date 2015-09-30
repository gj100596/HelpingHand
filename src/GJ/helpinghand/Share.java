package GJ.helpinghand;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Share extends Activity {
	
	private EditText name;
	private ListView suggestion;
	private TextView selected_gName,selected_gOwner,ListTitle;
	private SharedPreferences data,directory;
	private Button shareButton;
	private String[] nameParam;
	private int mode; //0 for file sharing and 1 for folder sharing
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		Bundle arg = getIntent().getExtras();
		mode = arg.getInt("mode");
		
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		name = (EditText) findViewById(R.id.useridShared);
		suggestion = (ListView) findViewById(R.id.share_suggestion);
		ListTitle = (TextView) findViewById(R.id.heading);
		shareButton = (Button) findViewById(R.id.share);
		data = this.getSharedPreferences(getString(R.string.user_detail), 
					Context.MODE_PRIVATE);
		directory = this.getSharedPreferences(getString(R.string.directory), 
				Context.MODE_PRIVATE);
		
		ListTitle.setText("My Group");
		new my_group_list().execute(new Server_Interaction());
		
		name.setHintTextColor(getResources().getColor(R.color.my_primary_text));
		
		name.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(name.getText().toString().equals("")){
					ListTitle.setText("My Group");
					new my_group_list().execute(new Server_Interaction());
				}
				else{
					ListTitle.setText("Public Group");
					new suggestion().execute(new Server_Interaction());
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		suggestion.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				View selected = suggestion.getChildAt(position-suggestion.getFirstVisiblePosition());
				selected_gName = (TextView) selected.findViewById(android.R.id.text1);
				selected_gOwner = (TextView) selected.findViewById(android.R.id.text2);
				if(mode==0 || mode==1)
					new share_file_thru_group().execute(new Server_Interaction());
				else
					new share_shared_file_thru_group().execute(new Server_Interaction());
			}
		});
		
		shareButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String nameList = name.getText().toString();
				if(!nameList.equals("")){
					nameParam = nameList.split(",");
					if(mode==0 || mode==1)
						new share_file_thru_member().execute(new Server_Interaction());
					else
						new share_shared_file_thru_member().execute(new Server_Interaction());
				}
				else{
					Toast.makeText(getApplicationContext(), "Enter User-ID or Select a Group",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class share_file_thru_group extends AsyncTask<Server_Interaction, String,String>{

		@Override
		protected String doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("u_id",
					data.getString(getString(R.string.shared_user_id), "")));
			arg.add(new BasicNameValuePair("group_name",selected_gName.getText().toString()));
			arg.add(new BasicNameValuePair("group_owner",selected_gOwner.getText().toString()));
			
			if(mode==0){
				arg.add(new BasicNameValuePair("path", 
					directory.getString(getString(R.string.dir_path), "main")
					+"/"+My_Fragment_Activity.selected_file_name.getText().toString()));
				arg.add(new BasicNameValuePair("type",
					My_Fragment_Activity.selected_file_type.getText().toString()));
				return params[0].sharing_file(arg,0);
			}
			arg.add(new BasicNameValuePair("path", 
					directory.getString(getString(R.string.dir_path), "main")
					+"/"+My_Folder_Fragment.selected_folder.getText().toString()));
			return params[0].sharing_folder(arg,0);
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals("Done"))
				Toast.makeText(getApplicationContext(), "File Shared", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), "Error Occured..Try Again Latter!", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	class share_shared_file_thru_group extends AsyncTask<Server_Interaction, String,String>{

		@Override
		protected String doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("group_name",selected_gName.getText().toString()));
			arg.add(new BasicNameValuePair("group_owner",selected_gOwner.getText().toString()));
			
			if(mode==2){
				arg.add(new BasicNameValuePair("u_id",
						Share_File_Fragment.selected_file_owner.getText().toString()));
				arg.add(new BasicNameValuePair("path", 
					directory.getString(getString(R.string.dir_shared_path), "main")
					+"/"+Share_File_Fragment.selected_file_name.getText().toString()));
				arg.add(new BasicNameValuePair("type",
					Share_File_Fragment.selected_file_type.getText().toString()));
				return params[0].sharing_file(arg,0);
			}
			arg.add(new BasicNameValuePair("u_id",
					Share_Folder_Fragment.selected_folder_owner.getText().toString()));
			arg.add(new BasicNameValuePair("path", 
					directory.getString(getString(R.string.dir_shared_path), "main")
					+"/"+Share_Folder_Fragment.selected_folder.getText().toString()));
			return params[0].sharing_folder(arg,0);
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals("Done"))
				Toast.makeText(getApplicationContext(), "File Shared", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), "Error Occured..Try Again Latter!", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	class share_file_thru_member extends AsyncTask<Server_Interaction, String,String>{

		@Override
		protected String doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("u_id",
					data.getString(getString(R.string.shared_user_id),"")));
			arg.add(new BasicNameValuePair("no", ""+nameParam.length));
			for(int i=0;i<nameParam.length;i++)
				arg.add(new BasicNameValuePair("String"+i, nameParam[i]));
			
			if(mode==0){
				arg.add(new BasicNameValuePair("path", 
						directory.getString(getString(R.string.dir_path), "main")
						+"/"+My_Fragment_Activity.selected_file_name.getText().toString()));
				arg.add(new BasicNameValuePair("type",
						My_Fragment_Activity.selected_file_type.getText().toString()));
				return params[0].sharing_file(arg,1);
			}
			arg.add(new BasicNameValuePair("path", 
					directory.getString(getString(R.string.dir_path), "main")
					+"/"+My_Folder_Fragment.selected_folder.getText().toString()));
			return params[0].sharing_folder(arg,1);
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals("Done"))
				Toast.makeText(getApplicationContext(), "File Shared", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), "Error Occured..Try Again Latter!", Toast.LENGTH_SHORT).show();
		}
		
	}

	class share_shared_file_thru_member extends AsyncTask<Server_Interaction, String,String>{

		@Override
		protected String doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("no", ""+nameParam.length));
			for(int i=0;i<nameParam.length;i++)
				arg.add(new BasicNameValuePair("String"+i, nameParam[i]));
			
			if(mode==2){
				arg.add(new BasicNameValuePair("u_id",
						Share_File_Fragment.selected_file_owner.getText().toString()));
				arg.add(new BasicNameValuePair("path", 
						directory.getString(getString(R.string.dir_shared_path), "main")
						+"/"+Share_File_Fragment.selected_file_name.getText().toString()));
				arg.add(new BasicNameValuePair("type",
						Share_File_Fragment.selected_file_type.getText().toString()));
				return params[0].sharing_file(arg,1);
			}
			arg.add(new BasicNameValuePair("u_id",
					Share_Folder_Fragment.selected_folder_owner.getText().toString()));
			arg.add(new BasicNameValuePair("path", 
					directory.getString(getString(R.string.dir_path), "main")
					+"/"+Share_Folder_Fragment.selected_folder.getText().toString()));
			return params[0].sharing_folder(arg,1);
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals("Done"))
				Toast.makeText(getApplicationContext(), "File Shared", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), "Error Occured..Try Again Latter!", Toast.LENGTH_SHORT).show();
		}
		
	}

	
	class suggestion extends AsyncTask<Server_Interaction, JSONArray, JSONArray>{

		@Override
		protected JSONArray doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("groupID",name.getText().toString()));
			return params[0].suggestion(arg);
		}
		
		@Override
		protected void onPostExecute(JSONArray result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result!=null){
			String groups[] = new String[result.length()];
			String owners[] = new String[result.length()];
			for(int i=0;i<result.length();i++){
				JSONObject jobj;
				try {
					jobj = result.getJSONObject(i);
					groups[i]=jobj.getString("group");
					owners[i]=jobj.getString("owner");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			suggestion_listlay adap = new suggestion_listlay(Share.this, groups, owners);
			suggestion.setAdapter(adap);
			}
			else{
				suggestion_listlay adap = new suggestion_listlay(Share.this,new String[0], new String[0]);
				suggestion.setAdapter(adap);
			}
		}
	}
	
	class my_group_list extends AsyncTask<Server_Interaction, JSONArray, JSONArray>{

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
			super.onPostExecute(result);
			if(result!=null){
			String groups[] = new String[result.length()];
			String owners[] = new String[result.length()];
			for(int i=0;i<result.length();i++){
				JSONObject jobj;
				try {
					jobj = result.getJSONObject(i);
					groups[i]=jobj.getString("fname");
					owners[i]=jobj.getString("owner");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			suggestion_listlay adap = new suggestion_listlay(Share.this, groups, owners);
			suggestion.setAdapter(adap);
			}
		}
	}
}
