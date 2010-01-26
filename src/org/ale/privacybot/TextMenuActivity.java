package org.ale.privacybot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class TextMenuActivity extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //no title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.text_menu);

    }

    @Override
    public void onStart() {
        super.onStart();
        
    	Button b1 = (Button)findViewById(R.id.send_text);
    	Button b2 = (Button)findViewById(R.id.send_my_key);
    	Button b3 = (Button)findViewById(R.id.send_pbt);
    	
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(TextMenuActivity.this, ChooseRecepientActivity.class);
            	i.putExtra("isText", true);
            	startActivity(i);
            }
        });
        
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(TextMenuActivity.this, SendKeyOrPbotActivity.class);
            	i.putExtra("isText", true);
            	i.putExtra("sendKey", true);
            	startActivity(i);
            }
        });
    	
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(TextMenuActivity.this, SendKeyOrPbotActivity.class);
            	i.putExtra("isText", true);
            	i.putExtra("sendKey", false);
            	startActivity(i);
            }
        });
        
    }
    
}