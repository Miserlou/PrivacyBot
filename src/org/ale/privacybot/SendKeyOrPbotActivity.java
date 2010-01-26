package org.ale.privacybot;

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
        
        if(sendKey){
        }
        else{
        	objPath = Uri.parse("file://"+"/data/app/org.ale.privacybot.apk");
        }
        
        if(isTxt){
        	sendTxt(objPath);
        }
        else{
        	sendEmail(objPath);
        }
        
        finish();
    }
    
    public void sendTxt(Uri u){
		System.out.println("Sending TXT");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra("sms_body", getString(R.string.privacybot_attached));
        intent.putExtra(Intent.EXTRA_STREAM, u);
        intent.putExtra(Intent.EXTRA_SUBJECT, R.string.private_data_attached);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.privacybot_attached));
        intent.setType("*/*");
        startActivity(Intent.createChooser(intent, getString(R.string.choose_msg_app))); 
    }
    
    public void sendEmail(Uri u){
		System.out.println("Sending Email");
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.privacybot_attached));
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.privacybot_attached));
        sendIntent.putExtra(Intent.EXTRA_STREAM, u);
        sendIntent.setType("*/*");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.choose_email_app)));
        finish();
    }

    }