package net.ralphpina.revappralphpina;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
	
	//private static final String TAG = "SecurityQuestionsActivity"; 
	
	private RevUtils revUtils;
	private Spinner questionSpinner1;
	private Spinner questionSpinner2;
	private EditText editTextAnswer1;
	private EditText editTextAnswer2;
	private Button buttonNext;
	
	private String question1;
	private String question2;
	private boolean firstTimeLoadingSpinner1;
	private boolean firstTimeLoadingSpinner2;
	private int posSpinner1;
	private int posSpinner2;
	
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_security_questions);
		populateView();
		revUtils = RevUtils.getInstance(this);
		populateData();		
	}


	@Override
	protected void onResume() {
		super.onResume();
		firstTimeLoadingSpinner1 = true;
		firstTimeLoadingSpinner2 = true;
		posSpinner1 = 0;
		posSpinner2 = 0;
		question1 = null;
		question2 = null;
		editTextAnswer1.setText("");
		editTextAnswer2.setText("");
		revUtils.callServerforQuestions();
	}

	private void populateView() {
		questionSpinner1 = (Spinner) findViewById(R.id.spinnerSecurityQuestionNum1);
		questionSpinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				if (pos == 0 && !firstTimeLoadingSpinner1) {
					showErrorDialog(getString(R.string.SecurityQuestion_selectQuestion));
					firstTimeLoadingSpinner1 = true;
					questionSpinner1.setSelection(posSpinner1, false);
				} else if (pos == questionSpinner2.getSelectedItemPosition() && !firstTimeLoadingSpinner1) {
					showErrorDialog(getString(R.string.SecurityQuestion_Validation_sameQuestionTwice));
					firstTimeLoadingSpinner1 = true;
					questionSpinner1.setSelection(posSpinner1, false);
				} else if (!firstTimeLoadingSpinner2){
					question1 = (String) parent.getItemAtPosition(pos);
					posSpinner1 = pos;
				}
				firstTimeLoadingSpinner1 = false;
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
				if (pos == 0 && !firstTimeLoadingSpinner2) {
					showErrorDialog(getString(R.string.SecurityQuestion_selectQuestion));
					firstTimeLoadingSpinner2 = true;
					questionSpinner2.setSelection(posSpinner2, false);
				} else if (pos == questionSpinner1.getSelectedItemPosition() && !firstTimeLoadingSpinner2) {
					showErrorDialog(getString(R.string.SecurityQuestion_Validation_sameQuestionTwice));
					firstTimeLoadingSpinner2 = true;
					questionSpinner2.setSelection(posSpinner2, false); 
				} else if (!firstTimeLoadingSpinner2) {
					question2 = (String) parent.getItemAtPosition(pos);
					posSpinner2 = pos;
				}
				firstTimeLoadingSpinner2 = false;
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
	}
	
	private void populateData() {
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, revUtils.getAvailableSecurityQuestions());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		questionSpinner1.setAdapter(adapter);
		questionSpinner2.setAdapter(adapter);
	}
	
	public void dataChanged() {
		adapter.notifyDataSetChanged();
		questionSpinner1.setSelection(0);
		questionSpinner2.setSelection(0);
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
			answer = field.getText().toString();
		} catch (NullPointerException e) {
			return false;
		}
		
		if (answer.trim().length() == 0) {
			return false;
		}
		
		return true;
	}
	
	private void showErrorDialog(String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(R.string.SecurityQuestion_Validation_error);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.dismiss();
	           }
	    });
		alertDialogBuilder.setCancelable(true);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
