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
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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


public class My_Folder_Fragment extends Fragment implements android.view.View.OnClickListener{


		private static final String ARG_SECTION_NUMBER = "section_number";
		private ListView content;
		private SharedPreferences data,directory;
		private ProgressDialog load;
		private View vw;
		private EditText name;
		public static TextView folder_name,selected_folder;
		private SwipeRefreshLayout refresh;
		
		public static Fragment newInstance(int sectionNumber) {
			My_Folder_Fragment fragment = new My_Folder_Fragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public My_Folder_Fragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
		
			 vw = inflater.inflate(R.layout.my_folder_layout, null);
			 content = (ListView) vw.findViewById(R.id.my_work);
			 refresh = (SwipeRefreshLayout) vw.findViewById(R.id.refresh);
			 refresh.setColorSchemeResources(R.color.material_blue_grey_800,R.color.my_primary);
			 data = getActivity().getSharedPreferences(getString(R.string.user_detail), 
					 											Context.MODE_PRIVATE);
			 directory = getActivity().getSharedPreferences(getString(R.string.directory), 
						Context.MODE_PRIVATE);
			 new get_folder().execute(new Server_Interaction());
			
			 content.setDividerHeight(1);
			 
			 //----------FAB--------
			 FloatingActionButton fab = (FloatingActionButton) vw.findViewById(R.id.fab);
			fab.setOnClickListener(this);
				
			
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
					edit.putString(getString(R.string.dir_fol), fold);
					String path = directory.getString(getString(R.string.dir_path), "main") 
							+ "/" + fold;
					edit.putString(getString(R.string.dir_path), path);
					edit.commit();
					
					//Start new Activity so that on pressing back it will come one step back!
					Intent new_win = new Intent(getActivity(),Home.class);
					startActivity(new_win);
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
			 
			 content.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
					View view, int position, long id) {
					final View clicked = (View) content.getChildAt(position-content.getFirstVisiblePosition());
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
								selected_folder=(TextView) clicked.findViewById(R.id.title);
								Intent share = new Intent(getActivity(),Share.class);
								share.putExtra("mode", 1);
								startActivity(share);
								break;
							case 1:
								selected_folder=(TextView) clicked.findViewById(R.id.title);
								name = new EditText(getActivity());
								name.setHint(R.string.fold_alert);
								new AlertDialog.Builder(getActivity())
									.setView(name)
									.setPositiveButton("Save", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											String fold = name.getText().toString();
											if(!fold.contains("/") || !fold.equals(""))
												new rename_folder().execute(new Server_Interaction());
											else
												Toast.makeText(getActivity(),
														"Folder name Can't Contain '/'", Toast.LENGTH_SHORT).show();
										}
									})
									.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
										}
									})
									.show();
								break;
							case 2:
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
								Toast.makeText(getActivity(), "Removed", Toast.LENGTH_SHORT).show();
								break;
							case 3:
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
			 
			refresh.setOnRefreshListener(new OnRefreshListener() {
					
					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						new get_folder().execute(new Server_Interaction());
					}
			});
			 
			 return vw;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.fab){
					name = new EditText(getActivity());
					name.setHint(R.string.fold_alert);
					new AlertDialog.Builder(getActivity())
						.setView(name)
						.setPositiveButton("Save", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								String fold = name.getText().toString();
								if(!fold.contains("/") || !fold.equals(""))
									new new_folder().execute(new Server_Interaction());
								else
									Toast.makeText(getActivity(),
											"Folder name Can't Contain '/'", Toast.LENGTH_SHORT).show();
							}
						})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
							}
						})
						.show(); 
			}
		}
		
		class new_folder extends AsyncTask<Server_Interaction, String, String>{
			
			ProgressDialog wait;
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				wait=new ProgressDialog(getActivity());
				wait.setTitle("Creating New Folder");
				wait.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				wait.setMessage("Setting Things Up...");
			}

			@Override
			protected String doInBackground(Server_Interaction... params) {
				// TODO Auto-generated method stub
				ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
				arg.add(new BasicNameValuePair("u_id", data.getString(getString(R.string.shared_user_id),"")));
				arg.add(new BasicNameValuePair("path", 
						directory.getString(getString(R.string.dir_path), "main")));
				arg.add(new BasicNameValuePair("folder_name",name.getText().toString()));
				
				return params[0].new_folder(arg);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				wait.dismiss();
				if(result.equalsIgnoreCase("True")){
					//---Store Folder Name for next Activity-----
					SharedPreferences.Editor edit = directory.edit();
					edit.putString(getString(R.string.dir_fol), name.getText().toString());
					String path = directory.getString(getString(R.string.dir_path), "main") 
							+ "/" + name.getText().toString();
					edit.putString(getString(R.string.dir_path), path);
					edit.commit();
					
					//Start new Activity so that on pressing back it will come one step back!
					Intent new_win = new Intent(getActivity(),Home.class);
					startActivity(new_win);
				}
				else
					Toast.makeText(getActivity(), "Error Occured. Try Latter!", Toast.LENGTH_SHORT).show();
			}
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
				
				return params[0].new_folder(arg);
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
				arg.add(new BasicNameValuePair("remove","1"));
				
				return params[0].new_folder(arg);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
					refresh.setRefreshing(true);
					new get_folder().execute(new Server_Interaction());
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
						directory.getString(getString(R.string.dir_path), "main")));
				
				return  params[0].my_folder(arg);
				
			}

			@Override
			protected void onPostExecute(JSONArray result) {
				// TODO Auto-generated method stub
				load.dismiss();
				refresh.setRefreshing(false);
				Custom_content_list adap;
				if(result!=null){
					adap = new Custom_content_list(getActivity(),result,0);
					content.setAdapter(adap);
				}
				else{
					//adap = new Custom_content_list(getActivity(),result,res);
				}
				super.onPostExecute(result);
			}
			
		}
}
