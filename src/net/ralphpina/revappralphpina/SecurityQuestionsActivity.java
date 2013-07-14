package net.ralphpina.revappralphpina;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SecurityQuestionsActivity extends Activity {
	
	//private RevUtils revUtils;
	private Spinner questionSpinner1;
	private Spinner questionSpinner2;
	private EditText editTextAnswer1;
	private EditText editTextAnswer2;
	private Button buttonNext;
	
	private String question1;
	private String question2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_security_questions);
		
		//revUtils = RevUtils.getInstance();
		//revUtils.getAvailableSecurityQuestions();
		
		questionSpinner1 = (Spinner) findViewById(R.id.spinnerSecurityQuestion1);
		questionSpinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        question1 = (String) parent.getItemAtPosition(pos);
		    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// do nothing
			}
		});
		
		questionSpinner2 = (Spinner) findViewById(R.id.spinnerSecurityQuestion2);
		questionSpinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        question2 = (String) parent.getItemAtPosition(pos);
		    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// do nothing
			}
		});

		editTextAnswer1 = (EditText) findViewById(R.id.editTextSecurityQuestion1);
		editTextAnswer2 = (EditText) findViewById(R.id.editTextSecurityQuestion2);
		buttonNext = (Button) findViewById(R.id.buttonNext);
		buttonNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (validateAndShowError()) {
					Intent intentConfirmation = new Intent(v.getContext(), ConfirmationActivity.class);
					intentConfirmation.putExtra("security_question_1", question1);
					intentConfirmation.putExtra("security_answer_1", editTextAnswer1.getText().toString());	
					intentConfirmation.putExtra("security_question_2", question2);
					intentConfirmation.putExtra("security_answer_2", editTextAnswer2.getText().toString());	
					startActivity(intentConfirmation);
				}				
			}
		});
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.planets_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		questionSpinner1.setAdapter(adapter);
		questionSpinner2.setAdapter(adapter);
	
	}
	
	private boolean validateAndShowError() {
		StringBuilder message = new StringBuilder();
		boolean valid = true;
		
		if (question1 == null) {
			valid = false;
			message.append("Choose question #1 \n");
		} 
		if (!validate(editTextAnswer1)) {
			valid = false;
			message.append("Provide answer #1 \n");
		}
		if (question2 == null) {
			valid = false;
			message.append("Choose question #2 \n");
		} 
		if (!validate(editTextAnswer2)) {
			valid = false;
			message.append("Provide answer #2");
		}
		
		if (!valid) {
			showErrorDialog(message.toString());
		}
		
		return valid;
	}
	
	private boolean validate(EditText field) {
		String answer = null;
		
		try {
			answer = editTextAnswer1.getText().toString();
		} catch (NullPointerException e) {
			return false;
		}
		
		if (answer == null) {
			return false;
		}
		
		if (answer.trim().length() == 0) {
			return false;
		}
		
		return true;
	}
	
	private void showErrorDialog(String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Validation Error!");
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setCancelable(true);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
