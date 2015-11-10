/*
 * Author: Robin Li
 * CSCI-251: - Project 1
 */
import java.util.List;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;

public class PasswordCrack {
	
	// given a potential password, perform SHA-256 to get encryption
	public static String sha256(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest md = MessageDigest.getInstance ("SHA-256");
		byte[] data = password.getBytes ("UTF-8");
		for(int i = 0; i < 100000; i++){
			md.update (data);
			data = md.digest();
		} 
		return bytesToHex(data);
	}
	
	public static String bytesToHex(byte[] bytes){
		StringBuffer sb = new StringBuffer();
		for(byte byt : bytes) sb.append(Integer.toString((byt&0xff)+0x100,16).substring(1));
		return sb.toString();
	}
	
	// debugging
	public static void printMap(LinkedHashMap<String,String> hm){
		for(Map.Entry<String, String> entry : hm.entrySet()){ 
			System.out.printf("Key: %s | Value: %s %n", entry.getKey(), entry.getValue());
		}
	}

	public static void main(String args[]) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		if(args.length != 2) usage(); // if cmd line doesn't have required # of args, print usage

		FileHandler db = new FileHandler();
		List<String> dbLines = db.readFile(args[1]);
		int maxUserLen = 0; 	// find the max length out of the user name
		LinkedHashMap<String, String> dbhm = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> dblhm = new LinkedHashMap<String, String>();
		for(String str: dbLines){
			String[] strArr = str.split("\\s+");
			dbhm.put(strArr[1], strArr[0]); // key: pw | value: userid
			dblhm.put(strArr[0], null);
			if(strArr[0].length() > maxUserLen){
				maxUserLen = strArr[0].length();
			}
		}
		//printMap(dbhm);
		//System.out.println("----------------------------------------");
		
		// store dict into array
		FileHandler dict = new FileHandler();
		List<String> dictLines = dict.readFile(args[0]);
		for(String str: dictLines){
			String pwd = sha256(str);
			if(dbhm.containsKey(pwd)){
				String userID = dbhm.get(pwd); // get the user id
				dblhm.put(userID, str); // replacing
			}
		}

		for(String key : dblhm.keySet()){
			if(dblhm.get(key) != null){
				System.out.print(key);
				int whiteSpaces = maxUserLen - key.length();
				while(whiteSpaces != 0){
					System.out.print(" ");
					whiteSpaces--;
				}
				System.out.println(" " + dblhm.get(key));
			} else continue;
		}
	}
	
	// print error output and exit
	private static void usage(){
		System.err.println("Usage: java PasswordCrack <dictionary> <db>");
		System.exit(1);
	}
}
