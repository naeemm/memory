package fr.internet.memory.ean;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/*
 * The output is a json structure comprised of 
 * Three Elements in Result structure
 * 
 * 1. Name of the Product :
 * 2. EAN code:
 * 3. Description
 * 
 * We have treated description as an array of all 
 * of the other elements.
 */
	
//////////////////////////////////////////////////
public class Result {

	private static ProgramSetting Conf = new ProgramSetting();
	private static String outputDir  = (String)Conf.prop.getProperty("outputDir");		
	private JSONObject jObj = new JSONObject();
	private boolean jobStatus;
	private String foodName;
	
	///////////////////////////////////////////////////////
	/*
	 * Constructor: decide which routine is to be called
	 */
	Result (String input, int ServiceNumber, String foodName) {
		switch (ServiceNumber)
		{
		case 1:
			FormulateStructure1 (input, foodName);			
			break;
		case 2: // introduce new routine for each new service
			FormulateStructure2 (input, foodName);			
			break;
		}
	}
	
	//////////////////////////////////////////////////////
	/*
	 * It will design output according to input
	 * from the service.1
	 */
	@SuppressWarnings("unchecked")
	private void FormulateStructure1 (String input, String foodName) {
		
		String[] arrRes = input.split(SC.NL);		
		String[] arrHeader = arrRes[0].split(SC.TB);
		String[] arrData;

//		System.out.println(SC.NL + input + 
//				SC.NL + input.length() + SC.NL + arrRes.length   );
		
		
		setJobStatus(arrRes.length > 1); // 2+ means header + data
		setFoodName(foodName);
				
		for (int j = 1; j<arrRes.length; j++ ) {
			jObj.clear();
			arrData = arrRes[j].split(SC.TB);
			
			jObj.put(arrHeader[0], arrData[0] ); // EAN Code
			jObj.put(arrHeader[5], arrData[5] ); // Name

			JSONArray desc = new JSONArray();

			// we will require to adjust iterator (6)
			for (int i=6; i<arrData.length; i++) {
				desc.add(arrHeader[i] + ":" + arrData[i]);
			}

			jObj.put("Product Description", desc);
			
			// writing
			try (FileWriter file = new FileWriter( outputDir + arrData[0])) {
				file.write(jObj.toJSONString());
		//		System.out.println("\nJSON Object: " + jObj);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		
		}
	}
	
	/////////////////////////////////////////////////////
	private void FormulateStructure2 (String input, String foodName) {
		
//		String[] arrRes = input.split(SC.NL);		
//		String[] arrHeader = arrRes[0].split(SC.TB);
//		String[] arrData;
		
		Random random = new Random();
		Integer number = random.nextInt(10);
        setJobStatus(number > 6); 
        setFoodName(foodName);
				
	}
	
	/////////////////////////////////////////////////////
	private void setJobStatus(Boolean jobStatus) {
		this.jobStatus = jobStatus;
	}

	/////////////////////////////////////////////////////
	public Boolean getJobStatus() {
		return this.jobStatus;
	}

	/////////////////////////////////////////////////////
	private void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	/////////////////////////////////////////////////////
	public String getFoodName() {
		return this.foodName;
	}

	
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////

}

///////////////////////////////////////////////////////
