package org.ale.privacybot;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class KeyImporterActivity extends Activity {
	final Handler mHandler = new Handler();
	Uri uri;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //no title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.key_importer);
        
        Intent mother = getIntent();
        uri = mother.getData();
		
    }
    
    public void onStart() {
        super.onStart();
        	
	        TextView tv = (TextView)findViewById(R.id.import_tv);
	        String txt = (String) tv.getText();
	        txt = txt + uri.getLastPathSegment();
	        tv.setText(txt);
	        importKey();
        	TextView done = (TextView) findViewById(R.id.install_done);
        	done.setVisibility(0);
	        
    }
    
    public void importKey(){
    	GPG.importKey(uri.getPath());
    }

}