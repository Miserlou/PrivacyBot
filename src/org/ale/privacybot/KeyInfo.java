package org.ale.privacybot;

public class KeyInfo {
	
    private String bytes;
    private String fprint;
    private String date;
    private String namecommentemail;
	
	public KeyInfo(String b, String f, String d, String n){
		bytes=b;
		fprint=f;
		date=d;
		namecommentemail=n;
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
   
}