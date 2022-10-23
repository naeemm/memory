package fr.internet.memory.ean;

/*
 * ==========================================================================
 * Class to load the configuration parameters                                | 
 * Important, if the program is called from Linux then change the slash sign |
 * according to the requirement of the operating system                      |
 *                                                                           | 
 *  for windows based operating system                                       |
 *  / works but if it did not work then replace / by \\                      | 
 *            in  config.properties file                                     |
 *                                                                           | 
 *  for linux based operating system                                         |
 *  following is required to be updated at config.properties file            | 
 *                  mapPath=data/dictMap.ser                                 |
 *                                                                           |
 * ==========================================================================                           
 */

/**
 * Author: Naeem
 *
 *
*/

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/////////////////////////////////////////////
public class ProgramSetting {

	public PropertiesConfiguration prop = null;
		
	// constructor will empower the public variable with 
	// all of the parameters to be used by the other classes
	public ProgramSetting() {
		InputStream input = null;
		try {
			prop = new PropertiesConfiguration("config.properties");
			
			//input = new FileInputStream("/config.properties");
			// load a properties file
			//prop.load(input);
			
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
	//////////////////////////////////////////////////////////////////////////////
	

}
