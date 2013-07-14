package net.ralphpina.revappralphpina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				goToMainActivity();
			}
			
		}).start();	
	}
	
	private void goToMainActivity() {
		Intent mainActivity = new Intent(this, MainActivity.class);
		startActivity(mainActivity);
	}
}
