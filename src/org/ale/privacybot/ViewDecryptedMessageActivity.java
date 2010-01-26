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
import android.widget.EditText;
import android.widget.TextView;

public class ViewDecryptedMessageActivity extends Activity {
	Uri uri;
	String password = "";
	boolean pe = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //no title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.view_decrypted_message);
        
        Intent mother = getIntent();
        uri = mother.getData();
        
    }

    @Override
    public void onStart() {
        super.onStart();
        //Get password then does the displaying
        //May need threading
        getPassword();
        
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
        	
        	String dec = GPG.decryptMessage(uri, password);
        	TextView tv = (TextView) findViewById(R.id.decrypted);
        	tv.setText(dec);
    	  }
    	});

    	alert.show();
        	
    }

    }