package net.ralphpina.revappralphpina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmationActivity extends Activity {
	
	private TextView questionSelected1;
	private TextView answer1;
	private TextView questionSelected2;
	private TextView answer2;
	
	private Button restartButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmation);
		
		questionSelected1 = (TextView) findViewById(R.id.textViewQuestion1);
		answer1 = (TextView) findViewById(R.id.textViewAnswer1);
		questionSelected2 = (TextView) findViewById(R.id.textViewQuestion2);
		answer2 = (TextView) findViewById(R.id.textViewAnswer2);
		
		Intent qandaIntent = getIntent();
		questionSelected1.setText(questionSelected1.getText() + " " + qandaIntent.getStringExtra("security_question_1"));
		answer1.setText(answer1.getText() + " " + qandaIntent.getStringExtra("security_answer_1"));
		questionSelected2.setText(questionSelected2.getText() + " " + qandaIntent.getStringExtra("security_question_2"));
		answer2.setText(answer2.getText() + " " + qandaIntent.getStringExtra("security_answer_2"));
				
		restartButton = (Button) findViewById(R.id.buttonRestart);
		restartButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent restartProcess = new Intent(v.getContext(), MainActivity.class);
				startActivity(restartProcess);
			}
		});
		
	}

}
