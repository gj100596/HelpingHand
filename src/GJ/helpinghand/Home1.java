package GJ.helpinghand;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Home1 extends FragmentActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private SharedPreferences data,directory;
	public static android.support.v4.app.FragmentManager fragmentManager;
	static String pathAfterIntent;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		//ActionBarHelp=1;
		data = this.getSharedPreferences(getString(R.string.user_detail), 
					Context.MODE_PRIVATE);
		directory = this.getSharedPreferences(getString(R.string.directory), 
				Context.MODE_PRIVATE);
		directory.getString(getString(R.string.dir_fol), "main");
		directory.getString(getString(R.string.dir_path), "main");
		directory.getString(getString(R.string.dir_shared_fol), "main");
		directory.getString(getString(R.string.dir_shared_path), "main");
		
		setContentView(R.layout.activity_home);
		onSectionAttached(1);
		restoreActionBar();
		
		new set_username().execute(new Server_Interaction());

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		//Create folder in SDCARD!
		File folder = new File("/sdcard/Helping Hand");
		if(!folder.isDirectory()){
			folder.mkdir();
		}
		File my_folder = new File("/sdcard/Helping Hand/My Files");
		if(!my_folder.isDirectory()){
			my_folder.mkdir();
		}
		File shared_folder = new File("/sdcard/Helping Hand/Shared Files");
		if(!shared_folder.isDirectory()){
			shared_folder.mkdir();
		}
		
		//Select The shared tab if intent is from shared folder
		if(getIntent().hasExtra("shared")){
			Bundle arg = getIntent().getExtras();
			Log.d("in","yes");
			if(arg.getInt("shared")==1)
				onNavigationDrawerItemSelected(1);
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1 && resultCode == RESULT_OK){
			Uri uri = data.getData();
			Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		    if (cursor == null) { // Source is Dropbox or other similar local file path
		        pathAfterIntent = uri.getPath();
		    } else { 
		        cursor.moveToFirst(); 
		        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
		        pathAfterIntent = cursor.getString(idx);
		    }
		    cursor.close();
		}
		else
			My_Fragment_Activity.file=null;
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		fragmentManager = getSupportFragmentManager();
		switch(position+1){
		case 1:
			onSectionAttached(1);
			restoreActionBar();
			fragmentManager.beginTransaction().replace(R.id.container,
					My_tab.newInstance(position+1)).commit();
			break;
		case 2:
			onSectionAttached(2);
			restoreActionBar();
			fragmentManager.beginTransaction().replace(R.id.container,
					My_tab.newInstance(position+1)).commit();
			break;
		case 3:
			onSectionAttached(3);
			restoreActionBar();
			fragmentManager.beginTransaction().replace(R.id.container,
					new group()).commit();
			break;
		case 4:
		case 5:
			Intent in = new Intent(Home1.this,Settings.class);
			startActivity(in);
			break;
		default:
			fragmentManager.beginTransaction().replace(R.id.container,
					Home_Fragment_Activity.newInstance(position+1)).commit();
			break;					
		}
		//fragmentManager.beginTransaction().replace(R.id.container,
		//				PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			
				if(directory.getString(getString(R.string.dir_fol), "main").equalsIgnoreCase("main"))
					mTitle = getString(R.string.title_section1);
				else
					mTitle=directory.getString(getString(R.string.dir_fol), "main");
			break;
		case 2:
			if(directory.getString(getString(R.string.dir_shared_fol), "main").equalsIgnoreCase("main"))
				mTitle = getString(R.string.title_section2);
			else
				mTitle=directory.getString(getString(R.string.dir_shared_fol), "main");
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.home, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}
	
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		String str = directory.getString(getString(R.string.dir_path), "main"); 
		if(!directory.getString(getString(R.string.dir_fol), "main").equals("main")){
			String[] part = str.split("/");
			String temp="main";
			for(int i=1;i<part.length-1;i++)
				temp=temp+"/"+part[i];
			SharedPreferences.Editor edit = directory.edit();
			edit.putString(getString(R.string.dir_fol), part[part.length-2]);
			edit.putString(getString(R.string.dir_path), temp);
			edit.commit();
		}
		String str1 = directory.getString(getString(R.string.dir_shared_path), "main"); 
		if(!directory.getString(getString(R.string.dir_shared_fol), "main").equals("main")){
			String[] part = str1.split("/");
			String temp="main";
			for(int i=1;i<part.length-1;i++)
				temp=temp+"/"+part[i];
			SharedPreferences.Editor edit = directory.edit();
			edit.putString(getString(R.string.dir_shared_fol), part[part.length-2]);
			edit.putString(getString(R.string.dir_shared_path), temp);
			edit.commit();
		}
	}

	class set_username extends AsyncTask<Server_Interaction, String, String>{

		@Override
		protected String doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> para = new ArrayList<NameValuePair>();
			para.add(new BasicNameValuePair("u_id", data.getString(getString(R.string.shared_user_id),"")));
			
			return params[0].user_name(para);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			
			NavigationDrawerFragment.nav_name.setText(result);
			
			SharedPreferences.Editor ed = data.edit();
			ed.putString(getString(R.string.shared_user_name), result);
			ed.commit();
		}
	}

}
