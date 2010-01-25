package org.ale.privacybot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
	
	public static ArrayList<KeyInfo> getKeyList(){
		// XXX: This needs to work for all keys, not just the first one
		
		ArrayList <KeyInfo> al = new ArrayList<KeyInfo>();
		String x = base + "--list-secret-keys --with-colons";
		String sec = execute(x);
		String[] secar = sec.split(":");
		String bytes = secar[1];
		String fprint = secar[4];
		String date = secar[5];
		String namecommentemail=secar[9];
		KeyInfo ki = new KeyInfo(bytes,fprint,date,namecommentemail);
		al.add(ki);
		
		return al;
	}
	
}

class Key{
	
	public String name;
	public String UID;
	
	public Key(){
		
	}
}

