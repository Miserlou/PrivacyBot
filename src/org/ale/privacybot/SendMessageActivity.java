package org.ale.privacybot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SendMessageActivity extends Activity {
	
	String password = "";
	boolean pe = false;
	boolean isTxt = false;
	String recip = "";
	String email = "";
	
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
        email = getIntent().getExtras().getString("email");

        // This is because the G1 and similar phones relaunch the activity when opened
        // which causes the password dialog to disappear
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String data = prefs.getString("getting_pass", "0");
        if(data=="1"){
        	getPassword();
        }
        
    }

    @Override
    public void onStart() {
        super.onStart();
        
    	Button b1 = (Button)findViewById(R.id.send_button);
    	
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	getPassword();
            	if(!pe){
            		System.out.println("No password entered");
            		return;
            	}
            	
            	TextView tv = (TextView) findViewById(R.id.edittext_msg);
            	String msg = tv.getText().toString();
            	String epath = GPG.encryptMessage(email, msg, password);
            	
            	if(isTxt){
            		System.out.println("Sending TXT");
	                Intent intent = new Intent(Intent.ACTION_SEND);
	                intent.putExtra("address", recip);
	                intent.putExtra("sms_body", getString(R.string.private_data_attached));
	                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+epath));
	                intent.setType("application/pgp-encrypted");
	                startActivity(intent); 
            }
            	else{
            		System.out.println("Sending Email");
	                Intent sendIntent = new Intent(Intent.ACTION_SEND);
	                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.private_data_attached));
	                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+epath));
	                sendIntent.setType("application/pgp-encrypted");
	                startActivity(sendIntent); 
            }
            }
        });
    }
        
    private void getPassword(){
    	
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final SharedPreferences.Editor editor = prefs.edit();
    	editor.putString("getting_pass", "1");
    	editor.commit();
    	
    	LayoutInflater li = LayoutInflater.from(this);
    	final View tEV = li.inflate(R.layout.password, null);
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);

    	alert.setTitle(getString(R.string.unlock_key));
    	alert.setMessage(getString(R.string.enter_pass));

    	// Set an EditText view to get user input 
    	final EditText input = (EditText) tEV.findViewById(R.id.edittext_pass);
    	alert.setView(tEV);

    	alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton) {
    		String value = input.getText().toString();
    	  	password = value;
    	  	pe=true;
        	editor.putString("getting_pass", "0");
        	editor.commit();
    	  }
    	});

    	alert.show();
        	
    }
    	

    
}