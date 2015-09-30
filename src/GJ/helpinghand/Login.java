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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	
	private Button go, forget, newac;
	private EditText user, pass;
	private SharedPreferences data,username;
	public CheckBox chk;
	Server_Interaction ser_obj = new Server_Interaction();
	private ProgressDialog load;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getActionBar().hide();
				
		go = (Button)findViewById(R.id.button3);
		forget = (Button)findViewById(R.id.button2);
		newac = (Button)findViewById(R.id.button1);
		user = (EditText) findViewById(R.id.autoCompleteTextView1);
		pass = (EditText) findViewById(R.id.editText1);
		chk = (CheckBox) findViewById(R.id.checkBox1);
		
		//check whether remember me was clicked?
		data = this.getSharedPreferences(getString(R.string.filename),
				Context.MODE_PRIVATE);
		//set the path stuff!!!!!
		SharedPreferences directory = this.getSharedPreferences(getString(R.string.directory), 
				Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = directory.edit();
		ed.putString(getString(R.string.dir_fol), "main");
		ed.putString(getString(R.string.dir_path), "main");
		ed.putString(getString(R.string.dir_shared_fol), "main");
		ed.putString(getString(R.string.dir_shared_path), "main");
		ed.commit();
	
		
		//To store user_id
		username = this.getSharedPreferences(getString(R.string.user_detail),
				Context.MODE_PRIVATE);
		
		
		if(data.getInt(getString(R.string.key), 0)!=0){
			Intent mnsr = new Intent(Login.this, Home.class);
			startActivity(mnsr);
			finish();;
		}
						
		go.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(user.getText().toString().equals("")){
					user.setBackgroundResource(R.drawable.outline_wrong);
					Toast.makeText(getApplicationContext(), "Enter User ID", Toast.LENGTH_LONG).show();
				}
				else if(pass.getText().toString().equals("")){
					pass.setBackgroundResource(R.drawable.outline_wrong);
					Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();
				}
				else{
					new log_process().execute(new Server_Interaction());
				}
			}
		});
		
		forget.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent forget  = new Intent(Login.this, Forgot_password.class);
				startActivity(forget);
			}
		});
		
		newac.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent new_ac  = new Intent(Login.this, New_ac.class);
				startActivity(new_ac);
				finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
	
	class log_process extends AsyncTask<Server_Interaction, String, String>{
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			load = new ProgressDialog(Login.this);
			load.setTitle("Signing In.....");
			load.setCancelable(false);
			load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			load.setMessage("Just A Second");
			load.show();
		}

		@Override
		protected String doInBackground(Server_Interaction... obj) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user",user.getText().toString()));
			params.add(new BasicNameValuePair("pass",pass.getText().toString()));
			
			int ans = obj[0].login_check(params);
			return ("" + ans);
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			load.dismiss();
			if(Integer.parseInt(result)==1){
				Intent mnsr = new Intent(Login.this,Home.class);
				//For Material Design Try!!!
				//Intent mnsr = new Intent(Login.this, Home_material.class);
				if(chk.isChecked()){
					SharedPreferences.Editor ed = data.edit();
					ed.putInt(getString(R.string.key), 1);
					ed.commit();
				}
				
				if(!username.getString(getString(R.string.shared_user_id), "NUll").
						equals(user.getText().toString())){
					SharedPreferences.Editor ed = username.edit();
					ed.putString(getString(R.string.shared_user_id), user.getText().toString());
					ed.commit();					
				}
				
				startActivity(mnsr);
				finish();
			}
			else{
				load.dismiss();
				Toast.makeText(getApplicationContext(),
						"INCORRECT ID OR PASSWORD", Toast.LENGTH_LONG).show();
			}
		}
		
	}
}
