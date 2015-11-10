import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/*
 * Author: Robin Li
 * PasswordCrack.java:
 * Store each entry of dictionary file into linked list as a ComputeHash object
 * so that when the thread runs, the hash is computed for each potential password.
 * Store each user name and password hash of database file into linked list as
 * a MatchPassword object so that when all the threads run, it looks through the
 * linked list of hashed potential password for a match.
 */
public class PasswordCrack {

	public static void main(String args[]) throws InterruptedException{
		// check for 2 input parameters; if not then exit
		if(args.length != 2) {
    		System.err.println("Usage: java PasswordCrack <dictionary> <db>");
    		System.exit(1);
    	}

        // list of users with pw hash in order of database file
        LinkedList<MatchPassword> userPwHashList = new LinkedList<MatchPassword>();
        
        // dictionary potential passwords
        LinkedList<ComputeHash> dictionaryList = new LinkedList<ComputeHash>();
        
        // Read dictionary file, put potential passwords into list of strings
        try {
        	BufferedReader br = new BufferedReader(new FileReader(args[0]));
        	String line;
        	while((line = br.readLine()) != null){
        		dictionaryList.add(new ComputeHash(line));
        	}
        	br.close();
        } catch (FileNotFoundException e) {
            System.err.println("Unable to open file '" + args[0] +"'");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Unable to open file '" + args[0] +"'");
            System.exit(1);
        }
        
        // Read database file user name and pw hash
        // Pass null for the first thread; every other thread pass in the previous thread
        // to sequentially pass the lock through the list of users
        try {
            BufferedReader br = new BufferedReader(new FileReader(args[1]));
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] entry = line.split("\\s+");
                // when count = 0; pass the first thread with null
                if(count == 0){
                	MatchPassword current = new MatchPassword(entry[0], entry[1], null, dictionaryList);
                	userPwHashList.add(current);
                } else{ // for every other thread; pass the previous thread
                	MatchPassword current = new MatchPassword(entry[0], entry[1], userPwHashList.get(count - 1), dictionaryList);
                	userPwHashList.add(current);
                }
                count++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.err.println("Unable to open file '" + args[1] +"'");
        } catch (IOException e) {
            System.err.println("Error reading file '" + args[1] + "'");
        }

        // Run Group2 Threads
        ExecutorService group2 = Executors.newCachedThreadPool();
        for(MatchPassword curr : userPwHashList){
        	group2.execute(curr);
        }

        // Run Group1 Threads
        ExecutorService group1 = Executors.newCachedThreadPool();
        for(ComputeHash thread : dictionaryList){
        	group1.execute(thread);
        }

        // Shutdown Group1 and Group2 Threads
        group1.shutdown();
        group2.shutdown();
        group1.awaitTermination(60L, TimeUnit.SECONDS);
        group2.awaitTermination(60L, TimeUnit.SECONDS);
	}
}