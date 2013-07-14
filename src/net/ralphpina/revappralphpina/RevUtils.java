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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class RevUtils {
	
	private static final String TAG = "RevUtils";
	
	private static RevUtils revUtils;
	private HttpURLConnection connection;
	private URL url;
	
	public static RevUtils getInstance() {
		if (revUtils == null) {
			revUtils = new RevUtils();
		}
			
		return revUtils;
	}
	
	private RevUtils() {
		new ApiTransaction().execute();
	}
	
	public ArrayList<String> getAvailableSecurityQuestions() {
		
		return new ArrayList<String>();
	}
	
	private class ApiTransaction extends AsyncTask<String, Integer, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			//accept no cookies
			CookieManager cookieManager = new CookieManager();
			cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_NONE);
			CookieHandler.setDefault(cookieManager);
			
			try {
				url = new URL(new URI("https://api-qa2.revworldwide.com/v1/securityQuestions").toASCIIString());
				connection = (HttpURLConnection) url.openConnection();
				TrustModifier.relaxHostChecking(connection); // here's where the magic happens
				connection.setDoInput(true);
				connection.setUseCaches(false);	
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Accept", "application/json");

				String response = readResponse(connection.getInputStream());
				
				Log.e(TAG, "response code = " + connection.getResponseCode());
				Log.e(TAG, "response message = " + connection.getResponseMessage());
				
				return new JSONObject(response);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
						
			return null;
		}
		
		protected void onPostExecute(JSONObject result) {
			Log.e(TAG, result.toString());
			
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
	
	// always verify the host - dont check for certificate
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

}
