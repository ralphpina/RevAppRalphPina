package net.ralphpina.revappralphpina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class MainActivity extends Activity {

	private Button nextButton;
	private RadioButton radioButtonEnglish;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		nextButton = (Button) findViewById(R.id.mainActivityNextButton);
		radioButtonEnglish = (RadioButton) findViewById(R.id.englishRadioButton);
		
		nextButton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent nextPage = new Intent(v.getContext(), SecurityQuestionsActivity.class);
				if (radioButtonEnglish.isChecked()) {
					nextPage.putExtra("language", "english");
				} else {
					nextPage.putExtra("language", "english");
				}
				startActivity(nextPage);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
	}
	
	

}
