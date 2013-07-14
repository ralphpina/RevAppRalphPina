package net.ralphpina.revappralphpina;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
			
			System.setProperty("http.keepAlive", "false");
			
			try {
				url = new URL(new URI("https://api-qa2.revworldwide.com/v1/securityQuestions/").toASCIIString());
				connection = (HttpURLConnection) url.openConnection();
				TrustModifier.relaxHostChecking(connection); // here's where the magic happens
				connection.setRequestMethod("GET");
				connection.setDoInput(true);
				connection.setUseCaches(false);	
				connection.setRequestProperty("Accept", "application/xml");

				//String response = readResponse(connection.getInputStream());
				
				Log.e(TAG, "response code = " + connection.getResponseCode());
				Log.e(TAG, "response message = " + connection.getResponseMessage());
				
				return new JSONObject("");
				
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

	/**
	 * From http://stackoverflow.com/questions/995514/https-connection-android
	 * Trust every server - dont check for any certificate
	 */
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
