package fr.internet.memory.ean;

import java.net.HttpURLConnection;
import java.net.URL;

public class WebService {

	protected String urlSite;
	protected Boolean Available;
	protected String Status ;
	protected String name ;
	protected String foodName;
    protected int ServiceNumber;

	public Boolean CheckServiceAvailability() {
		int code = 200;
		
		try {
			URL siteURL = new URL(urlSite);
			HttpURLConnection connection = (HttpURLConnection) siteURL
					.openConnection();

			connection.setRequestMethod("GET");
			connection.connect();

			code = connection.getResponseCode();
			if (code == 200) {
				this.Available = true;
				setStatus("Ok");
			}
		} catch (Exception e) {
			setStatus(e.getMessage());
			this.Available = false;
		}
		//System.out.println("Available: " + getStatus() + "  " + this.Available);
		return this.Available;
	}

	public void setStatus(String status) {
    	this.Status = status;
    }

    public String getStatus() {
    	return this.Status;
    }
    
    public void setServiceName(String name) {
    	this.name = name;
    }

    public String getServiceName() {
    	return this.name;
    }
    

	////////////////////////////////////////////////////////	
	/*
	 * service number will identify calling object in result class
	 */
    public int getServiceNumber() {
		return this.ServiceNumber;
	}

	public void setServiceNumber(int ServiceNumber) {
		this.ServiceNumber = ServiceNumber;
	}

	////////////////////////////////////////////////////////////////
	/*
	 * given input to be searched across various services 
	*/	
	public String getFoodName() {
		return this.foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	///////////////////////////////////////////////////////////////

	
	
}
