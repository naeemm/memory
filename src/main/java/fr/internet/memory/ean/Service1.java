package fr.internet.memory.ean;

import java.util.concurrent.Callable;
import java.io.IOException;
import org.jsoup.Jsoup;

//webagent is the abstract / signature interface for every service 
public class Service1 extends WebService implements WebAgent, Callable<Result> {
	 
	ProgramSetting Conf = new ProgramSetting();
	///////////////////////////////////////////////////////////
    
    public Service1(String foodName) {
		super.setFoodName(foodName);
		super.setServiceName("OpenFoodFacts");
		super.setServiceNumber(1);
        this.urlSite = (String)	Conf.prop.getProperty("Service1");
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
	public Result Search() {
	
		Result result = null;
		String urlS = "https://ssl-api.openfoodfacts.org/cgi/search.pl?action=process&search_terms=";
		String urlE = "&sort_by=unique_scans_n&page_size=20&download=on";
		String url = getFoodName().trim().replaceAll(SC.SPACE, SC.Plus);
		url = urlS + url + urlE;
	        
        try {
	
			byte[] bytes = Jsoup.connect(url)
					   .header("Accept-Encoding", "gzip, deflate")
					   .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					   .ignoreContentType(true)
					   .maxBodySize(0)
					   .timeout(600000)
					   .execute()
					   .bodyAsBytes();
			
			String data = new String(bytes);

			result = new Result(data, getServiceNumber(), getFoodName());
	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;	
	 }
}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
