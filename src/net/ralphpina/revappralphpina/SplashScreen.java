package net.ralphpina.revappralphpina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class SplashScreen extends Activity {
	
	private ImageView icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		icon = (ImageView) findViewById(R.id.imageIconSplash);
		
		Animation glowAnimation = 
				AnimationUtils.loadAnimation(
						SplashScreen.this,
						R.anim.icon_loading
						);
		icon.startAnimation(glowAnimation);
		
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
