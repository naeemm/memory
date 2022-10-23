package fr.internet.memory.ean;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import fr.internet.memory.ean.ProductNames;


//////////////////////////////////////////////////////////////
public class Start 
{
	
	private static ProgramSetting Conf = new ProgramSetting();
	private static ProductNames productNames = new ProductNames();

    ///////////////////////////////////////////////////////////////////////////
    public static void main( String[] args )    {
    	
    	//----------------------------------------------
    	// see description of this function
    	try {
			disableSSLCertCheck();
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
    	
    	//---------------------------------------------
    	// All product names from grocery
    	List<String> products = productNames.getProductNames();
    	products = Sample (products, 50); 
    	
    	//----------------------------------------------
    	/*
    	 * LookupService is responsible to run various DB services asynchronusly
    	 */    	
    	LookupService lookup = new LookupService();

    	//-----------------------------------------------
    	// Synchronus call to multithreaded program
    	// submitted all product list to all available services
    	lookup.FindEan(products);
    	
    	//---------------------------------------------------------
    	/*
    	 * Synchronus call of mulithreaded job, 
    	 * partially vitiate full benefits of future + multithreading 
    	 */
    /*
    	for (int i=0; i< products.size(); i++) {          	
    		System.out.println("==============================");
    		System.out.println(products.get(i));	
    		lookup.FindEan(products.get(i));    		
    	}
    */	 
    	//------------------------------------------------------
   		System.out.println( "Found products have been saved in Json format @ " + 
			(String)Conf.prop.getProperty("outputDir"));
   		// display all of the json files
   		// lookup.Display();

   		//------------------------------------------------------
   		lookup.ShutDownService();
    }
    
    ///////////////////////////////////////////////////    
    /*
     * This routine helps us in disabling the https certificate problem
     * The bleak side is that the current application is blindly trusting
     * every other connection as well
     * 
     * A second solution is as below:
     * A dedicated and secured way require to place following 
     * two statement in main function
     * 
     * System.setProperty("javax.net.ssl.trustStore", trustFile);
     * private static String trustFile  = (String)Conf.prop.getProperty("trustFile");		
     * 
     * before running two lines, register ssl key ring file,
	 * after this java will come to know about its existence
	 * otherwise script will throw error for (secured) https API 
	 */
    
    private static void disableSSLCertCheck() throws NoSuchAlgorithmException, KeyManagementException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
    
    //////////////////////////////////////////////////
    /*
     * The grocery.txt file contains thousands of unique product names
     * better to take a sample from it for experimentation purpose
     */
    private static List<String>  Sample (List<String> products, int fraction) {
    	System.out.println("Total Product before sampling:  " + products.size());
    	List<String> ret = products.subList(0, products.size()/fraction);
    	System.out.println("Total Product after sampling:  " + ret.size());
    	return ret;
    }
    
}

	////////////////////////////////////////////////////////////