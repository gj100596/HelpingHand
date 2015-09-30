package GJ.helpinghand;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Share_File_Fragment extends Fragment {
	
	private static final String ARG_SECTION_NUMBER = "section_number";
	private ListView content;
	View vw;
	private SharedPreferences data,directory;
	private ProgressDialog load;
	public static String folder;
	public static TextView selected_file_name,selected_file_type,selected_file_owner;
	private SwipeRefreshLayout refresh;
	
	public static Fragment newInstance(int sectionNumber) {
		Share_File_Fragment fragment = new Share_File_Fragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public Share_File_Fragment() {
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		 vw = inflater.inflate(R.layout.home_fragment_layout, null);
			
		 content = (ListView) vw.findViewById(R.id.shared_work);
		 refresh = (SwipeRefreshLayout) vw.findViewById(R.id.refresh);
		 refresh.setColorSchemeResources(R.color.material_blue_grey_800,R.color.my_primary);
		 
		 content.setDividerHeight(1);
		 data = getActivity().getSharedPreferences(getString(R.string.user_detail), 
					Context.MODE_PRIVATE);		
		 directory=getActivity().getSharedPreferences(getString(R.string.directory),
					Context.MODE_PRIVATE);
		 folder=directory.getString(getString(R.string.dir_shared_fol), "main");
		 
		 new get_shared_content().execute(new Server_Interaction());
			
		 content.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					View selected = content.getChildAt(position-content.getFirstVisiblePosition());
					selected_file_name = (TextView) selected.findViewById(R.id.title);
					selected_file_type = (TextView) selected.findViewById(R.id.file_type);
					selected_file_owner = (TextView) selected.findViewById(R.id.date);
					
					new download().execute(new Server_Interaction());
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
								selected_file_name=(TextView) clicked.findViewById(R.id.title);
								selected_file_type=(TextView) clicked.findViewById(R.id.file_type);
								selected_file_owner=(TextView) clicked.findViewById(R.id.date);
								Intent share = new Intent(getActivity(),Share.class);
								share.putExtra("mode", 2);
								startActivity(share);
								break;
							case 1:
								selected_file_name=(TextView) clicked.findViewById(R.id.title);
								new AlertDialog.Builder(getActivity())
									.setMessage("Do you Want to Delete the Folder?")
									.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											new delete_file().execute(new Server_Interaction());
										}
									})
									.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
										}
									})
									.show();
								Toast.makeText(getActivity(), "Removed", Toast.LENGTH_SHORT).show();
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
					
					return false;
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
					new get_shared_content().execute(new Server_Interaction());
				}
			});
		 
		 return vw;
	}

	class download extends AsyncTask<Server_Interaction, String, String>{
		
		NotificationCompat.Builder mBuilder;
		NotificationManager mNotifyManager;
		String dest,origin;
		int down=0;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			mNotifyManager = (NotificationManager) getActivity().
					getSystemService(Context.NOTIFICATION_SERVICE);
			mBuilder = new NotificationCompat.Builder(getActivity());
			
			mBuilder.setContentTitle("Downloading File")
			    .setContentText("Download in progress");
			
			switch(selected_file_type.getText().toString().toLowerCase()){
			case "jpg":
				mBuilder.setSmallIcon(R.drawable.jpg);
				break;
			case "pdf":
				mBuilder.setSmallIcon(R.drawable.pdf);
				break;
			case "txt":
				mBuilder.setSmallIcon(R.drawable.txt);
				break;
			default:
				mBuilder.setSmallIcon(R.drawable.file);
				break;
			}
			
			mBuilder.setProgress(0, 0, true);
			mNotifyManager.notify(1,mBuilder.build());
		}

		@Override
		protected String doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub
			
			origin = selected_file_owner.getText().toString()+
					directory.getString(getString(R.string.dir_shared_path), "main").substring(4)+"/"+
					selected_file_name.getText().toString();
			dest = "/sdcard/Helping Hand/Shared Files/"+
					selected_file_name.getText().toString();
			File check = new File(dest);
			if(!check.isFile())
				return params[0].download_file(origin,dest);
			down=1;
			mNotifyManager.cancel(1);
			return "true";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			mBuilder.setContentText("Download Completed").setProgress(0, 0, false);
			if(down!=1)
				mNotifyManager.notify(1,mBuilder.build());
			
			Intent showFile = new Intent();
			showFile.setAction(Intent.ACTION_VIEW);
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+
					"/Helping Hand/Shared Files/"+
					selected_file_name.getText().toString());
			switch(selected_file_type.getText().toString().toLowerCase()){
			case "jpg":
			case "png":
			case "gif":
			case "tif":
			case "jpeg":
				showFile.setDataAndType(Uri.fromFile(file),"image/*");
				break;
			case "pdf":
				showFile.setDataAndType(Uri.fromFile(file),"application/pdf");
				break;
			case "txt":
				showFile.setDataAndType(Uri.fromFile(file),"text/plain");
				break;
			default:
				showFile.setType("*/*");
				break;
			}
			showFile = Intent.createChooser(showFile, "Select to open file");
			startActivity(showFile);
		}	
	}
	
	class delete_file extends AsyncTask<Server_Interaction, String, String>{

		@Override
		protected String doInBackground(Server_Interaction... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("u_id", data.getString(getString(R.string.shared_user_id),"")));
			arg.add(new BasicNameValuePair("path", directory.getString(getString(R.string.dir_path), "main")));
			arg.add(new BasicNameValuePair("folder_name",selected_file_name.getText().toString()));
			
			return params[0].shared_file_work(arg);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(result.equalsIgnoreCase("True")){
				refresh.setRefreshing(true);
				new get_shared_content().execute(new Server_Interaction());
			}
		}
	}
	
	
	class get_shared_content extends AsyncTask<Server_Interaction, JSONArray, JSONArray>{

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
			
			return  params[0].share_file(arg);
			
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
