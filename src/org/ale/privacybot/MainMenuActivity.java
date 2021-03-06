package org.ale.privacybot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainMenuActivity extends Activity {
	
	SharedPreferences prefs;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //no title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.main);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String data = prefs.getString("binary_data", "0");
        
        System.out.println("binary data is " + data);

    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	
    	String data = prefs.getString("binary_data", "0");
    	System.out.println("binary data is " + data);
    	
        if(data == "0"){
        	System.out.println("Luanchin instarlre");
        	launchInstaller();
        }
        
    	Button b1 = (Button)findViewById(R.id.txt);
    	Button b2 = (Button)findViewById(R.id.email);
    	Button b3 = (Button)findViewById(R.id.keymanager);
    	
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent km = new Intent(MainMenuActivity.this, TextMenuActivity.class);
            	startActivity(km);
            }
        });
        
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent km = new Intent(MainMenuActivity.this, EmailMenuActivity.class);
            	startActivity(km);
            }
        });
    	
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent km = new Intent(MainMenuActivity.this, KeyManagementActivity.class);
            	startActivity(km);
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	
    	MenuItem mi = menu.add(0,0,0,"Shell");
    	mi.setIcon(android.R.drawable.ic_menu_manage);

    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case 0:
    			startActivity(new Intent(this, ShellActivity.class));
    			return(true);
    	}
    	return(super.onOptionsItemSelected(item));
    }

    public void launchInstaller(){
    	Intent install = new Intent(MainMenuActivity.this, InstallerActivity.class);
    	startActivity(install);
    }

}