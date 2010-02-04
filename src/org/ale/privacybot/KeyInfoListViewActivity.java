package org.ale.privacybot;

import java.util.ArrayList;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class KeyInfoListViewActivity extends ListActivity{
	
	protected static final int CONTEXTMENU_DELETEITEM = 0; 
   
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
        
        registerForContextMenu(getListView());
        
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
        	  m_keys = GPG.getSecKeyList();
        	  m_keys.addAll(GPG.getPubKeyList());
              //wtf why is this here
              Thread.sleep(2000);
            } catch (Exception e) {
            }
            runOnUiThread(returnRes);
        }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add(0, 0, 0, "Make default");
		menu.add(0, 1, 0,  "Delete");
}

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	try {
    	    info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
    	} catch (ClassCastException e) {
    	    System.out.println("Bad menuInfo");
    	    return false;
    	}
    	long id = getListAdapter().getItemId(info.position);

		switch (item.getItemId()) {
		case 0:
			System.out.println("You pressed Make Default");
			GPG.makeDefault(m_adapter.getItem((int) id), getBaseContext());
			return true;
		case 1:
			System.out.println("You pressed Delete");
			GPG.deleteKey(m_adapter.getItem((int) id), getBaseContext());
			m_adapter.remove(m_adapter.getItem((int) id));
			return true;
		default:
			return super.onContextItemSelected(item);
		}	
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
                
                //XXX: Visually differentiate between public and secret keys.
                KeyInfo o = items.get(position);
                if (o != null) {
                        TextView tt = (TextView) v.findViewById(R.id.toptext);
                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                        ImageView iv = (ImageView) v.findViewById(R.id.icon);
                        if (tt != null) {
                              tt.setText(o.getNameCommentEmail());                            }
                        if(bt != null){
                        	  if (!o.getPublic()){
                        		  bt.setText("Secret - " + o.getFingerprint());
                        		  iv.setImageResource(R.drawable.greenicon);
                        	  }
                        	  else{
                        		  bt.setText(o.getFingerprint());
                        	  }
                        }
                }
                return v;
        }
	}
}