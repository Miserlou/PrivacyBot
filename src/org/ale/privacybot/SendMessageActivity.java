package org.ale.privacybot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class SendMessageActivity extends Activity {
	
	String password = "";
	boolean pe = false;
	boolean isTxt = false;
	String recip = "";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //no title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.enter_message);
        
        isTxt = getIntent().getExtras().getBoolean("isText");
        recip = getIntent().getExtras().getString("recipient");

    }

    @Override
    public void onStart() {
        super.onStart();
        
    	Button b1 = (Button)findViewById(R.id.send_button);
    	
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	getPassword();
            	if(!pe){
            		return;
            	}
            	
            	//startActivity(new Intent(EmailMenuActivity.this, KeyInfoListViewActivity.class));
            }
        });
    }
        
    private void getPassword(){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);

    	alert.setTitle(getString(R.string.unlock_key));
    	alert.setMessage(getString(R.string.enter_pass));

    	// Set an EditText view to get user input 
    	final EditText input = (EditText) findViewById(R.id.edittext_pass);
    	alert.setView(input);

    	alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton) {
    		String value = input.getText().toString();
    	  	password = value;
    	  	pe=true;
    	  }
    	});

    	alert.show();
        	
    }
    	

    
}