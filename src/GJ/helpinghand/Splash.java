package GJ.helpinghand;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Splash extends Activity {
	
	//static String site;
	//sActivity ac =this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		getActionBar().hide();
		/*
		 *
		    PackageManager localPackageManager = this.getPackageManager();
		    try {
		        Resources localResources = this.getResources();
		        localResources.getString(R.string.common_google_play_services_unknown_issue);
		    } catch (Throwable localThrowable1) {
		        Log.e("GooglePlayServicesUtil", "The Google Play services resources were not found. "
		                + "Check your project configuration to ensure that the resources are included.");
		    }
		    */
		
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					Intent it = new Intent(Splash.this, Login.class);
					startActivity(it);
					finish();
				}
			}
		}.start();
		
		
		/*View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.ip, null);
		AlertDialog.Builder ip = new AlertDialog.Builder(ac);
		ip.setView(view);
		ip.setCancelable(false);
		final AlertDialog ipShow = ip.show();
		
		ipname = (EditText) view.findViewById(R.id.name);
		last = (Button) view.findViewById(R.id.last);
		go = (Button) view.findViewById(R.id.go);
		
		final SharedPreferences ipFile = getSharedPreferences("ip", Context.MODE_PRIVATE);
		last.setText(ipFile.getString("ipname", "-"));
		
		final InputMethodManager kb = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		
		last.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!ipFile.getString("ipname", "-").equals("-")){
					site=ipFile.getString("ipname", "-");
					ipShow.dismiss();
						new Thread(new Runnable() {
							public void run() {
								try {
									URL url = new URL("http://"+site+":45450/my_group.php");
									final HttpURLConnection check =(HttpURLConnection) url.openConnection();
									if(check.getResponseCode()== HttpURLConnection.HTTP_OK)
										Thread.sleep(2000);
									else
										Toast.makeText(getApplicationContext(),"Server Is Down!", Toast.LENGTH_LONG).show();
									check.disconnect();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}finally{
									Intent it = new Intent(Splash.this, Login.class);
									startActivity(it);
									finish();
								}
							}
						}).run();
				}
			}
		});
		
		go.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!ipname.getText().toString().equals("")){
					site=ipname.getText().toString();
					SharedPreferences.Editor edit = ipFile.edit();
					edit.putString("ipname", site);
					edit.commit();
					kb.hideSoftInputFromWindow(ipShow.getCurrentFocus().getWindowToken(), 0);
					ipShow.dismiss();
					new Thread(new Runnable() {
						public void run() {
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}finally{
								Intent it = new Intent(Splash.this, Login.class);
								startActivity(it);
								finish();
							}
						}
					}).run();
					
				}
			}
		});*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
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
