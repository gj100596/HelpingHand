package GJ.helpinghand;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class My_tab extends Fragment {
	
	private View main;
	public static ViewPager pager;
	public static FragmentManager fm;
	private int mode;
	
	public static Fragment newInstance(int sectionNumber) {
		
		My_tab fragment = new My_tab(sectionNumber);
		Bundle args = new Bundle();
		args.putInt("Section_Number", sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	public My_tab(int x){
		mode=x;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View main= inflater.inflate(R.layout.fragment_my_tab, null);
		return main;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		pager = (ViewPager) view.findViewById(R.id.pager);
		fm = getChildFragmentManager();//.findFragmentById(R.id.pager_fragment);
		
		if(mode==1){
			PagerAdap adapter = new PagerAdap(fm);
			pager.setAdapter(adapter);
		}
		else{
			PagerFolderAdap adapter = new PagerFolderAdap(fm);
			pager.setAdapter(adapter);
		}				
	}
	
	
}
