package fr.internet.memory.ean;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/*
 * LookupService is responsible to run various DB services Asynchronusly
 * It is using Future and multithread technology
 * 
 */


public class LookupService 
{
	private static final int MYTHREADS = 100;
	private ThreadPoolExecutor executor; 
	private static ProgramSetting Conf = new ProgramSetting();
	private List<Future<Result>> resultList = new ArrayList<Future<Result>>();

	//////////////////////////////////////////////////////////////
    LookupService () {
    	executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MYTHREADS);
    }

	//////////////////////////////////////////////////////////////
    /*
     * Synchronus execution of multi threads 
     * Extendable by adding a new service
     */
    public void FindEan(List<String> products) {
		Service1 service1  = null;
		Future<Result> result = null;
		Iterator<String> prd = products.iterator();
	
		while (prd.hasNext()) {
			service1  = new Service1(prd.next().toString());
			result = executor.submit(service1);
		    resultList.add(result);
		}
				
		System.out.println(resultList.size() + " jobs submitted to Service1 - "
				+ service1.getServiceName()  );
		products = FilterResults();
 		System.out.println("Service1 did not Found " +  resultList.size() + " jobs");
 		
		//--- Repeatable section for every new service ....
 		Service2 service2 = null;
 		System.out.println(products.size() + " jobs submitted to Service2");
 		prd = products.iterator();
		resultList.clear();
		
		while (prd.hasNext()) {
			service2  = new Service2(prd.next().toString());
	        result = executor.submit(service2);
		    resultList.add(result);		    
		}
 		products = FilterResults();
		System.out.println("Service2 did not Found " + resultList.size() + " jobs");
		//--- End of repeatable section for every new service ....
 		
		
	}

	//////////////////////////////////////////////////////////////
	/*
	 * Update the product list as we are successfull in jobs
	 */
	private List<String> FilterResults() {
		List<String> products = new ArrayList<String>();
		
		// remove results which were found by service
		resultList.removeIf(s ->  {
			try {
				return (s.get().getJobStatus() == true);
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}  );
		
		///----------------------------------------------
		// update product list by those names which 
		// current service failed in finding
		Iterator<Future<Result>> p = resultList.iterator();
		while (p.hasNext()) {
			try {
				products.add(p.next().get().getFoodName().toString());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return products;
	}
	
	
    //////////////////////////////////////////////////////////////
	/*
	 * Synchronus call of mulithreaded job, 
	 * partially vitiate full benefits of future + multithreading 
	 */
    public void FindEan(String input) {
      Service1 service1  = new Service1(input);
      Service2 service2 = new Service2(input);
      Future<Result> result = null;
      
      result = executor.submit(service1);
      if (isFound(result, service1)) {
    	  result = executor.submit(service1);
          return ;
      }
      
      result = executor.submit(service2);
      if (isFound(result, service2)) {
    	  result = executor.submit(service2);
          return ;
      }
    //  System.out.println("Proceed for 3rd service and so on");
          
      System.out.println("Sorry! no record found across any service");
            
    }

    /////////////////////////////////////////////////////////////
   
    /*
     * Check Whether records of data found ?
     * Each of new service will require a new 
     * overloaded function with change in type of 2nd parameter
     */
    		
    private Boolean isFound(Future<Result> input, Service1 worker) {
    	Boolean ret = false;
    	try {
			ret = input.get().getJobStatus() ;
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	System.out.println("Result Found @ " + worker.getServiceName() + " : " + ret);
    	
       return ret;
    }
    
	/////////////////////////////////////////////////////////////
	
    private Boolean isFound(Future<Result> input, Service2 worker) {
    	Boolean ret = false;
    	try {
			ret = input.get().getJobStatus() ;
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	System.out.println("Result Found @ " + worker.getServiceName() + " : " + ret);
    	
       return ret;
    }
    
	/////////////////////////////////////////////////////////////
	/*
	 * Display json files produced as a result of running services 
	 */
    
    public void Display () {
    	File folder = new File((String)Conf.prop.getProperty("outputDir"));
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
		  File file = listOfFiles[i];
		  //if (file.isFile() && file.getName().endsWith(Ext)) {
		  if (file.isFile()) {
			  Display (file.getAbsolutePath());	  
		  } 
		}
    }
    
    ///////////////////////////////////////////////
    /*
     * overloaded function to assist its public counterpart 
     */
    private void Display (String path) {
        JSONParser parser = new JSONParser();
    	try {
            Object obj = parser.parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) obj;
            System.out.println(jsonObject.toString() + SC.NL);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
    
	///////////////////////////////////////////////////////////
	public void ShutDownService() {
		//shut down the executor service now
		executor.shutdown();
	}
	///////////////////////////////////////////////////////////

	
} // end of class

///////////////////////////////////////////////////////////////////////





