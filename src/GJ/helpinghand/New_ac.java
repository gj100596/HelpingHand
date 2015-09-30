package GJ.helpinghand;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class New_ac extends Activity {
	
	private EditText mail,uid,pass,pass_again;
	private CheckBox terms;//,news;
	private Button done, back;
	private ProgressDialog load;
	private SharedPreferences data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_ac);
		getActionBar().hide();
		
		mail = (EditText) findViewById(R.id.mail_id);
		uid=(EditText) findViewById(R.id.user_id);
		pass=(EditText) findViewById(R.id.password);
		pass_again= (EditText) findViewById(R.id.password_again);
		done = (Button) findViewById(R.id.go);
		back = (Button) findViewById(R.id.already);
		terms = (CheckBox) findViewById(R.id.agree);
		//news = (CheckBox) findViewById(R.id.news);
		data = this.getSharedPreferences(getString(R.string.user_detail), Context.MODE_PRIVATE);
		
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent bk = new Intent(New_ac.this, Login.class);
				startActivity(bk);
				finish();
			}
		});
		
		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(check_null())
					new make_account().execute(new Server_Interaction());
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_ac, menu);
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
	private boolean check_null(){
		
		if(mail.getText().toString().equals("")){
			mail.setBackgroundResource(R.drawable.outline_wrong);
			Toast.makeText(getApplicationContext(), "Enter Mail ID", Toast.LENGTH_LONG).show();
		}
		else if(uid.getText().toString().equals("")){
			uid.setBackgroundResource(R.drawable.outline_wrong);
			Toast.makeText(getApplicationContext(), "Enter User ID", Toast.LENGTH_LONG).show();
		}
		else if(pass.getText().toString().equals("")){
			pass.setBackgroundResource(R.drawable.outline_wrong);
			Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();
		}
		else if(pass_again.getText().toString().equals("")){
			pass_again.setBackgroundResource(R.drawable.outline_wrong);
			Toast.makeText(getApplicationContext(), "Enter Password Again", Toast.LENGTH_LONG).show();
		}
		else if(!(pass.getText().toString().equals(pass_again.getText().toString()))){
				Toast.makeText(getApplicationContext(),
						"Password Doesn't Match", Toast.LENGTH_LONG).show();					
			}
		else if(!terms.isChecked()){
				Toast.makeText(getApplicationContext(),
						"Please Accept To Terms", Toast.LENGTH_LONG).show();
		}
		else
			return true;
		return false;
		
	}
	class make_account extends AsyncTask<Server_Interaction, String, String>{
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			load = new ProgressDialog(New_ac.this);
			load.setTitle("Creating Account");
			load.setCancelable(false);
			load.setMessage("Taking You to Your Profile Page...");
			load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			load.show();
		}

		@Override
		protected String doInBackground(Server_Interaction... obj) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("u_id",uid.getText().toString()));
			params.add(new BasicNameValuePair("mail",mail.getText().toString()));
			params.add(new BasicNameValuePair("pass",pass.getText().toString()));
			
			/*InstanceID instanceID = InstanceID.getInstance(New_ac.this);
			String token=null;
			try {
				token = instanceID.getToken("57665058023",
				        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Log.d("Token",token);
			params.add(new BasicNameValuePair("token",token));*/
			
			/*Parse.initialize(New_ac.this, "nXkbngasn0gI5cfeSmN8Fdztpsdu7cBAPKxSePgw", "622wKXKsYIzPQNujisqCMSX5uLtdAlSvIHJcJJsB");
			ParseInstallation.getCurrentInstallation().saveInBackground();
			  */
			return  obj[0].new_user(params);
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(result.equals("True")){
				load.dismiss();
				
				//saving user id.....
				SharedPreferences.Editor editor = data.edit();
				editor.putString(getString(R.string.shared_user_id), uid.getText().toString());
				editor.commit();
				
				//going to profile page
				Intent mnsr = new Intent(New_ac.this, Profile_setting.class);
				startActivity(mnsr);
				finish();
			}
			else if(result.equals("User Exists")){
				load.dismiss();
				Toast.makeText(getApplicationContext(),
						"User Exists...Choose other User ID", Toast.LENGTH_LONG).show();
			}
			else{
				load.dismiss();
				Toast.makeText(getApplicationContext(),
						"Some Errored Occured....Try Latter", Toast.LENGTH_LONG).show();
			}
		}
		
	}
}
