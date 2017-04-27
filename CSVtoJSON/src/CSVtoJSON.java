import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.l33tindustries.tools.datastructure.CSVdata;

public class CSVtoJSON {

	private static Logger logger = Logger.getLogger(CSVtoJSON.class.getName());

	static final String usage = " USAGE:CSVtoJSON <Path to CSV File> <Delimiter>";

	private static String getCurrentMethodName() 
 	{ 
 		StackTraceElement stackTraceElements[] = (new Throwable()).getStackTrace(); 
 		
 		return 	stackTraceElements[1].toString().replaceFirst(stackTraceElements[1].toString().split("\\.")[0]+"\\.", "");
 	}
	
	public static void main(String[] args) {
		
		if(args.length != 2)
	    {
			System.out.println(usage);
	        System.exit(2);
	    }
		
		logger.trace(getCurrentMethodName() + " Entering application.");

		
		//USAGE: <file> <delimiter> <database | systemFileoutput> 
		
		final String CSVFile = args[0];
		
		final String Delmimiter = args[1];
		
		logger.trace(getCurrentMethodName() + " Delimiter: " + Delmimiter);
		
		//CREATE THE MASTER JSON OBJECT THAT HOLDS THE ARRAY OF DOCUMENTS
		logger.debug(getCurrentMethodName() + " Creating the Master JSON Document.");
		JsonObject jBulkDocsJSON = new JsonObject();
		
		//CREATE THE ARRAY OF JSON DOCS FOR BULK OPERATIONS
		logger.debug(getCurrentMethodName() + " Creating the Master Array that goes in the Master JSON Document.");
		JsonArray jBulkDocsArray = new JsonArray();
	    
		//EXAMPLE : POPULATE THE ARRAY WITH VALUES
		//JsonPrimitive element = new JsonPrimitive("Test");
		//jBulkDocsArray.add(element);
				
		int CSVLineCount = 0;
		
		logger.debug(getCurrentMethodName() + " About to read file: " + CSVFile);

		try(BufferedReader br = new BufferedReader(new FileReader(CSVFile))) {
			String header = "";
			
			for(String line; (line = br.readLine()) != null; ) {
				logger.debug(getCurrentMethodName() + " Line in file: " + CSVFile + ", Line: " + CSVLineCount);
				if (CSVLineCount == 0)
				{
					logger.debug(getCurrentMethodName() + " First Line of CSV File. Grabbing first line as the header " + CSVFile);
					header = line;
					logger.debug(getCurrentMethodName() + " Going to Next line. Header is : " + header);
				}
				else
				{
					logger.trace(getCurrentMethodName() + " Header Established " + header);
					logger.debug(getCurrentMethodName() + " Creating a JSON Object to place into Array. ");
					//JsonObject JSONdoc = new JsonObject();

					logger.debug(getCurrentMethodName() + " Processing Line: " + line);
		    	
					logger.debug(getCurrentMethodName() +" Using established header: " + header);
					 
					 // FOR REFERENCE:
					 // String regex = String.format("(?x) "+ // enable comments, ignore white spaces
				     //           ",                         "+ // match a comma
				     //           "(?=                       "+ // start positive look ahead
				     //           "  (                       "+ // start group 1
				     //           "    %s*                   "+ // match 'otherThanQuote' zero or more times
				     //           "    %s                    "+ // match 'quotedString'
				     //           "  )*                      "+ // end group 1 and repeat it zero or more times
				     //           "  %s*                     "+ // match 'otherThanQuote'
				     //           "  $                       "+ // match the end of the string
				     //           ")                         ", // stop positive look ahead
					
					//String[] headerTokens = header.split(Delmimiter + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
					//String[] dataTokens = line.split(Delmimiter + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
					
					
					//logger.debug(getCurrentMethodName() + " tokens: " + headerTokens.length + " data: " + dataTokens.length + " line: " + CSVLineCount);
					//int LineCSVCount = 0;
					
					
					/*
					for(String t : headerTokens) {
			            
			            logger.trace(getCurrentMethodName() +" Adding Element Index: " + LineCSVCount + " name: " + t + ", element: " + dataTokens[LineCSVCount]);
			            //JSONdoc.add(t,  new JsonPrimitive(dataTokens[LineCSVCount]));			           
			            
			            LineCSVCount++;
			        }*/
					//logger.debug(getCurrentMethodName() + " Adding : " + JSONdoc);
					//CSVdata csVdata = new CSVdata(header, line);

					
					jBulkDocsArray.add(new CSVdata(header, line).CSVLinetoJSON());

				}
				CSVLineCount++;
		    }
		    // line is not visible here.
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error(getCurrentMethodName() + " " + CSVFile + " not found.");
			System.out.println("ERROR : " + CSVFile + " not found. Exiting.");
			System.exit(1);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e)
		{
			logger.error(getCurrentMethodName() + " Header and Data are not equal. Skipping line " + CSVLineCount);
		}
		
		logger.debug(getCurrentMethodName() + "Adding everyting to the master bulk docs array ");
		jBulkDocsJSON.add("docs", jBulkDocsArray);

		System.out.println(jBulkDocsJSON);
		
		logger.trace(getCurrentMethodName() + " Exiting application.");
		
	}

}
