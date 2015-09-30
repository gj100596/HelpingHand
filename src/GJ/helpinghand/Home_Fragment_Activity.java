package GJ.helpinghand;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Home_Fragment_Activity extends Fragment {
	
	private static final String ARG_SECTION_NUMBER = "section_number";
	private ListView content;
	View vw;
	private SharedPreferences data;
	private ProgressDialog load;
	public String folder="main";
	
	public static Fragment newInstance(int sectionNumber) {
		Home_Fragment_Activity fragment = new Home_Fragment_Activity();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public Home_Fragment_Activity() {
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((Home) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		 vw = inflater.inflate(R.layout.home_fragment_layout, null);
			
		 content = (ListView) vw.findViewById(R.id.shared_work);
		 
		 content.setDivider(null);
		 content.setDividerHeight(1);
		 data = getActivity().getSharedPreferences(getString(R.string.user_detail), 
					Context.MODE_PRIVATE);		 
		 new get_shared_content().execute(new Server_Interaction());
			
		 content.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), 
							"The item ie file will be shown", Toast.LENGTH_SHORT).show();
				}
			 });
			 
			content.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					// TODO Auto-generated method stub
					String option_pro[] = new String[]{"Share",
													   "Rename",
													   "Remove",
													   "Report Content"};
					AlertDialog.Builder options = new AlertDialog.Builder(getActivity());
					
					options.setItems(option_pro, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							switch(which){
							case 0:
								Toast.makeText(getActivity(), "Share", Toast.LENGTH_SHORT).show();
								break;
							case 1:
								Toast.makeText(getActivity(), "Rename", Toast.LENGTH_SHORT).show();
								break;
							case 2:
								Toast.makeText(getActivity(), "Remove", Toast.LENGTH_SHORT).show();
								break;
							case 3:
								Toast.makeText(getActivity(), "Report", Toast.LENGTH_SHORT).show();
								break;
							default:
								Toast.makeText(getActivity(), "ERRRR", Toast.LENGTH_SHORT).show();
								break;
							}
						}
					});
					options.show();
					
					return false;
				}
			});
		 
		 return vw;
	}
	class get_shared_content extends AsyncTask<Server_Interaction, JSONArray, JSONArray>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			load = new ProgressDialog(getActivity());
			load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			load.setTitle("Loading");
			load.setMessage("Fetching Your Data...");
			load.show();
			
		}
	
		@Override
		protected JSONArray doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub

			ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("u_id", data.getString(getString(R.string.shared_user_id),"")));
			arg.add(new BasicNameValuePair("folder", folder));
			
			return  params[0].share_file(arg);
			
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			// TODO Auto-generated method stub
			load.dismiss();
			Custom_content_list adap;
			if(result!=null){
				adap = new Custom_content_list(getActivity(),result,1);
				content.setAdapter(adap);
			}
			else{
				//adap = new Custom_content_list(getActivity(),result,res);
				Toast.makeText(getActivity(), "No Files Yet", Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
		
	}


}
