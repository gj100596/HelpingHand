package GJ.helpinghand;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;



public class PagerFolderAdap extends FragmentStatePagerAdapter{

	public PagerFolderAdap(FragmentManager fm) {
		super(fm);
	}

	public Fragment getItem(int arg0) {
		switch(arg0){
		case 0:
			return Share_File_Fragment.newInstance(0);
		case 1:
			return Share_Folder_Fragment.newInstance(1);
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		if(position==0)
			return "File";
		else
			return "Folder";
	}

}
