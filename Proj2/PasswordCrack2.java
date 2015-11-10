import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;


/*
 * Author: Robin Li
 * PasswordCrack2.java:
 * Given a database file of username and password hashes, PasswordCrack2 is a parallel
 * program in Java that uses the Parallel Java 2 Library to carry out a brute force attack. 
 * Output:
 * List of users whose passwords were found, along with each such user's password. Each line
 * of output pertains to only one user. When finished, prints two final lines with the number
 * of users and the number of passwords found.
 */
public class PasswordCrack2 extends Task{
	public static int pwMaxLen = 4; // max length of a password
	public static int numChars = 36; // each pw char is a lowercase letter a thru z or digit 0 thru 9
	public static char[] chars = new char[numChars]; // holds possible pw chars
	public static LinkedHashMap<String, String> hm = new LinkedHashMap<String, String>(); // holds input db file
	public static int numUsers; // number of users in database
	public static int pwdsFound = 0; // how many passwords cracked/found
	
	/**
	 * Given a potential password, perform SHA-256 to get encryption
	 * @param String password
	 * @exception NoSuchAlgorithmException
	 *			  UnsupportedEncodingException
	 * @return String hash of input password
	 */
 	public static String sha256(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
 		MessageDigest md = MessageDigest.getInstance ("SHA-256");
 		byte[] data = password.getBytes ("UTF-8");
 		md.update (data);
 		data = md.digest();
 		return bytesToHex(data);
 	}
 	// helper function for sha256
 	public static String bytesToHex(byte[] bytes){
 		StringBuffer sb = new StringBuffer();
 		for(byte byt : bytes) sb.append(Integer.toString((byt&0xff)+0x100,16).substring(1));
 		return sb.toString();
 	}
 	
 	// make char array of lower case letters and digits
 	public static void buildCharArr(){
 		int i = 0;
 		for(char c = 'a'; c <= 'z'; c++, i++){
 			chars[i] = c;
 		}
 		for(char c = '0'; c <= '9'; c++, i++){
 			chars[i] = c;
 		}
 	}
 	
	/**
	 * Store users and pw hashs into hashmap (Key: user | Value: hash)
	 * @param String db - input file
	 * @return none
	 */
 	public static void readDBFile(String db){
 		try{
 			BufferedReader br = new BufferedReader(new FileReader(db));
            String line;
            while ((line = br.readLine()) != null) {
                String[] entry = line.split("\\s+");
                hm.put(entry[0], entry[1]);
            }
			numUsers = hm.size();
            br.close();
 		} catch (FileNotFoundException e) {
            System.err.println("Unable to open file '" + db +"'");
        } catch (IOException e) {
            System.err.println("Error reading file '" + db + "'");
        }
 	}

	/**
	 * Each thread uses findPwHash method to find the pwHash that matches one of the generated permutations of lengths 1 to pwMaxLen
	 * @param String username
	 *		  String pwHash
	 * @exception NoSuchAlgorithmException
	 *			  UnsupportedEncodingException
	 * @return none
	 */
	public static void findPwHash(String username, String pwHash) throws NoSuchAlgorithmException, UnsupportedEncodingException{
 		for(int a = 0; a < pwMaxLen; a++){ // loop to get lengths up to pwMaxLen
 			for(int b = 0; b < numChars; b++){ // generate pwds of length 1
 				if(a > 0){
 					for(int c = 0; c < numChars; c++){ // generate pwds of length 2
 						if(a > 1){
 							for(int d = 0; d < numChars; d++){ // generate pwds of length 3
 								if(a > 2){
 									for(int e = 0; e < numChars; e++){ // generate pwds of length 4
 	 	 								String s = "" + chars[b] + chars[c] + chars[d] + chars[e];
 	 	 								if(pwHash.equals(sha256(s)) ){ // hash the pw and check if match
											System.out.println(username + " " + s);
 	 	 			 						pwdsFound++;
 	 	 			 					}
 	 	 							}
 								} else {
 									String s = "" + chars[b] + chars[c] + chars[d];
 	 								if(pwHash.equals(sha256(s))){
										System.out.println(username + " " + s);
 	 			 						pwdsFound++;
 	 			 					}
 								}
 	 						}
 						} else {
 							String s = "" + chars[b] + chars[c];
 							if(pwHash.equals(sha256(s))){
								System.out.println(username + " " + s);
 		 						pwdsFound++;
 		 					}
 						}
 					}
 				} else{
 					String s = "" + chars[b];
 					if(pwHash.equals(sha256(s))){
						System.out.println(username + " " + s);
 						pwdsFound++;
 					}
 				}
			}
 		}
 	}
	
	/**
	 * Get the pw hash at the ith entry of hashmap
	 * @param int i
	 * @return String password hash
	 */
	public static String getHashByIndex(int i){
		int c = 0;
		for(Map.Entry<String, String> entry: hm.entrySet()){
			if(c == i) return entry.getValue();
			c++;
		}
		return "";
	}
	
	/**
	 * Get the user name at the ith entry of hashmap
	 * @param int i
	 * @return String username
	 */
	public static String getUserByIndex(int i){
		int c = 0;
		for(Map.Entry<String, String> entry: hm.entrySet()){
			if(c == i) return entry.getKey();
			c++;
		}
		return "";
	}
 	
	/**
	 * 
	 * @param String database text file
	 * @exception InterruptedException
	 *			  NoSuchAlgorithmException
	 *			  UnsupportedEncodingException
	 * @return none
	 */
	public void main(String args[]) throws InterruptedException, NoSuchAlgorithmException , UnsupportedEncodingException{
		final long startTime = System.currentTimeMillis();
		if(args.length != 1) { // if there isn't 1 input argument; then print error msg and exit
    		System.err.println("Usage: java pj2 PasswordCrack2 <databaseFile>");
    		System.exit(1);
    	}
		readDBFile(args[0]); // read in database file
		buildCharArr(); // make char array (what characters used to generate passwords)
		
		// parallel operation; produces the number of users in the db file of threads, each
		// thread is given the pwHash it needs to find by generating all possible pwds
		parallelFor(0, numUsers - 1) .exec (new Loop() {
			public void run (int i) {
				String username = getUserByIndex(i);
				String pwHash = getHashByIndex(i);
				try {
					findPwHash(username, pwHash);
				} catch (NoSuchAlgorithmException e) {
					System.err.println(e);
				} catch (UnsupportedEncodingException e){
					System.err.println(e);
				}
			}
		}); 
		System.out.println(numUsers + " users");
		System.out.println(pwdsFound + " passwords found");
 		final long endTime = System.currentTimeMillis();
 		System.out.println("Total execution time: " + (endTime - startTime) + " milliseconds");
	}
}
