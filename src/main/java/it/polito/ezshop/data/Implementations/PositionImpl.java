package it.polito.ezshop.data.Implementations;

public class PositionImpl {

	Integer aisleNumber;
	String alphabeticId;
	Integer levelNumber;

	// constructor empty
	public PositionImpl() {

		this.aisleNumber = null;
		this.alphabeticId = null;
		this.levelNumber = null;

	}

	// constructor with parameters
	public PositionImpl(String location) {

		if (location == "" || location.split("-").length != 3) {
			this.aisleNumber = null;
			this.alphabeticId = "";
			this.levelNumber = null;
		} else {
			Integer aisleNumber = Integer.parseInt(location.split("-")[0]);
			String alphabeticId = location.split("-")[1];
			Integer levelNumber = Integer.parseInt(location.split("-")[2]);
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

		if (aisleNumber == null || alphabeticId == null || alphabeticId == "" || levelNumber == null)
			return " ";
		else
			return aisleNumber.toString() + " " + alphabeticId + " " + levelNumber.toString();

	}

	// setter entire position
	public void setPosition(String location) {

		if (location == "" || location.split("-").length != 3) {
			this.aisleNumber = null;
			this.alphabeticId = null;
			this.levelNumber = null;
		} else {
			Integer aisleNumber = Integer.parseInt(location.split("-")[0]);
			String alphabeticId = location.split("-")[1];
			Integer levelNumber = Integer.parseInt(location.split("-")[2]);
			this.aisleNumber = aisleNumber;
			this.alphabeticId = alphabeticId;
			this.levelNumber = levelNumber;
		}

	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((aisleNumber == null) ? 0 : aisleNumber.hashCode());
		result = prime * result + ((alphabeticId == null) ? 0 : alphabeticId.hashCode());
		result = prime * result + ((levelNumber == null) ? 0 : levelNumber.hashCode());
		return result;

	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PositionImpl other = (PositionImpl) obj;
		if (aisleNumber == null) {
			if (other.aisleNumber != null)
				return false;
		} else if (!aisleNumber.equals(other.aisleNumber))
			return false;
		if (alphabeticId == null) {
			if (other.alphabeticId != null)
				return false;
		} else if (!alphabeticId.equals(other.alphabeticId))
			return false;
		if (levelNumber == null) {
			if (other.levelNumber != null)
				return false;
		} else if (!levelNumber.equals(other.levelNumber))
			return false;
		return true;

	}

}
