package net.ralphpina.revappralphpina;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.util.Log;

public class RevUtils {
	
	private static final String TAG = "RevUtils";
	
	private static RevUtils revUtils;
	private HttpURLConnection connection;
	private URL url;
	private SecurityQuestionsActivity activity;
	private ArrayList<String> securityQuestions;
	
	public static RevUtils getInstance(SecurityQuestionsActivity activity) {
		if (revUtils == null) {
			revUtils = new RevUtils();
		}
		revUtils.activity = activity;
		return revUtils;
	}
	
	private RevUtils() {
		securityQuestions = new ArrayList<String>();
	}
	
	public void callServerforQuestions() {
		activity.progress.show();
		new ApiTransaction().execute();
	}
	
	public ArrayList<String> getAvailableSecurityQuestions() {
		
		return securityQuestions;
	}
	
	private class ApiTransaction extends AsyncTask<String, Integer, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			//accept no cookies
			CookieManager cookieManager = new CookieManager();
			cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_NONE);
			CookieHandler.setDefault(cookieManager);
			
			try {
				System.setProperty("http.keepAlive", "false");
				url = new URL(new URI("https://api-qa2.revworldwide.com/v1/securityQuestions").toASCIIString());
				connection = (HttpURLConnection) url.openConnection();
				TrustModifier.relaxHostChecking(connection); // here's where the magic happens
				connection.setDoInput(true);
				connection.setUseCaches(false);	
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Accept", "application/json");

				String response = readResponse(connection.getInputStream());
				
				return new JSONObject(response);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
						
			return null;
		}
		
		protected void onPostExecute(JSONObject result) {
			activity.progress.dismiss();
			processResponse(result);
		}
		
		private String readResponse(InputStream stream) throws IOException {
			ByteArrayBuffer baf = new ByteArrayBuffer(50);

			int bytesRead = -1;
			byte[] buffer = new byte[1024];
			while ((bytesRead = stream.read(buffer)) >= 0) {
				// process the buffer, "bytesRead" have been read, no more, no less
				baf.append(buffer, 0, bytesRead);
			}
			stream.close();
			return new String(baf.toByteArray());
		}
	}
	
	private void processResponse(JSONObject result) {
		try {
			securityQuestions.clear();
			securityQuestions.add(activity.getString(R.string.SecurityQuestion_selectQuestion));
			@SuppressWarnings("unchecked")
			Iterator<String> itr = result.keys();
			while (itr.hasNext()) {
				String questionNumber = itr.next();
				String questionCode = result.getString(questionNumber);
				securityQuestions.add(getStringResourceByName("SecurityQuestion." + questionCode));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			activity.dataChanged();
		}
	}
	
	private String getStringResourceByName(String string) {
	      String packageName = activity.getPackageName();
	      int resId = activity.getResources().getIdentifier(string, "string", packageName);
	      String question = null;
	      try {
	    	  question = activity.getString(resId);
	      } catch (NotFoundException e) {
	    	  question = string.substring(string.indexOf(".") + 1);
	    	  Log.i(TAG, "Using " + question);
	      }
	      return question;
	}


}
