package it.polito.ezshop.data.Implementations;

import it.polito.ezshop.data.ProductType;

public class ProductTypeImpl implements ProductType {

	private Integer quantity = 0;
	private String note;
	private String productDescription;
	private String barCode;
	private Double pricePerUnit;
	private Integer id = -1;
	private Double discountRate = 0.0;
	private PositionImpl location = new PositionImpl();

	// constructor empty
	public ProductTypeImpl() {

		this.note = "no notes";
		this.productDescription = "no description";
		this.barCode = "xxxxxxxxxxxxxx";
		this.pricePerUnit = 0.0;
		this.id = -1;

	}

	// constructor with parameters
	public ProductTypeImpl(String note, String productDescription, String barCode, Double pricePerUnit) {

		Long.parseLong(barCode);
		this.note = note;
		this.productDescription = productDescription;
		this.barCode = barCode;
		this.pricePerUnit = pricePerUnit;

	};

	// getter quantity
	public Integer getQuantity() {

		return quantity;

	};

	// setter quantity
	public void setQuantity(Integer quantity) {

		this.quantity = quantity;

	};

	// getter location
	public String getLocation() {

		return location.getPosition();

	};

	// setter location
	public void setLocation(String location) {

		this.location.setPosition(location);

	};

	// getter note
	public String getNote() {

		return note;

	};

	// setter note
	public void setNote(String note) {

		this.note = note;

	};

	// getter product description
	public String getProductDescription() {

		return productDescription;

	};

	// setter product description
	public void setProductDescription(String productDescription) {

		this.productDescription = productDescription;

	};

	// setter barcode
	public String getBarCode() {

		return barCode;

	};

	// setter barcode
	public void setBarCode(String barCode) {

		this.barCode = barCode;

	};

	// getter price per unit
	public Double getPricePerUnit() {

		return pricePerUnit;

	};

	// setter price per unit
	public void setPricePerUnit(Double pricePerUnit) {

		this.pricePerUnit = pricePerUnit;

	};

	// getter ID
	public Integer getId() {

		return id;

	};

	// setter ID
	public void setId(Integer id) {

		this.id = id;

	};

	// getter discount rate
	public Double getDiscountRate() {

		return discountRate;

	}

	// setter discount rate
	public void setDiscountRate(Double discountRate) {

		this.discountRate = discountRate;

	}

	// method to print all informations about the product
	public void print() {

		if (this.id == -1 || this.id == null)
			System.out.println("no product");
		else
			System.out.println(this.id + ")" + this.productDescription + ": " + this.pricePerUnit + "€" + " - "
					+ this.barCode + ", at " + this.location.getPosition() + ", pieces: " + this.quantity + " ("
					+ this.note + ") - [discount rate: " + this.discountRate + "%]");

	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((barCode == null) ? 0 : barCode.hashCode());
		result = prime * result + ((discountRate == null) ? 0 : discountRate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((pricePerUnit == null) ? 0 : pricePerUnit.hashCode());
		result = prime * result + ((productDescription == null) ? 0 : productDescription.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
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
		ProductTypeImpl other = (ProductTypeImpl) obj;
		if (barCode == null) {
			if (other.barCode != null)
				return false;
		} else if (!barCode.equals(other.barCode))
			return false;
		if (discountRate == null) {
			if (other.discountRate != null)
				return false;
		} else if (!discountRate.equals(other.discountRate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (pricePerUnit == null) {
			if (other.pricePerUnit != null)
				return false;
		} else if (!pricePerUnit.equals(other.pricePerUnit))
			return false;
		if (productDescription == null) {
			if (other.productDescription != null)
				return false;
		} else if (!productDescription.equals(other.productDescription))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		return true;

	};

}
