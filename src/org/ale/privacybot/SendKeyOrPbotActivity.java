package org.ale.privacybot;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

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
        
        if(sendKey){
        		boolean exists = (new File("/data/data/org.ale.privacybot/my_pub.key")).exists();
        		if (exists){
        			objPath = Uri.parse("file://"+"/data/data/org.ale.privacybot/my_pub.key");
        		}
        		else {
        			GPG.exportPublicKey();
        			objPath = Uri.parse("file://"+"/data/data/org.ale.privacybot/my_pub.key");
        		}
        		attachedString = getString(R.string.pubkey_attached);
        }
        else{
        	objPath = Uri.parse("file://"+"/data/app/org.ale.privacybot.apk");
        	attachedString = getString(R.string.privacybot_attached);
        }
        
        if(isTxt){
        	sendTxt(objPath, attachedString);
        }
        else{
        	sendEmail(objPath, attachedString);
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
        intent.setType("*/*");
        startActivity(Intent.createChooser(intent, getString(R.string.choose_msg_app))); 
    }
    
    public void sendEmail(Uri u, String as){
		System.out.println("Sending Email");
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, as);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, as);
        sendIntent.putExtra(Intent.EXTRA_STREAM, u);
        sendIntent.setType("*/*");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.choose_email_app)));
        finish();
    }

    }