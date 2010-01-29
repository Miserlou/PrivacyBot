package org.ale.privacybot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.util.ArrayList;

import android.net.Uri;

// GPG execution functions

public class GPG{
	
	static String base = "/data/data/org.ale.privacybot/gpgx --homedir=/data/data/org.ale.privacybot/.gpg ";
	
	public static String execute(String s){
		
		Process p;
		try {
			p = Runtime.getRuntime().exec(s);
			//p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = er.readLine()) != null) {
				  sb.append(line).append("\n");
				}
			System.out.println("GPG Error:");
			System.out.println(sb.toString());
			sb = new StringBuffer();
			line = null;
			while ((line = br.readLine()) != null) {
			  sb.append(line).append("\n");
			}
			String answer = sb.toString();
			System.out.println("GPG Stdout:");
			System.out.println(answer);
			return answer;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static String importKey(String str){
		// XXX: Note - that allow nonselfsigned thing is a hack. Should be removed
		// when I can figure out why it thinks self signed keys aren't.
		String x = base + "--allow-secret-key-import --allow-non-selfsigned-uid --import " + str;
		return execute(x);
	}
	
	public static String exportPublicKey(){
		String x = base + "--output=/sdcard/my_pub.key --armor --export";
		return execute(x);
	}
	
	public static String encryptMessage(String recipiant, String message, String password){
		
		// XXX: Change to ExternalStorageDirectory
		File pb = new File("/sdcard/pb");
		File pbg = new File("/sdcard/pb.gpg");
		try { 
			System.out.println("writing to pb");
			BufferedWriter out = new BufferedWriter(new FileWriter("/sdcard/pb"));
			out.write(message);
			out.close(); } 
		catch (IOException e) {
			System.out.println("writing to pb error");
		} 

		String x = base + "-r " + recipiant + " --passphrase=" + password + " -e " + "/sdcard/pb";
		execute(x);
		pb.delete();
		return pbg.getAbsolutePath(); 
		
	}
	
	public static String decryptMessage(Uri uri, String password){
		String x = base + "-passphrase=" + password + " -d " + uri.getPath();
		return execute(x);
	}
	
	public static boolean checkPassword(String pASSphrase){
		
		// Is there a better way to check for this?
		String s = base + "--sign --dry-run --passphrase=" + pASSphrase;
		Process p;
		try {
			p = Runtime.getRuntime().exec(s);
			//p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = er.readLine()) != null) {
				  sb.append(line).append("\n");
				}
			System.out.println("GPG Error:");
			System.out.println(sb.toString());
			if(sb.toString().contains("bad passphrase")){
				return false;
			}
			sb = new StringBuffer();
			line = null;
			while ((line = br.readLine()) != null) {
			  sb.append(line).append("\n");
			}
			String answer = sb.toString();
			System.out.println("GPG Stdout:");
			System.out.println(answer);
			if(sb.toString().contains("bad passphrase")){
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static ArrayList<KeyInfo> getSecKeyList(){
		
		String[] secar;
		String bytes;
		String fprint;
		String date;
		String namecommentemail;
		boolean pub=false;
		
		ArrayList <KeyInfo> al = new ArrayList<KeyInfo>();
		String x = base + "--list-secret-keys --with-colons";
		String sec = execute(x);
		
		String[] keys = sec.split("\n");
		
		for(int i=0; i<keys.length; i++){
			secar = keys[i].split(":");
			if(secar[0].contains("pub") || secar[0].contains("sec")){
				bytes = secar[2];
				fprint = secar[4];
				date = secar[5];
				namecommentemail=secar[9];
				al.add(new KeyInfo(bytes,fprint,date,namecommentemail,pub));
			}
		}
		return al;
	}
	
	public static ArrayList<KeyInfo> getPubKeyList(){
		
		String[] secar;
		String bytes;
		String fprint;
		String date;
		String namecommentemail;
		boolean pub=true;
		
		ArrayList <KeyInfo> al = new ArrayList<KeyInfo>();
		String x = base + "--list-keys --with-colons";
		String sec = execute(x);
		
		String[] keys = sec.split("\n");
		
		for(int i=0; i<keys.length; i++){
			secar = keys[i].split(":");
			if(secar[0].contains("pub") || secar[0].contains("sec")){
				bytes = secar[2];
				fprint = secar[4];
				date = secar[5];
				namecommentemail=secar[9];
				al.add(new KeyInfo(bytes,fprint,date,namecommentemail,pub));
			}
		}
		return al;
	}
	
}



