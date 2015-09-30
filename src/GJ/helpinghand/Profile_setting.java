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
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Profile_setting extends Activity {
	
	private Button edit_name,edit_pass,save;
	private EditText name,old_pass,new_pass;
	//private ImageView dp;
	private TextView user_id;
	private SharedPreferences data;//dp_image;
	private int name_flag=0,pass_flag=0;
	private ProgressDialog load;
	private byte[] dpByteArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_setting);
		
		edit_name = (Button) findViewById(R.id.edit_name);
		edit_pass = (Button) findViewById(R.id.edit_pass);
		save = (Button) findViewById(R.id.go);
		name = (EditText)findViewById(R.id.username);
		old_pass = (EditText)findViewById(R.id.old_pass);
		new_pass = (EditText)findViewById(R.id.new_pass);
		user_id = (TextView) findViewById(R.id.profile_userid);
	//	dp = (ImageView) findViewById(R.id.dp);
		data = this.getSharedPreferences(getString(R.string.user_detail),Context.MODE_PRIVATE);
		//dp_image = this.getSharedPreferences(getString(R.string.dp),Context.MODE_PRIVATE);
		
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		user_id.setText(data.getString(getString(R.string.shared_user_id),""));
		
		/*String dp_uri_path = dp_image.getString("dp", "null");
		if(!dp_uri_path.equals("null")){
			Uri dp_uri = Uri.parse(dp_uri_path);
			Log.d("test",dp_uri.toString());
			dp.setImageURI(dp_uri);
		}
		edit_dp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent dp = new Intent();
				dp.setAction(Intent.ACTION_GET_CONTENT);
				dp.setType("image/*");
				dp  = Intent.createChooser(dp, "Select to Complete Action");
				startActivityForResult(dp, 1);
				
			}
		});*/
		
		//checking for user info from memory
		name.setText(data.getString(getString(R.string.shared_user_name), "User Name"));		
		
		old_pass.setVisibility(4);
		new_pass.setVisibility(4);
		name.setInputType( InputType.TYPE_NULL);
	//	dp.setBackgroundResource(R.drawable.ic_launcher);
		
		edit_name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name.setInputType(InputType.TYPE_CLASS_TEXT);
				name_flag=1;
				
				name.setCursorVisible(true);
			}
		});
		
		edit_pass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				old_pass.setVisibility(0);
				new_pass.setVisibility(0);
				if(!old_pass.getText().toString().equals("") && !new_pass.getText().toString().equals(""))
					pass_flag=1;
			}
		});	
		
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			if(name_flag!=0 || pass_flag!=0)
					new save_profile().execute(new Server_Interaction());

			}
		});
	}
	
	/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
			Uri uri  = data.getData();
			dp.setImageURI(uri);
			
			try {
				Bitmap bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
				dpByteArray = stream.toByteArray();
				
				new upload_image().execute(new Server_Interaction());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile_setting, menu);
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
	
	class upload_image extends AsyncTask<Server_Interaction, String, String>{

		@Override
		protected String doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub
			params[0].upload_image(dpByteArray,data.getString(getString(R.string.shared_user_id),""));
			return null;
		}
		
	}
	
	public class save_profile extends AsyncTask<Server_Interaction, String, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			load = new ProgressDialog(Profile_setting.this);
			load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			load.setTitle("Saving Your Data");
			load.setMessage("Making Changes....");
			load.show();			
		}
		
		@Override
		protected String doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub
			
			ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("u_id", data.getString(getString(R.string.shared_user_id),"")));
			if(name_flag==1)
				arg.add(new BasicNameValuePair("new_id", name.getText().toString()));
			if(pass_flag==1){
				arg.add(new BasicNameValuePair("user_pass", old_pass.getText().toString()));
				arg.add(new BasicNameValuePair("new_pass", new_pass.getText().toString()));
			}
			int reply = params[0].profile(arg);
			return ""+reply;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			load.dismiss();
			switch(Integer.parseInt(result)){
			case 1:
				Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
				SharedPreferences.Editor edit = data.edit();
				edit.putString(getString(R.string.shared_user_name), name.getText().toString());
				edit.commit();
				
				Intent main = new Intent(Profile_setting.this, Home.class);
				startActivity(main);
				break;
			case 0:
				Toast.makeText(getApplicationContext(), "Error Occured...Try Latter", Toast.LENGTH_SHORT).show();
				break;
			case -1:
				Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
				break;
			}
			
			Intent home = new Intent(Profile_setting.this,Home.class);
			startActivity(home);
			finish();
			
		}
		
	}
}
