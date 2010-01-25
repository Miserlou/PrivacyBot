package org.ale.privacybot;

public class KeyInfo {
	
    private String bytes;
    private String fprint;
    private String date;
    private String namecommentemail;
    private boolean pub = true;
	
	public KeyInfo(String b, String f, String d, String n, boolean p){
		bytes=b;
		fprint=f;
		date=d;
		namecommentemail=n;
		pub=p;
	}
	
	public String getBytes(){
		return bytes;
	}
	
	public String getFingerprint(){
		return fprint;
	}
	
	public String getDate(){
		return date;
	}
	
	public String getNameCommentEmail(){
		return namecommentemail;
	}
	
	public boolean getPublic(){
		return pub;
	}
   
}