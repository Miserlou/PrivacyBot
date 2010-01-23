package org.ale.privacybot;

import org.openintents.filemanager.FileManagerActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class KeyManagement extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //no title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.key_management);

    }

    @Override
    public void onStart() {
        super.onStart();
        
    	Button b1 = (Button)findViewById(R.id.my_key);
    	Button b2 = (Button)findViewById(R.id.new_key);
    	Button b3 = (Button)findViewById(R.id.import_key);
    	
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	startActivity(new Intent(KeyManagement.this, FileManagerActivity.class));
            }
        });
        
    }
    
}