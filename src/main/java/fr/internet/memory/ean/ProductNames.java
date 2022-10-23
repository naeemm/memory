package fr.internet.memory.ean;
/**
 * 
 * 1. load all of the product name
 * 2. remove all numeric words (any word with numeric value)
 * 3. without step 2, no product name was returning any result
 * 4. remove double white spaces
 * 5. all products are saved in list in a serialized format
 * 6. step 5 will increase speed on follow up start up of program  
 *  
*/

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.internet.memory.ean.ProgramSetting;

public class ProductNames
{
	ProgramSetting Conf = new ProgramSetting();
	private List<String> ProductNames;
	private String prodNameTextFilePath;
	private String prodNameBinFilePath;
	private Map <String, Integer> uniqueProd = new HashMap<String, Integer>();
    
	///////////////////////////////////////////////////////////////
	public boolean isprodName(String word)	{
		return ProductNames.contains(word.toLowerCase().trim());
	}
	
	//////////////////////////////////////////////////////////////
	public ProductNames() {
		this.ProductNames  = new ArrayList<String>();
		setprodNameBinFilePath();
		setprodNameTextFilePath();
		
		// bin file already exists.
		if (prodNameBinFileExists()) {
			setProductNames(Deserialize());
			
		// text file exists. 	
		} else if (prodNameTextFileExists()) {			
			BuildFromTextFile(); // load all product names
			Serialize();  // convert to binary for next time speedy load 
		
		// text file does not exist.
		} else {
				System.out.println("grocery text file not found." + SC.NL
						+ " fix 'config.properties' for its path");
		}
	}
	
	//////////////////////////////////////////////////////////////
	public List<String> getProductNames()	{
		return this.ProductNames;		
	}
	
	//////////////////////////////////////////////////////////////
	public void setProductNames(List<String> inp)	{
		this.ProductNames = inp;		
	}

	//////////////////////////////////////////////////////////////
	private void setprodNameTextFilePath()	{
		prodNameTextFilePath  = (String)
				Conf.prop.getProperty("prodNameTextFilePath");				
		// System.out.println(prodNameTextFilePath);
	}
	
	//////////////////////////////////////////////////////////////
	private boolean prodNameTextFileExists()	{		
		Path path = Paths.get(prodNameTextFilePath);
		return Files.exists(path, LinkOption.NOFOLLOW_LINKS);
	}

	//////////////////////////////////////////////////////////////
	private String getprodNameTextFilePath()	{		
		return this.prodNameTextFilePath;
	}

	//////////////////////////////////////////////////////////////
	private void setprodNameBinFilePath()	{
		prodNameBinFilePath  = (String)
				Conf.prop.getProperty("prodNameBinFilePath");		
	}
	
	//////////////////////////////////////////////////////////////
	private boolean prodNameBinFileExists()	{
		Path path = Paths.get(this.prodNameBinFilePath);
		return Files.exists(path, LinkOption.NOFOLLOW_LINKS);
	}

	//////////////////////////////////////////////////////////////
	private String getprodNameBinFilePath()	{
		return this.prodNameBinFilePath;	
	}
	
	//////////////////////////////////////////////////////////////

	public int length()	{
		return ProductNames.size();
	}	
	//////////////////////////////////////////////////////////////
		
	// @input: path of the dictionary file
	// @output: populate list object
	private void BuildFromTextFile() {		
		String  thisProduct = null;
		try {
		     // open input stream stop word file for populating array list.
		     BufferedReader br = new BufferedReader
		    		 (new FileReader(getprodNameTextFilePath()));
		     
		     while ((thisProduct = br.readLine()) != null) {
		    	thisProduct = Clean(thisProduct);
		    	if (!isDuplicate(thisProduct))
		    	  	ProductNames.add(thisProduct);
		     }
		     br.close();
		  }
		catch(Exception e){
		     e.printStackTrace();
		  }
		uniqueProd = null; // clear memory
	}
	
	//////////////////////////////////////////////////////////
	// Is it duplicate
	private Boolean isDuplicate(String input) {
		Boolean ret = false;
		if (uniqueProd.containsKey(input)) {
			return true;
		} else {
			uniqueProd.put(input, 0);
		}		
		return ret;
	}
	
	/////////////////////////////////////////////////////////////////////
	// Serialize the object in hard disk
	// to speed up the processing on next call of the program
	// useful for large text
	private int Serialize() {
		int ret = 1;
		try
        {
           FileOutputStream fos = new FileOutputStream( 
        		   					getprodNameBinFilePath());
           ObjectOutputStream oos = new ObjectOutputStream(fos);
           oos.writeObject(ProductNames);
           oos.close();
           fos.close();
         // System.out.printf("Serialized HashMap is saved at " + prop.getProperty("mapPath"));
        }catch(IOException ioe)
        {
           ioe.printStackTrace();
           ret = -1;
        }		
		return ret;
	}
	
	//////////////////////////////////////////////////////////////
	// pull out the serialized object 
	// optimized way of running program
	@SuppressWarnings("unchecked")
	private List<String> Deserialize() {
		
		List<String> tmpObj = new ArrayList<String>();
		try
	      {
	         FileInputStream fis = new FileInputStream( 
	        		 				getprodNameBinFilePath());
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         tmpObj = (ArrayList<String>) ois.readObject();
	         ois.close();
	         fis.close();
	      }catch(IOException ioe)
	      {
	         ioe.printStackTrace();
	         System.out.println("Kindly develop the map (one time only), "
	         		+ "then you can use this option on every start up of "
	         		+ "this program");
	         return null;
	      }catch(ClassNotFoundException c)
	      {	         
	         c.printStackTrace();
	         return null;
	      }
	    // System.out.println("Deserialized HashMap..");
		return tmpObj;	
	}
	
	//////////////////////////////////////////////////////////////	
	/*
	 * without cleaning the product name, API is not returning any result
	 */
	private String Clean(String input) {
		String ret = "";
		
		// remove words containing any numeric value
		String[] arr = input.replaceAll("\\w*\\d\\w* *", "").split(" +");
		
		for (int i=0; i<arr.length; i++) {
			ret += arr[i] + SC.SPACE;
		}
		
		// remove all special characters 
		ret = ret.replaceAll("[^a-zA-Z ]+","");
		
		// remove duplicate white spaces
		ret = ret.replaceAll("\\s+", " ");
		ret = ret.trim();

		return ret;
	}	


	//////////////////////////////////////////////////////////////	

}
