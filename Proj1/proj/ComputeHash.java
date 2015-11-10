import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Author: Robin Li
 * ComputeHash.java:
 * For each ComputeHash object, when thread is called to run,
 * it calculates the hash for the potential password
 */
public class ComputeHash implements Runnable {
	private String password;
	private String hash;
	
	public ComputeHash(String password){
		this.password = password;
	}
	public String getPassword(){
		return this.password;
	}
	
	public String getHash(){
		return this.hash;
	}
	
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
 	// helper function for sha256
 	public static String bytesToHex(byte[] bytes){
 		StringBuffer sb = new StringBuffer();
 		for(byte byt : bytes) sb.append(Integer.toString((byt&0xff)+0x100,16).substring(1));
 		return sb.toString();
 	}
 	
 	// calculate hash for potential password
 	public void run(){
 		try {
			this.hash = sha256(this.password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
 	}
}