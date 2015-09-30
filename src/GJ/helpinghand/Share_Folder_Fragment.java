package GJ.helpinghand;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import GJ.helpinghand.My_Fragment_Activity.get_content;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Share_Folder_Fragment extends Fragment{


		private static final String ARG_SECTION_NUMBER = "section_number";
		private ListView content;
		private SharedPreferences data,directory;
		private ProgressDialog load;
		private View vw;
		private EditText name;
		public static TextView folder_name,selected_folder,selected_folder_owner;
		private SwipeRefreshLayout refresh;
		
		public static Fragment newInstance(int sectionNumber) {
			Share_Folder_Fragment fragment = new Share_Folder_Fragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public Share_Folder_Fragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
		
			 vw = inflater.inflate(R.layout.share_folder_layout, null);
			 content = (ListView) vw.findViewById(R.id.my_work);
			 refresh = (SwipeRefreshLayout) vw.findViewById(R.id.refresh);
			 refresh.setColorSchemeResources(R.color.material_blue_grey_800,R.color.my_primary);
			 data = getActivity().getSharedPreferences(getString(R.string.user_detail), 
					 											Context.MODE_PRIVATE);
			 directory = getActivity().getSharedPreferences(getString(R.string.directory), 
						Context.MODE_PRIVATE);
			
			 content.setDividerHeight(1);
			 
			 new get_folder().execute(new Server_Interaction());
			 
			 content.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					View selected = content.getChildAt(position-content.getFirstVisiblePosition());
					folder_name = (TextView) selected.findViewById(R.id.title);
					String fold = folder_name.getText().toString();
					
					//---Store Folder Name for next Activity-----
					SharedPreferences.Editor edit = directory.edit();
					edit.putString(getString(R.string.dir_shared_fol), fold);
					String path = directory.getString(getString(R.string.dir_shared_path), "main") 
							+ "/" + fold;
					edit.putString(getString(R.string.dir_shared_path), path);
					edit.commit();
					
					//Start new Activity so that on pressing back it will come one step back!
					Intent new_win = new Intent(getActivity(),Home.class);
					new_win.putExtra("shared", 1);
					startActivity(new_win);
				}
			 });
			 
			content.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					// TODO Auto-generated method stub
					final View clicked = (View) content.getChildAt(position-content.getFirstVisiblePosition());	
					String option_pro[] = new String[]{"Share",
													   "Remove",
													   "Report Content"};
					AlertDialog.Builder options = new AlertDialog.Builder(getActivity());
					
					options.setItems(option_pro, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							switch(which){
							case 0:
								selected_folder=(TextView) clicked.findViewById(R.id.title);
								selected_folder_owner=(TextView) clicked.findViewById(R.id.date);
								Intent share = new Intent(getActivity(),Share.class);
								share.putExtra("mode", 3);
								startActivity(share);
								break;
							case 1:
								selected_folder=(TextView) clicked.findViewById(R.id.title);
								new AlertDialog.Builder(getActivity())
									.setMessage("Do you Want to Delete the Folder?")
									.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											new delete_folder().execute(new Server_Interaction());
										}
									})
									.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
										}
									})
									.show();
								Toast.makeText(getActivity(), "Remove", Toast.LENGTH_SHORT).show();
								break;
							case 2:
								Toast.makeText(getActivity(), "The Object Will be Checked! Thank You", Toast.LENGTH_SHORT).show();
								break;
							default:
								Toast.makeText(getActivity(), "ERRRR", Toast.LENGTH_SHORT).show();
								break;
							}
						}
					});
					options.show();
					
					return true;
				}
			});
			
			content.setOnScrollListener(new  OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					int topRowVerticalPosition = 
						      (content == null || content.getChildCount() == 0) ? 
						        0 : content.getChildAt(0).getTop();
					refresh.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
					
				}
			});
			
			refresh.setOnRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					new get_folder().execute(new Server_Interaction());
				}
			});
			 
			 return vw;
		}
		class rename_folder extends AsyncTask<Server_Interaction, String, String>{

			@Override
			protected String doInBackground(Server_Interaction... params) {
				// TODO Auto-generated method stub
				ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
				arg.add(new BasicNameValuePair("u_id", data.getString(getString(R.string.shared_user_id),"")));
				arg.add(new BasicNameValuePair("path", 
						directory.getString(getString(R.string.dir_path), "main")));
				arg.add(new BasicNameValuePair("folder_name",selected_folder.getText().toString()));
				arg.add(new BasicNameValuePair("rename",name.getText().toString()));
				
				return params[0].shared_folder_work(arg);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if(result.equalsIgnoreCase("True")){
					refresh.setRefreshing(true);
					new get_folder().execute(new Server_Interaction());
				}					
			}
		}
		
		class delete_folder extends AsyncTask<Server_Interaction, String, String>{

			@Override
			protected String doInBackground(Server_Interaction... params) {
				// TODO Auto-generated method stub
				ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
				arg.add(new BasicNameValuePair("u_id", data.getString(getString(R.string.shared_user_id),"")));
				arg.add(new BasicNameValuePair("path", directory.getString(getString(R.string.dir_path), "main")));
				arg.add(new BasicNameValuePair("folder_name",selected_folder.getText().toString()));
				
				return params[0].shared_folder_work(arg);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if(result.equalsIgnoreCase("True")){
					refresh.setRefreshing(true);
					new get_folder().execute(new Server_Interaction());
				}
			}
		}
		
		
		class get_folder extends AsyncTask<Server_Interaction, JSONArray, JSONArray>{

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				if(!refresh.isRefreshing()){
					load = new ProgressDialog(getActivity());
					load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					load.setTitle("Loading");
					load.setMessage("Fetching Your Data...");
					load.show();
				}
				
			}
		
			@Override
			protected JSONArray doInBackground(Server_Interaction... params) {
				// TODO Auto-generated method stub

				ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
				arg.add(new BasicNameValuePair("u_id", data.getString(getString(R.string.shared_user_id),"")));
				arg.add(new BasicNameValuePair("folder", 
						directory.getString(getString(R.string.dir_shared_path), "main")));
				return  params[0].share_folder(arg);
				
			}

			@Override
			protected void onPostExecute(JSONArray result) {
				// TODO Auto-generated method stub
				load.dismiss();
				refresh.setRefreshing(false);
				Custom_content_list adap;
				if(result!=null){
					adap = new Custom_content_list(getActivity(),result,1);
					content.setAdapter(adap);
				}
				else{
					//adap = new Custom_content_list(getActivity(),result,res);
				}
				super.onPostExecute(result);
			}
			
		}

}
