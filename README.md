
Food EAN Lookup Finder
======================

The program is developed in Eclipse Neon (JavaSE 1.8). I have used maven so no need to add any jar library. Program input setting can be observed / altered in config.properties. There is no url or path hard coded except written in this java standard configuration file.  

# Security Instructions

The security instructions are related to only https web site. Without security measures, the java library can't open the secured connection. I have provided two means:

1. Disable Secured Socket Layer Certificate Check: The same has been implemented and adopted by means of a single routine. Currently this mode is active.

2. Second approach is selective approach. It permits only selected url/ssl. It has also been implemented but commented. We just need to call following two lines in main() routine of Start.java

System.setProperty("javax.net.ssl.trustStore", trustFile);
private static String trustFile  = (String)Conf.prop.getProperty("trustFile");		

However, it also required some system level actions as described below: 

Download the certificate file in firefox browser. On left side of (https://....), one can find a blue circle. This can be used to download the certificate file. Following online resource is useful for step by step guide visually.
 
http://superuser.com/questions/97201/how-to-save-a-remote-server-ssl-certificate-locally-as-a-file
 
 and then follow instruction provided at: 
 
 http://stackoverflow.com/questions/7744075/how-to-connect-via-https-using-jsoup

# Input file

We have been provided grocery.txt. It contains almost 7 thousand food entries. Most of the product names (entries) were mixed with seller name (auchan, carrefour etc.) and also the product size/capacity specification. In this format, the website returns zero results. Hence, the current format of the products forced us to make some pre processing. We sought following pre processing:

1. Numeric words (any word containing single numeric value) was eliminated.
2. Double white spaces were removed.
3. Result was saved in a serialized object, so next time we can load it immediately. This treatment is only useful if the input text file is very large.
4. In case, the serialized file is not found (first time). The system creates it for successive run. 
5. Sampling of the product list. It is useful for immediate testing. it is implemented in a single function available in Start.java. the fraction argument in this function is a divider to the size of the list.

# Output file

If any service finds the result for a given product name, the result is 1 to many records. The system saves all available information of each of the result in a separate file. The output file is a json format while giving the EAN to the file. One display function enables us to draw out the json text for all of the products.


# Functionality of the Program

We have designed the principle lookupservice. The start class calls the lookup service. This service calls 1 to many database services (service1, service2..) . The program (as instructed) utilizes the multithreaded and Future technology of java. There are two modes of this program

1. Synchronus: In this functionality, a single job is assigned to a service. If the product is not found, then the same job is assigned to other service and so on. This type of functionality does not provide the great benefits of multi threading but the only benefit is that we don't need to issue wait() as if the http connection is busy (which always raise resource deadlock or resource contention problem). This type of functionality ensures that there is no such scenario.

2. Asynchronus: In this approach, bulk number of assignments are submitted to first service. The service issues multiple number of threads and store its result in Future<T> structure which is coherent with the multithreading approach. Those products which are not found are submitted to second service and so on.      

# How to add a new service

We have provided one interface and one super class for any new service. Following steps are useful while adding a novel service.
1. Create a new java file like service1.java or service2.java
2. Append code in the FindEan()... two overloaded function in lookupservice.java
3. Append a new function like FormulateStructure2() in Result.java. While doing so, the structure of the class be maintained and followed up. There is no other change/addition required 


Author: M. Naeem 
naeem.lyon@gmail.com
==================== End =======================    
