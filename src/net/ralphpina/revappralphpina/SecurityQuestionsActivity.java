package net.ralphpina.revappralphpina;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
	private boolean resetSpinner1;
	private boolean resetSpinner2;
	private int posSpinner1;
	private int posSpinner2;
	
	private ArrayAdapter<String> adapter;
	private Context context;
	public ProgressDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_security_questions);
		context = this;
		progress = new ProgressDialog(this);
		progress.setTitle(R.string.loading);
		progress.setMessage(getString(R.string.wait_loading));
		populateView();
		revUtils = RevUtils.getInstance(this);
		populateData();		
	}


	@Override
	protected void onResume() {
		super.onResume();
		resetSpinner1 = true;
		resetSpinner2 = true;
		posSpinner1 = 0;
		posSpinner2 = 0;
		question1 = null;
		question2 = null;
		editTextAnswer1.setText("");
		editTextAnswer2.setText("");
		
		tryConnectingAndLoadingData();
	}
		
	private void tryConnectingAndLoadingData() {
		ConnectivityManager cm =
		        (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected;
		if (activeNetwork == null) {
			isConnected = false;
		} else {
			isConnected = activeNetwork.isConnectedOrConnecting();
		}
	
		if (isConnected) {
			revUtils.callServerforQuestions();
		} else {
			showNetworkErrorDialog();
		}
	}

	private void populateView() {
		questionSpinner1 = (Spinner) findViewById(R.id.spinnerSecurityQuestionNum1);
		questionSpinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				if (!resetSpinner1) {
					if (pos == 0) {
						showErrorDialog(getString(R.string.SecurityQuestion_selectQuestion));
						resetSpinner1 = true;
						questionSpinner1.setSelection(posSpinner1, false);
					} else if (pos == questionSpinner2.getSelectedItemPosition()) {
						showErrorDialog(getString(R.string.SecurityQuestion_Validation_sameQuestionTwice));
						resetSpinner1 = true;
						questionSpinner1.setSelection(posSpinner1, false);
					} else {
						question1 = (String) parent.getItemAtPosition(pos);
						posSpinner1 = pos;
					}
				} else {
					resetSpinner1 = false;
				}
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
				if (!resetSpinner2) {
					if (pos == 0) {
						showErrorDialog(getString(R.string.SecurityQuestion_selectQuestion));
						resetSpinner2 = true;
						questionSpinner2.setSelection(posSpinner2, false);
					} else if (pos == questionSpinner1.getSelectedItemPosition()) {
						showErrorDialog(getString(R.string.SecurityQuestion_Validation_sameQuestionTwice));
						resetSpinner2 = true;
						questionSpinner2.setSelection(posSpinner2, false); 
					} else {
						question2 = (String) parent.getItemAtPosition(pos);
						posSpinner2 = pos;
					}
				} else {
					resetSpinner2 = false;
				}
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
			message.append(getString(R.string.SecurityQuestion_selectQuestion) + " #1 \n");
		} 
		if (!validate(editTextAnswer1)) {
			valid = false;
			message.append(getString(R.string.SecurityQuestion_Validation_provideAnAnswer) + " #1 \n");
		}
		if (question2 == null) {
			valid = false;
			message.append(getString(R.string.SecurityQuestion_selectQuestion) + " #2 \n");
		} 
		if (!validate(editTextAnswer2)) {
			valid = false;
			message.append(getString(R.string.SecurityQuestion_Validation_provideAnAnswer) + " #2");
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
	
	private void showNetworkErrorDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(R.string.SecurityQuestion_Validation_error);
		alertDialogBuilder.setMessage(R.string.Error_networkError);
		alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.dismiss();
	        	   tryConnectingAndLoadingData();
	           }
	    });
		alertDialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.dismiss();
	               Intent goBack = new Intent(context, MainActivity.class);
	               startActivity(goBack);
	           }
	       });
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
