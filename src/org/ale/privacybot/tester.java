package org.ale.privacybot;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class tester extends Activity {
	
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
        final SharedPreferences.Editor editor = prefs.edit();
        String data = prefs.getString("binary_data", "0");
        
        // XXX: REMOVE FOR PRODUCTION
        System.out.println("binary_data is");
        System.out.println(data);
        data = "0";
        
        if(data != "1"){
        	launchInstaller();
        }
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	Button b1 = (Button)findViewById(R.id.txt);
    	Button b2 = (Button)findViewById(R.id.email);
    	Button b3 = (Button)findViewById(R.id.keymanager);
    	
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent km = new Intent(tester.this, KeyManagement.class);
            	startActivity(km);
            }
        });
    }

    public void launchInstaller(){
    	Intent install = new Intent(tester.this, Installer.class);
    	startActivity(install);
    }

}