package GJ.helpinghand;

import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Settings extends Activity {
	
	private ListView list;
	private Intent go;
	private SharedPreferences data;
	private SharedPreferences rem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		data = this.getSharedPreferences(getString(R.string.user_detail),Context.MODE_PRIVATE);
		rem = Settings.this.getSharedPreferences(getString(R.string.filename),Context.MODE_PRIVATE);
		
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		list = (ListView) findViewById(R.id.set);
		
		final String[] content = new String[]{"Profile","Notification","Theme","Logout","FeedBack"};
		int[] res= new int[]{R.drawable.ic_account_circle_white_36dp,
							 R.drawable.ic_speaker_notes_white_36dp,
							 R.drawable.ic_store_white_36dp,
							 android.R.drawable.ic_lock_power_off,
							 R.drawable.ic_assessment_white_36dp};
		
		list.setAdapter(new Custom_rest_list(this, res, content));
		
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch(position){
				case 0:
					go = new Intent(Settings.this,Profile_setting.class);
					startActivity(go);
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					SharedPreferences.Editor ed = rem.edit();
					ed.putInt(getString(R.string.key), 0);
					ed.commit();
					
					SharedPreferences.Editor edit = data.edit();
					edit.clear();
					edit.commit();
					
					Intent home = new Intent(Settings.this,Login.class);
					home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(home);
					finish();
					break;
				case 4:
					Intent email = new Intent(Intent.ACTION_SEND);
					email.setType(HTTP.PLAIN_TEXT_TYPE);
					email.putExtra(Intent.EXTRA_EMAIL, "hhsuport@gmail.com");  
					email.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
					email = Intent.createChooser(email, "Select to Complete Action");
					startActivity(email);
					break;
				}
				Toast.makeText(getApplicationContext(), content[position],Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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
}
