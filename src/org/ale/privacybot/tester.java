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
    
    public void doCommand(String command, String arg0, String arg1)
	{
		try
		{
				String fullCmd = command + " " + arg0 + " " + arg1;
				Process p = Runtime.getRuntime().exec(fullCmd);
				p.waitFor();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.getMessage());
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}

	public boolean compareAsset(String assetname)
	{
		boolean match = false;

		String destname = "/data/data/org.ale.privacybot/" + assetname;
		File newasset = new File(destname);
		try
		{
			BufferedInputStream out = new BufferedInputStream(new FileInputStream(newasset));
			BufferedInputStream in = new BufferedInputStream(this.getAssets().open(assetname));
			match = true;
			while(true)
			{
				int b = in.read();
				int c = out.read();
				if(b != c)
				{
					match = false;
					break;
				}
				if(b == -1)
				{
					break;
				}
			}
			out.close();
			in.close();
		}
		catch (IOException ex)
		{
			match = false;
		}
		return match;
	}
	
	public void copyAsset(String assetname)
	{
		String destname = "/data/data/org.ale.privacybot/" + assetname;
		File newasset = new File(destname);
		try
		{
			newasset.createNewFile();
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newasset));
			BufferedInputStream in = new BufferedInputStream(this.getAssets().open(assetname));
			int b;
			while((b = in.read()) != -1)
			{
				out.write(b);
			}
			out.flush();
			out.close();
			in.close();
		}
		catch (IOException ex)
		{
			System.out.println("Failed to copy file '" + assetname + "'.\n");
		}
	}

	public void copyFile(String srcname, String destname)
	{
		File newasset = new File(destname);
		File srcfile = new File(srcname);
		try
		{
			newasset.createNewFile();
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newasset));
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcfile));
			int b;
			while((b = in.read()) != -1)
			{
				out.write(b);
			}
			out.flush();
			out.close();
			in.close();
		}
		catch (IOException ex)
		{
			System.out.println("Failed to copy file '" + srcname + "' to '" + destname + "'.\n");
		}
	}
	
	public void checkInstall(){
		if(!compareAsset("version.txt")){
			// This is a fucking HACK
			// Files must be smaller than 1.2M to transfer to /data/data/
			// EXCEPT if they are FUCKIGN .mp3s
			// This took me a WHOLE FUCKING DAY of hacking to figure out a solution
			// GodfuckingDAMMIT
			// It also takes quite a long time to copy the file so users should be told to wait patiently
			doCommand("/system/bin/mkdir", "/data/data/org.ale.privacybot/.gpg", "");
			copyAsset("gpgx.mp3");
			doCommand("/system/bin/mv", "/data/data/org.ale.privacybot/gpgx.mp3", "/data/data/org.ale.privacybot/gpgx");
			doCommand("/system/bin/chmod", "777", "/data/data/org.ale.privacybot/gpgx");
			copyAsset("version.txt");
		}
	}
}