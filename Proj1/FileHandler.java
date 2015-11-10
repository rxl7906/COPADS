import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * FileHandler.java:
 * - parse every line of input file
 */
public class FileHandler {
	List<String> readFile(String filename){
		List<String> records = new ArrayList<String>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			while((line = br.readLine()) != null){
				records.add(line);
			}
			br.close();
			return records;
		} catch(FileNotFoundException e){
			System.err.println("Unable to open file '" + filename +"'");
			e.printStackTrace();
			System.exit(1);
		    return null;
		} catch(IOException e){
			System.err.println("Error reading file '" + filename + "'");
			e.printStackTrace();
			System.exit(1);
		    return null;
		}
	}
}
