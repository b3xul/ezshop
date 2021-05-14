package it.polito.ezshop.data.Implementations;

import it.polito.ezshop.exceptions.InvalidLocationException;

public class PositionImpl {
	Integer aisleNumber ;
	String alphabeticId ;
	Integer levelNumber ;
	
// constructor empty
	public PositionImpl() {
		this.aisleNumber = null;
		this.alphabeticId = null;
		this.levelNumber = null;
	}
	
// method to verify if a string contains only letters
	public static boolean isStringOnlyAlphabet(String str){
        return ((str != null)
                && (!str.equals(""))
                && (str.matches("^[a-zA-Z]*$")));
    }
	
// method to verify if a string contains only numbers
	public static boolean isStringOnlyNumbers(String str){
        return ((str != null)
                && (!str.equals(""))
                && (str.matches("^[0-9]*$")));
    }

// constructor with parameters
	public PositionImpl(String location) {
		Integer aisleNumber = Integer.parseInt(location.split(" ")[0]);
		String alphabeticId = location.split(" ")[1];
		Integer levelNumber = Integer.parseInt(location.split(" ")[2]);
		this.aisleNumber = aisleNumber;
		this.alphabeticId = alphabeticId;
		this.levelNumber = levelNumber;
	}
	
// getter aisle number
	public Integer getAisleNumber() {
		return aisleNumber;
	}
	
// getter alphabetic ID
	public String getAlphabeticId() {
		return alphabeticId;
	}
	
// getter level number
	public Integer getLevelNumber() {
		return levelNumber;
	}
	
// getter entire position as string
	public String getPosition() {
		if(aisleNumber == null || alphabeticId == null || alphabeticId == "" || levelNumber == null) return " ";
		else return aisleNumber.toString() + " " + alphabeticId + " " + levelNumber.toString();
	}
	
// setter entire position
	public void setPosition(String location) {
			Integer aisleNumber = Integer.parseInt(location.split(" ")[0]);
			String alphabeticId = location.split(" ")[1];
			Integer levelNumber = Integer.parseInt(location.split(" ")[2]);
			this.aisleNumber = aisleNumber;
			this.alphabeticId = alphabeticId;
			this.levelNumber = levelNumber;
	}
	
	
	
	
	
	public static void main(String[] args) throws InvalidLocationException {
		PositionImpl p = new PositionImpl("2 a 1");
		//System.out.println(p.isStringOnlyNumbers("22a£"));
		p.setPosition("2 a 1");
	}
	
}
