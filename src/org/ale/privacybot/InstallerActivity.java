package org.ale.privacybot;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class InstallerActivity extends Activity {
	final Handler mHandler = new Handler();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //no title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.installer);
		
    }
    
    public void onStart() {
        super.onStart();
        installData();
        
    }
    
    public void installData(){
        final Runnable runInstall = new Runnable() {
        	
        	public void run(){
        		checkInstall();
        		
        		//Save in settings
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                final SharedPreferences.Editor editor = prefs.edit();
            	editor.putString("binary_data", "1");
            	editor.commit();
            	
            	TextView done = (TextView) findViewById(R.id.install_done);
            	done.setVisibility(0);
       	}
        };
        
    	mHandler.postDelayed(runInstall, 1000);
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
		
		System.out.println("Match is");
		System.out.println(match);
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
	
	public void newFile(String destname)
	{
		File newasset = new File(destname);
		try {
			newasset.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to create " + destname);
			e.printStackTrace();
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
			//newFile("/data/data/org.ale.privacybot/.gpg/pubring.gpg");
			//doCommand("/system/bin/chmod", "777", "/data/data/org.ale.privacybot/.gpg/pubring.gpg");
			//newFile("/data/data/org.ale.privacybot/.gpg/secring.gpg");
			//doCommand("/system/bin/chmod", "777", "/data/data/org.ale.privacybot/.gpg/secring.gpg");
			//newFile("/data/data/org.ale.privacybot/.gpg/random_seed");
			//doCommand("/system/bin/chmod", "777", "/data/data/org.ale.privacybot/.gpg/random_seed");
			//newFile("/data/data/org.ale.privacybot/.gpg/trustdb.gpg");
			//doCommand("/system/bin/chmod", "777", "/data/data/org.ale.privacybot/.gpg/random_seed");
			copyAsset("version.txt");
		}
	}
}