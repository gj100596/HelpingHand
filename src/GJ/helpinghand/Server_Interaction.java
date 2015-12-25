package GJ.helpinghand;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Server_Interaction {
	
	public int login_check(ArrayList<NameValuePair> param){
		
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			//HttpPost con = new HttpPost("http://192.168.1.40:45450/login.php");
			HttpPost con = new HttpPost("http://yourserver/login.php");
			con.setEntity(new UrlEncodedFormEntity(param));
			
			HttpResponse response = httpclient.execute(con);
			String jsonString = EntityUtils.toString(response.getEntity());
			
			//JSONArray jarray = new JSONArray(jsonString);
			//Log.d("Delete Product", jarray.toString());
			JSONObject jobj = new JSONObject(jsonString);
			
			if(jobj.getString("result").equals("True"))
				return 1;		
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
		
	}
	
	public String new_user(ArrayList<NameValuePair> param){
		
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			//HttpPost con = new HttpPost("http://192.168.1.40:45450/login.php");
			HttpPost con = new HttpPost("http://yourserver/new_ac.php");
			con.setEntity(new UrlEncodedFormEntity(param));
			
			HttpResponse response = httpclient.execute(con);
			String jsonString = EntityUtils.toString(response.getEntity());
			
			JSONObject jobj = new JSONObject(jsonString);
			
			return jobj.getString("result");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public int profile(ArrayList<NameValuePair> params){
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://yourserver/profile.php");	
			request.setEntity(new UrlEncodedFormEntity(params));
			
			HttpResponse response = client.execute(request);
			String answer = EntityUtils.toString(response.getEntity());
			
			JSONObject ans = new JSONObject(answer);
			
			if(ans.getString("user").equalsIgnoreCase("User Not Found")){
				Log.d("User-Not Found",ans.getString("user"));
				return 0;
			}
			if(ans.getString("password").equalsIgnoreCase("User Not Found") ||
					ans.getString("password").equalsIgnoreCase("Error")){
				Log.d("Password-Not Found",ans.getString("password"));
				return 0;				
			}
			if(ans.getString("password").equalsIgnoreCase("Incorrect Password")){
				Log.d("Password-Incorrect",ans.getString("password"));
				return -1;
			}
			return 1;
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public JSONArray my_file(ArrayList<NameValuePair> params){
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://yourserver/my_data.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
			String JsonString = EntityUtils.toString(response.getEntity());
			
			JSONObject jobj = new JSONObject(JsonString);
			JSONArray jarry = null;
			if(!jobj.isNull("temp"))
				jarry = jobj.getJSONArray("temp");
			
			return jarry;
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public JSONArray my_folder(ArrayList<NameValuePair> params){
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://yourserver/my_folder.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
			String JsonString = EntityUtils.toString(response.getEntity());
			
			JSONObject jobj = new JSONObject(JsonString);
			JSONArray jarry = null;
			if(!jobj.isNull("temp"))
				jarry = jobj.getJSONArray("temp");
			
			return jarry;
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public JSONArray my_group(ArrayList<NameValuePair> params){
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://yourserver/my_group.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
			String JsonString = EntityUtils.toString(response.getEntity());
			JSONObject jobj = new JSONObject(JsonString);
			JSONArray jarry = null;
			if(!jobj.isNull("temp"))
				jarry = jobj.getJSONArray("temp");
			
			return jarry;
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public JSONArray group_detail(ArrayList<NameValuePair> params){
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://yourserver/group_detail.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
			String JsonString = EntityUtils.toString(response.getEntity());
			JSONObject jobj = new JSONObject(JsonString);
			
			JSONArray jarry = null;
			if(!jobj.isNull("temp"))
				jarry = jobj.getJSONArray("temp");
			
			return jarry;
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public JSONArray share_file(ArrayList<NameValuePair> params){
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://yourserver/share_file.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
			String JsonString = EntityUtils.toString(response.getEntity());
			
			JSONObject jobj = new JSONObject(JsonString);
			JSONArray jarry = null;
			if(!jobj.isNull("temp"))
				jarry = jobj.getJSONArray("temp");
			
			return jarry;
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public JSONArray share_folder(ArrayList<NameValuePair> params){
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://yourserver/shared_folder.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
			String JsonString = EntityUtils.toString(response.getEntity());
			
			JSONObject jobj = new JSONObject(JsonString);
			JSONArray jarry = null;
			if(!jobj.isNull("temp"))
				jarry = jobj.getJSONArray("temp");
			
			return jarry;
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	public String user_name(ArrayList<NameValuePair> params){
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://yourserver/user_name.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
			String JsonString = EntityUtils.toString(response.getEntity());
			
			JSONObject jobj = new JSONObject(JsonString);
			return jobj.getString("result");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public void create_group(ArrayList<NameValuePair> params){
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://yourserver/creategroup.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void update_group(ArrayList<NameValuePair> params){
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://yourserver/updategroup.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String new_folder(ArrayList<NameValuePair> params){
		http://yourserver					
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://yourserver/my_new_folder.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
			String JsonString = EntityUtils.toString(response.getEntity());
			JSONObject jobj = new JSONObject(JsonString);
			return jobj.getString("result");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String shared_folder_work(ArrayList<NameValuePair> params){
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://yourserver/shared_folder_work.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
			String JsonString = EntityUtils.toString(response.getEntity());
			Log.d("dasgffs",JsonString);
			JSONObject jobj = new JSONObject(JsonString);
			return jobj.getString("result");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String shared_file_work(ArrayList<NameValuePair> params){
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://yourserver/shared_file_work.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
			String JsonString = EntityUtils.toString(response.getEntity());
			Log.d("dasgffs",JsonString);
			JSONObject jobj = new JSONObject(JsonString);
			return jobj.getString("result");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String file_work(ArrayList<NameValuePair> params){
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://yourserver/my_file_work.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
			String JsonString = EntityUtils.toString(response.getEntity());
			Log.d("dasgffs",JsonString);
			JSONObject jobj = new JSONObject(JsonString);
			return jobj.getString("result");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String sharing_file(ArrayList<NameValuePair> params,int mode){
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post;
			if(mode==0)
				post = new HttpPost("http://yourserver/sharing_file_thru_group.php");
			else
				post = new HttpPost("http://yourserver/sharing_file_thru_member.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
			String JsonString = EntityUtils.toString(response.getEntity());
			
			JSONObject jobj = new JSONObject(JsonString);
			return jobj.getString("result");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String sharing_folder(ArrayList<NameValuePair> params,int mode){
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post;
			if(mode==0)
				post = new HttpPost("http://yourserver/sharing_folder_thru_group.php");
			else
				post = new HttpPost("http://yourserver/sharing_folder_thru_member.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
			String JsonString = EntityUtils.toString(response.getEntity());
			Log.d("dsa",JsonString);
			JSONObject jobj = new JSONObject(JsonString);
			return jobj.getString("result");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONArray suggestion(ArrayList<NameValuePair> params){
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://yourserver/suggestion.php");
			
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(post);
			
			String JsonString = EntityUtils.toString(response.getEntity());
			
			JSONObject jobj = new JSONObject(JsonString);
			JSONArray jarry = null;
			if(!jobj.isNull("temp")){
				jarry = jobj.getJSONArray("temp");
				return jarry;
			}
			return null;
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String download_file(String file, String saveDir){
		
		try {
			//URL url = new URL("http://yourserver/Docs" + file);
			URL url = new URL("http://yourserver/Docs/"+file);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			int responseCode = httpConn.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) {
				Log.d("response","ok");
				String contentType = httpConn.getContentType();
				int contentLength = httpConn.getContentLength();
				
				// opens input stream from the HTTP connection
				InputStream inputStream = httpConn.getInputStream();
				
				// opens an output stream to save into file
				FileOutputStream outputStream = new FileOutputStream(saveDir);
				
				int bytesRead = -1;
				byte[] buffer = new byte[4096];
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				outputStream.close();
				inputStream.close();
				
				httpConn.disconnect();
				return "true";
			} 
			else {
				Log.d("No file to download. Server replied HTTP code: " ,"" + responseCode);
				httpConn.disconnect();
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "false";
	}

	public String upload_file(String filename,String userID, String type, String path){
	    
	    HttpURLConnection connection = null;
	    DataOutputStream outputStream = null;
 
	    String lineEnd = "\r\n";
	    String twoHyphens = "--";
	    String boundary = "*****";
 
	    int bytesRead, bytesAvailable, bufferSize;
	    byte[] buffer;
	    int maxBufferSize = 1 * 1024;
	    try {
	        FileInputStream fileInputStream = new FileInputStream(new File(filename));
 
	        URL url = new URL("http://yourserver/upload.php");
	        connection = (HttpURLConnection) url.openConnection();
 
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setUseCaches(false);
	        connection.setRequestMethod("POST");
 
	        connection.setRequestProperty("Connection", "Keep-Alive");
	        connection.setRequestProperty("Content-Type",
	                "multipart/form-data; boundary=" + boundary);
 
	        outputStream = new DataOutputStream(connection.getOutputStream());
	        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
 
	        
	        outputStream.writeBytes("Content-Disposition: form-data; name=\"u_id\"" + lineEnd);
	        outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
	        outputStream.writeBytes("Content-Length: " + userID.length() + lineEnd);
	        outputStream.writeBytes(lineEnd);
	        outputStream.writeBytes(userID + lineEnd);
	        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
 
	        outputStream.writeBytes("Content-Disposition: form-data; name=\"path\"" + lineEnd);
	        outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
	        outputStream.writeBytes("Content-Length: " + path.length() + lineEnd);
	        outputStream.writeBytes(lineEnd);
	        outputStream.writeBytes(path + lineEnd);
	        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
	        
	        outputStream.writeBytes("Content-Disposition: form-data; name=\"type\"" + lineEnd);
	        outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
	        outputStream.writeBytes("Content-Length: " + path.length() + lineEnd);
	        outputStream.writeBytes(lineEnd);
	        outputStream.writeBytes(type + lineEnd);
	        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
 
	        String fname[] = filename.split("/");
	        outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
	                + fname[fname.length-1] + "\"" + lineEnd);
	        outputStream.writeBytes(lineEnd);
 
	      	bytesAvailable = fileInputStream.available();
	        bufferSize = Math.min(bytesAvailable, maxBufferSize);
	        buffer = new byte[bufferSize];
 
	        // Read file
	        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	        while (bytesRead > 0) {
                try {
                    outputStream.write(buffer, 0, bufferSize);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
	        
	        outputStream.writeBytes(lineEnd);
	        outputStream.writeBytes(twoHyphens + boundary + twoHyphens
	                + lineEnd);
	        
	        InputStream iStream = connection.getInputStream();
            char arry[] = new char[1000];
            Reader in = new InputStreamReader(iStream, "UTF-8");
            in.read(arry);
            String response = new String(arry);
	        outputStream.close();
	        
            return response;
	    } catch (Exception ex) {
	        // Exception handling
	        ex.printStackTrace();
	    }
	    return "error";
	}
}
