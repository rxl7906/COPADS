import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class Hash {
	
	// given a potential password, perform SHA-256 to get encryption
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
	public static void main(String args[]) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		System.out.println("tin4 =>" + sha256("tin4"));
		System.out.println("ric39 =>" + sha256("ric39"));
		System.out.println("1gdfas =>" +sha256("1gdfas"));
		System.out.println("r8l =>" +sha256("r8l"));
	}
}
