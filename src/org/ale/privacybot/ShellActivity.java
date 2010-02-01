package org.ale.privacybot;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ShellActivity extends Activity {
	Uri uri;
	String password = "";
	boolean pe = false;
	TextView tv;
	Button b;
	EditText et;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //no title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.shell);
        
        Intent mother = getIntent();
        uri = mother.getData();
        
    }

    @Override
    public void onStart() {
        super.onStart();
        tv = (TextView)findViewById(R.id.command_result);
        b = (Button)findViewById(R.id.button_execute);
        et = (EditText)findViewById(R.id.shell_input);
        
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	execCommand();
            }
            });
    }
    
    private void execCommand(){
    	String s = et.getText().toString();
    	String res = GPG.publicExecute(s);
    	tv.setText(res);
    }

    }