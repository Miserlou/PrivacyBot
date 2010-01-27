package org.ale.privacybot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SendKeyOrPbotActivity extends Activity {
	Boolean isTxt = false;
	Boolean sendKey = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //no title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.object_sender);
        
        isTxt = getIntent().getExtras().getBoolean("isText");
        sendKey = getIntent().getExtras().getBoolean("sendKey");
        
    }

    @Override
    public void onStart() {
        super.onStart();
        
        Uri objPath = Uri.parse("");
        String attachedString = "";
        String mime = "";
        
        if(sendKey){
        	mime = "application/pgp-keys";
        	
        		boolean exists = (new File("/sdcard/my_pub.key")).exists();
        		if (exists){
        			objPath = Uri.fromFile(new File("/sdcard/my_pub.key"));
        		}
        		else {
        			GPG.exportPublicKey();
        			objPath = Uri.fromFile(new File("/sdcard/my_pub.key"));
        		}
        		attachedString = getString(R.string.pubkey_attached);
        }
        else{
        	mime = "application/vnd.android.package-archive";
    		boolean exists = (new File("/sdcard/privacybot.apk")).exists();
    		if (exists){
    			objPath = Uri.fromFile(new File("/sdcard/privacybot.apk"));
    		}
    		else {
    			try{
    				appToSD();
    				objPath = Uri.fromFile(new File("/sdcard/privacybot.apk"));
    				}
    			catch(IOException e){
    				Toast.makeText(this, getString(R.string.unable_to_copy), Toast.LENGTH_LONG).show();
    				finish();
    			}
    			
    		}
        		attachedString = getString(R.string.privacybot_attached);
        }
        
        if(isTxt){
        	sendTxt(objPath, attachedString);
        }
        else{
        	sendEmail(objPath, attachedString, mime);
        }
        
        finish();
    }
    
    public void sendTxt(Uri u, String as){
		System.out.println("Sending TXT");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra("sms_body", as);
        intent.putExtra(Intent.EXTRA_STREAM, u);
        intent.putExtra(Intent.EXTRA_SUBJECT, as);
        intent.putExtra(Intent.EXTRA_TEXT, as);
        intent.setType("application/pgp-keys");
        startActivity(Intent.createChooser(intent, getString(R.string.choose_msg_app))); 
    }
    
    public void sendEmail(Uri u, String as, String mime){
		System.out.println("Sending Email");
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, as);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, as);
        sendIntent.putExtra(Intent.EXTRA_STREAM, u);
        sendIntent.setType(mime);
        startActivity(Intent.createChooser(sendIntent, getString(R.string.choose_email_app)));
        finish();
    }
    
    public void appToSD() throws IOException{
    	
    		File destFile = new File("/sdcard/privacybot.apk");
    		File sourceFile = new File("/data/app/org.ale.privacybot.apk");
    	
    		 if(!destFile.exists()) {
    		  destFile.createNewFile();
    		 }

    		 FileChannel source = null;
    		 FileChannel destination = null;
    		 try {
    		  source = new FileInputStream(sourceFile).getChannel();
    		  destination = new FileOutputStream(destFile).getChannel();
    		  destination.transferFrom(source, 0, source.size());
    		 }
    		 finally {
    		  if(source != null) {
    		   source.close();
    		  }
    		  if(destination != null) {
    		   destination.close();
    		  }
    		}

    }

    }