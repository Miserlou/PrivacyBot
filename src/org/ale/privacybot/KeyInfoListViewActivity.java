package org.ale.privacybot;

import java.util.ArrayList;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class KeyInfoListViewActivity extends ListActivity{
   
    private ProgressDialog m_ProgressDialog = null;
    private ArrayList<KeyInfo> m_keys = null;
    private KeyInfoAdapter m_adapter;
    private Runnable viewKeys;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //no title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
  
        setContentView(R.layout.key_list);
        m_keys = new ArrayList<KeyInfo>();
        this.m_adapter = new KeyInfoAdapter(this, R.layout.row, m_keys);
                setListAdapter(this.m_adapter);
       
        viewKeys = new Runnable(){
            @Override
            public void run() {
                getKeys();
            }
        };
    Thread thread =  new Thread(null, viewKeys, "MagentoBackground");
        thread.start();
        //This stuff would be better to load from a DB
        m_ProgressDialog = ProgressDialog.show(KeyInfoListViewActivity.this,    
              getString(R.string.please_wait), getString(R.string.listing_keys), true);
    }
    
    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            if(m_keys != null && m_keys.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_keys.size();i++)
                m_adapter.add(m_keys.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };
    private void getKeys(){
          try{
        	  m_keys = GPG.getKeyList();
              //wtf why is this here
              Thread.sleep(2000);
            } catch (Exception e) {
            }
            runOnUiThread(returnRes);
        }


private class KeyInfoAdapter extends ArrayAdapter<KeyInfo> {

        private ArrayList<KeyInfo> items;

        public KeyInfoAdapter(Context context, int textViewResourceId, ArrayList<KeyInfo> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                }
                KeyInfo o = items.get(position);
                if (o != null) {
                        TextView tt = (TextView) v.findViewById(R.id.toptext);
                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                        if (tt != null) {
                              tt.setText(o.getNameCommentEmail());                            }
                        if(bt != null){
                              bt.setText(o.getFingerprint());
                        }
                }
                return v;
        }
	}
}