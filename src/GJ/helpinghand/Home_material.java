package GJ.helpinghand;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Home_material extends AppCompatActivity {

	private DrawerLayout DrawerLay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_material);
		
		//Toolbar
		Toolbar tb =(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(tb);
		
		//Navigation Drawer
		
		DrawerLay = (DrawerLayout) findViewById(R.id.drawer_layout);
		NavigationView nav = (NavigationView) findViewById(R.id.navigation);
		
		nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			
			@Override
			public boolean onNavigationItemSelected(MenuItem arg0) {
				// TODO Auto-generated method stub
				
				DrawerLay.closeDrawers();
				int id = arg0.getItemId();
				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				switch(id){
				case R.id.design_home:
					fragmentManager.beginTransaction().replace(R.id.container,
							My_tab.newInstance(1)).commit();
					break;
				case R.id.design_shared:
					fragmentManager.beginTransaction().replace(R.id.container,
							My_tab.newInstance(2)).commit();
					break;
				case R.id.design_setting:
					Intent in = new Intent(Home_material.this,Settings.class);
					startActivity(in);
					break;
				default:
					fragmentManager.beginTransaction().replace(R.id.container,
							Home_Fragment_Activity.newInstance(0)).commit();
					break;					
				}

				return false;
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_material, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
			return super.onOptionsItemSelected(item);
	}
}
