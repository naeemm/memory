package fr.internet.memory.ean;

import java.util.concurrent.Callable;

/*
 * Class is a supposed service
 * We are utilizing openfoodfacts url but returning a dummy result
 * The idea is to 
 * 1. replace the url of service2 in config.properties.
 * 2. recode the logic in search() using the given API
 * 3. most of the routines will not require to be changed significantly 
 * in case of adopting it for a new service    
 */


// webagent is the abstract / signature interface for every service 
public class Service2 extends WebService implements WebAgent, Callable<Result> {
	 
	ProgramSetting Conf = new ProgramSetting();
    
    //////////////////////////////////////////
    
    public Service2(String foodName) {
		super.setFoodName(foodName);
		super.setServiceName("Service2");
		super.setServiceNumber(2);
        this.urlSite = (String)	Conf.prop.getProperty("Service2");
	}

	///////////////////////////////////////////////////////////
    /**
     * this is invoked when executor.submit(..) is issued
     *
    */    
    @Override
    public Result call() throws Exception {
        Result result = null;
        super.CheckServiceAvailability();

        if (this.Available){ // proceeds only if service is alive
          result = Search();  
        } else {
    	  System.out.println(super.getStatus());
    	  System.out.println("If the problem is security related for https");
    	  System.out.println("Follow the instruction given in readme");
        }
        return  result;
    }
    	

	///////////////////////////////////////////////////////////////
	/*
	 * While utilizing a new API, the outcome of the result 
	 * must be consistent in line with res.
	 * The 'res' contains the header information
	 * and its corresponding cell values.
	 * A consistent (yet very simple) structure of the result and res 
	 * will ensure to easily incorporate it in the program
	 */
	public Result Search() {
	
		// Dummy result..... 
		Result result = null;
		
		String res = "code" + SC.TB + "url" + SC.TB + "creator	created_t" + SC.TB ;
		res +=  "last_modified_t" + SC.TB + "product_name" + SC.TB;
		res += "generic_name" + SC.TB +	"quantity" + SC.TB + "packaging_tags";
		res += "brands" + SC.TB + "brands_tags" + SC.TB + "categories";
		res += "categories_tags	labels" + SC.TB +	"labels_tags";
		
		res += SC.NL;
		
		res += "1234567890123" + SC.TB + "http://www.second.api/" + SC.TB + "a" + SC.TB ;
		res += "mod" + SC.TB + "Not Real Name" + SC.TB;
		res += "b" + SC.TB +	"123" + SC.TB + "A34";
		res += "brands" + SC.TB + "brands_tags" + SC.TB + "Food";
		res += "b" + SC.TB + "L1";
		
		result = new Result(res, getServiceNumber(), getFoodName());
		
		return result;	
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}