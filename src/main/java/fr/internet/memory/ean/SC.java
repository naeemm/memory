package fr.internet.memory.ean;
/**
 * Author: Naeem
 */

import java.text.DecimalFormat;

public class SC {
	public static String DLMT = ","; 
	public static String Comma = ","; 
	public static String DOT = ".";
	public static String COLON = ":";
	public static String SQ = "'";
	public static String HY = "-";	
	public static String SPACE = " ";
	public static String QUOTE = "\"";
	public static String NL = "\n";	
	public static String TB = "\t";	
	public static String MiddleBracketS = "[";
	public static String MiddleBracketE = "]";
	public static String BBracketS = "{";
	public static String BBracketE = "}";	
	public static String ZeroSpace = "";
	public static String FSlash = "/";
	public static String sCOLON = ";";
	public static String US = "_";
	public static String PIPE = "|";
	public static String GT = ">";
	public static String ST = "<";
	public static String EQ = "=";
	public static String Plus = "+";
		
		
	public static void main(String[] args) {
		System.out.println("Testing maven output.....");
	}
	
	SC () {
		// default constructor
	}
	
	///////////////////////////////////////////////////////////////////
	public static String Formate (double d) {
		String pattern = "##.##";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		String ret = decimalFormat.format(d);
		return ret;
	}
	///////////////////////////////////////////////////////////////////

	public static String Formate (float d) {
		String pattern = "##.##";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		String ret = decimalFormat.format((double)d);
		return ret;
	}
	///////////////////////////////////////////////////////////////////


	 
}
