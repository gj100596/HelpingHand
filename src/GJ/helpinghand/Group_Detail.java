package GJ.helpinghand;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Group_Detail extends Activity{

	private String group_name,owner,user_id;
	private TextView gName,gOwner;
	private Button add,leave;
	private EditText name;
	private ListView memberList;
	private int myIndex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group__detail);
		
		Bundle param = getIntent().getExtras();
		group_name=param.getString("group");
		owner=param.getString("owner");
		user_id=param.getString("user_id");
		
		gName = (TextView) findViewById(R.id.gName);
		gOwner = (TextView) findViewById(R.id.gOwner);
		memberList = (ListView) findViewById(R.id.member_list);
		add = (Button) findViewById(R.id.add);
		leave = (Button) findViewById(R.id.leave);
		
		getActionBar().setTitle(group_name);
		gName.setText(group_name);
		gOwner.setText("Created by " + owner);
		
		new group_detail().execute(new Server_Interaction());
		
		memberList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position == myIndex){
					AlertDialog.Builder alert = new AlertDialog.Builder(Group_Detail.this);
					alert.setMessage("Want to Leave the Group?");
					alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_LONG).show();
						}
					});
					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					alert.show();
				}
			}
		});
		
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = new EditText(Group_Detail.this);
				name.setHint("Enter User-ID of Member Seprated By Comma");
				name.setGravity(Gravity.TOP);
				name.setMinLines(3);
				AlertDialog.Builder	mem = new AlertDialog.Builder(Group_Detail.this);
				mem.setTitle("Enter Member Id");
				mem.setView(name);
				mem.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new create_group().execute(new Server_Interaction());
					}
				});
				mem.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				mem.show();
			}
		});
		
		leave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder	mem = new AlertDialog.Builder(Group_Detail.this);
				if(owner.equalsIgnoreCase(user_id)){
					mem.setTitle("Do you want to Leave The Group?");
					mem.setMessage("Note: You are the group owner, By leaving you are deleting " +
							"the whole group");
				}
				else
					mem.setTitle("Do you want to Leave The Group?");
				mem.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new remove_group().execute(new Server_Interaction());
					}
				});
				mem.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				mem.show();
			}
		});
		
		memberList.setOnScrollListener(new  OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				int topRowVerticalPosition = 
					      (memberList == null || memberList.getChildCount() == 0) ? 
					        0 : memberList.getChildAt(0).getTop();
				if(!(firstVisibleItem == 0 && topRowVerticalPosition >= 0)){
					add.setVisibility(4);
					leave.setVisibility(4);
				}
				else{
					add.setVisibility(0);
					leave.setVisibility(0);
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group__detail, menu);
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
	
	class create_group extends AsyncTask<Server_Interaction, String ,String>{

		String nameParam[];
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			nameParam=name.getText().toString().split(",");
		}
		@Override
		protected String doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("group_name",group_name));
			arg.add(new BasicNameValuePair("owner",owner));
			arg.add(new BasicNameValuePair("no", ""+nameParam.length));
			for(int i=0;i<nameParam.length;i++)
				arg.add(new BasicNameValuePair("String"+i, nameParam[i]));
	
			params[0].update_group(arg);
		return null;
		}
		
	}
	
	class remove_group extends AsyncTask<Server_Interaction, String ,String>{

		
		@Override
		protected String doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("u_id",user_id));
			arg.add(new BasicNameValuePair("group_name",group_name));
			arg.add(new BasicNameValuePair("owner",owner));
		
			params[0].update_group(arg);
		return null;
		}
		
	}

	
	class group_detail extends AsyncTask<Server_Interaction, JSONArray, JSONArray>{

		
		@Override
		protected JSONArray doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub

			ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("group",group_name));
			arg.add(new BasicNameValuePair("owner",owner));
			
			return  params[0].group_detail(arg);
			
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			// TODO Auto-generated method stub			
			if(result!=null){
				try {
					JSONObject answer;
					String member[] = new String[result.length()];
					int i,j;
					for(i=0,j=0;i<result.length();i++){
						answer = result.getJSONObject(i);
						if(!answer.getString("name").equalsIgnoreCase(user_id))
							member[j++]=answer.getString("name");
					}
					member[j]="YOU";
					myIndex=j;
					ArrayAdapter<String> adap = new ArrayAdapter<>(getApplicationContext(),
							android.R.layout.simple_list_item_1,android.R.id.text1,member);
					memberList.setAdapter(adap);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
			}
		
		}
	}
}
