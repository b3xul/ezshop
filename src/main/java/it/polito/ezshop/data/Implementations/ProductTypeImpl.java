package ezshop;

import it.polito.ezshop.exceptions.*;
public class ProductTypeImpl {
	private Integer quantity = 0;
	private String location = "no location";
	private String note;
	private String productDescription;
	private String barCode;
	private Double pricePerUnit;
	private Integer id;
	
	public ProductTypeImpl() {
		this.note = "no notes";
		this.productDescription = "no dscription";
		this.barCode = "xxxxxxxxxxxxxx";
		this.pricePerUnit = 0.0;
		this.id = null;
	}
	
	public ProductTypeImpl(String note, String productDescription, String barCode, Double pricePerUnit) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		
			

			if(productDescription == null || productDescription == "") {
				throw new InvalidProductDescriptionException("invalid description");
			}
			else if(barCode == null || barCode == "") {
				throw new InvalidProductCodeException("invalid barcode: barcode not inserted");
			}
			else if(barCode.length() < 12 || barCode.length() > 14) {
				Long.parseLong(barCode);				
				throw new InvalidProductCodeException("invalid barcode: wrong length");
			}
			else if(pricePerUnit <= 0) {
				throw new InvalidPricePerUnitException("invalid price");
			}
			else if(false) {
				//throw new UnauthorizedException("user error");
			}else {
				Long.parseLong(barCode);				
				this.note = note;
				this.productDescription = productDescription;
				this.barCode = barCode;
				this.pricePerUnit = pricePerUnit;
			}
		
		
	}
	
	public Integer getQuantity() {
		return quantity;
	};

    public void setQuantity(Integer quantity) {
    	try {
    		this.quantity = quantity;
    	}catch(Exception ex) {
    		System.out.println("Caught the exception");
            System.out.println(ex.getMessage());
    	}
    	
    };

    public String getLocation() {
    	return location;
    };

    public void setLocation(String location) {
    	
    	this.location = location;
    	
    };

    public String getNote() {
    	return note;
    };

    public void setNote(String note) {
    	
    	this.note = note;
    	
    };

    public String getProductDescription() {
    	
    	return productDescription;
    };

    public void setProductDescription(String productDescription) {
    	try{
    		if(productDescription == null || productDescription == "") {
				throw new InvalidProductDescriptionException("invalid description");
			}
    		this.productDescription = productDescription;
    	}catch(Exception ex) {
    		System.out.println("Caught the exception");
            System.out.println(ex.getMessage());
    	}
    };

    public String getBarCode() {
    	return barCode;
    };

    public void setBarCode(String barCode) {
    	try{
    		if(barCode == null || barCode == "") {
				throw new InvalidProductCodeException("invalid barcode: barcode not inserted");
			}
			Long.parseLong(barCode);
			if(barCode.length() < 12 || barCode.length() > 14) {
				throw new InvalidProductCodeException("invalid barcode: wrong length");
			}
    		this.barCode = barCode;
    	}catch(NumberFormatException e){
			System.out.println("Caught the exception");
            System.out.println("invalid barcode: no letter in barcode");
		}catch(Exception ex) {
			System.out.println("Caught the exception");
            System.out.println(ex.getMessage());
    	}
    };

    public Double getPricePerUnit() {
    	return pricePerUnit;
    };

    public void setPricePerUnit(Double pricePerUnit) {
    	try{
    		if(pricePerUnit <= 0) {
				throw new InvalidPricePerUnitException("invalid price");
			}
    		this.pricePerUnit = pricePerUnit;
    	}catch(Exception ex) {
    		System.out.println("Caught the exception");
            System.out.println(ex.getMessage());
    	}
    };

    public Integer getId() {
    	return id;
    };

    public void setId(Integer id) {
    	try{
    		this.id = id;
    	}catch(Exception e) {
    		
    	}
    };

	
	public void print() {
		System.out.println(this.id + ")" + this.productDescription + ": " +  this.pricePerUnit + "€" + " - " + this.barCode + ", at " +  this.location + ", pieces: " +  this.quantity + " (" + this.note + ")");
	}
	
	
	
	
	
	public static void main(String[] args) {
//		ProductType p1 = new ProductType("buono", "biscotti", "00001111", 1.50);
//		p1.print();
//		p1.barCode = "111";
//		System.out.println(p1.barCode);
		
	}
	
}
