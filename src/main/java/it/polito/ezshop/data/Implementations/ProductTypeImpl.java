package it.polito.ezshop.data.Implementations;

import it.polito.ezshop.data.ProductType;

import it.polito.ezshop.exceptions.*;

public class ProductTypeImpl implements ProductType{
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
		this.productDescription = "no dscription";
		this.barCode = "xxxxxxxxxxxxxx";
		this.pricePerUnit = 0.0;
		this.id = null;
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

// method to verify if a string contains only numbers
    public static boolean isStringOnlyNumbers(String str){
        return ((str != null)
                && (!str.equals(""))
                && (str.matches("^[0-9]*$")));
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
		if(this.id == -1 || this.id == null) System.out.println("no product");
		else System.out.println(this.id + ")" + this.productDescription + 
				": " +  this.pricePerUnit + "€" + " - " + this.barCode + ", at " +  
				this.location.getPosition() + ", pieces: " +  this.quantity +
				" (" + this.note + ") - [discount rate: " + this.discountRate + "%]");
	};
		
}
