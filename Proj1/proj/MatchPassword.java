import java.util.LinkedList;
import java.util.concurrent.Semaphore;
/*
 * Author: Robin Li
 * MatchPassword.java: 
 * The order in which MatchPassword objects are created follow the order
 * of reading in the database file. Each object holds the user name, password hash,
 * knows what thread was completed before. Every object goes through the dictionary
 * list. The boolean checkList becomes true when the thread finds a match and prints.
 * The semaphore lock is used by the first MatchPassword thread, then sequentially
 * passed on to the next thread in the order of how the database is read.
 */
public class MatchPassword implements Runnable {
	private String username;
	private String pwHash;
	private MatchPassword previousThread;
	private LinkedList<ComputeHash> dictionaryList;
	private boolean checkList = false;
	private final Semaphore lock = new Semaphore(1, true);
	
	public MatchPassword(String username, String pwHash,
			MatchPassword previousThread, LinkedList<ComputeHash> dictionaryList){
		this.username = username;
		this.pwHash = pwHash;
		this.previousThread = previousThread;
		this.dictionaryList = dictionaryList;
	}
	
	public String getUserName(){
		return this.username;
	}
	
	public String getPwHash(){
		return this.pwHash;
	}
	
	public synchronized boolean getCheckList(){
		return this.checkList;
	}
	
	public synchronized void setCheckList(){
		this.checkList = true;
	}
	
	public void run(){
		// first thread doesn't have previous; so null
		if(this.previousThread == null){
			try{
				lock.acquire();
			} catch(InterruptedException e){
				System.err.println("MatchPassword.java: Unable to acquire lock");
			}
		} else{ // every other thread knows the previous
			while (this.previousThread.getCheckList() == false){
				Thread.yield();
			}
			try{
				lock.acquire();
			} catch(InterruptedException e){
				System.err.println("MatchPassword.java: Unable to acquire lock");
			}
			
		}
		int i = 0;
		boolean found = false;
		// loop until reach each of dictionary list or when thread finds match
		while(i < this.dictionaryList.size() && found == false){
			// when a hash gets computed break out of loop
			while(this.dictionaryList.get(i).getHash() == null){
				Thread.yield();
			}

			// when this thread has the same hash as the hash in the dictionary list
			if(this.dictionaryList.get(i).getHash().equals(this.pwHash)){
				synchronized (System.out){
				    System.out.println(this.username + " " + this.dictionaryList.get(i).getPassword());
				}
				found = true; // to leave while loop
			}
			i++;
		}
		setCheckList(); // set thread to true and it won't run
		lock.release(); // release lock so the next thread can run
	}
}