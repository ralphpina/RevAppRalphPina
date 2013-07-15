package net.ralphpina.revappralphpina;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends Activity {

	private Button nextButton;
	private Spinner spinnerLanguage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		nextButton = (Button) findViewById(R.id.mainActivityNextButton);
		spinnerLanguage = (Spinner) findViewById(R.id.spinnerLanguageSelection);
		
		ArrayList<String> languages = new ArrayList<String>();
		languages.add(getString(R.string.Language_english));
		languages.add(getString(R.string.Language_spanish));
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerLanguage.setAdapter(adapter);
		
		spinnerLanguage.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				if (((String) parent.getItemAtPosition(pos)).equals(view.getContext().getString(R.string.Language_english))) {
					setLanguage("en");
				} else {
					setLanguage("es");
				}
		    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// do nothing
			}
		});
		
		nextButton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent nextPage = new Intent(v.getContext(), SecurityQuestionsActivity.class);
				nextPage.putExtra("language", (String) spinnerLanguage.getSelectedItem());
				startActivity(nextPage);
			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
	}
	
	private void setLanguage(String lang) {
	    Locale locale = new Locale(lang); 
	    Locale.setDefault(locale);
	    Configuration config = new Configuration();
	    config.locale = locale;
	    getBaseContext().getResources().updateConfiguration(config, 
	    getBaseContext().getResources().getDisplayMetrics());
	}

}
