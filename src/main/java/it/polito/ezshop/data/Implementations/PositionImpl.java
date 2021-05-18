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
	
// constructor with parameters
	public PositionImpl(String location) {
		if (location == "" || location.split(" ").length != 3) {
			this.aisleNumber = null;
			this.alphabeticId = "";
			this.levelNumber = null;
		}else {
			Integer aisleNumber = Integer.parseInt(location.split(" ")[0]);
			String alphabeticId = location.split(" ")[1];
			Integer levelNumber = Integer.parseInt(location.split(" ")[2]);
			this.aisleNumber = aisleNumber;
			this.alphabeticId = alphabeticId;
			this.levelNumber = levelNumber;
		}
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
			if (location == "" || location.split(" ").length != 3) {
			this.aisleNumber = null;
			this.alphabeticId = null;
			this.levelNumber = null;
		}else {
			Integer aisleNumber = Integer.parseInt(location.split(" ")[0]);
			String alphabeticId = location.split(" ")[1];
			Integer levelNumber = Integer.parseInt(location.split(" ")[2]);
			this.aisleNumber = aisleNumber;
			this.alphabeticId = alphabeticId;
			this.levelNumber = levelNumber;
		}
	}
	
}
