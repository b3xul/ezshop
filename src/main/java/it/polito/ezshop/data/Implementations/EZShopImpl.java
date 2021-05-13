package it.polito.ezshop.data.Implementations;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import it.polito.ezshop.exceptions.*;

public class EZShopImpl {

// method to open the connection to database
	@SuppressWarnings("finally")
	public Connection dbAccess() {
		
		Connection conn = null;  
	    
		try {  
	    
			String url = "jdbc:sqlite:C:\\Users\\andre\\OneDrive\\Desktop\\prova_java.db";  
	        conn = DriverManager.getConnection(url);  
            System.out.println("Connection to SQLite has been established.");
	    
		}catch (Exception e) {  
	    
			System.out.println(e.getMessage());  
        
		}finally {  
        
			return conn;
	    
		}
	
	}
	
// method to close the connection to database
	public void dbClose(Connection conn) {
		 
		try {  
	    
			if (conn != null) {  
	    	
				conn.close();  
	           
			}  
	     
		}catch (SQLException ex) {  
	      
	    	 System.out.println(ex.getMessage());  
	    
		}  
	
	}
	
// method to create a new product	
	public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		
		Integer id;
		Connection conn = null;  
		
		try {
		
			if(false){ //User exception
			
				throw new UnauthorizedException("user error");
				
			}
			else {
				
				ProductTypeImpl newProduct = new ProductTypeImpl(note, description, productCode, pricePerUnit);
				conn = dbAccess();
				
				
				System.out.println("Product created");
				String sql = "INSERT INTO product (description, price, barcode, location, quantity, note) VALUES ('" + description + "', " + pricePerUnit + ", '" + productCode + "', '" + newProduct.getLocation() + "', " + newProduct.getQuantity() + ", '" + note + "')";

				Statement statement = conn.createStatement();
		        statement.executeUpdate(sql);
		        
		        String sql2 = "SELECT id FROM product WHERE id=(SELECT max(id) FROM product)";
		        Statement statement2 = conn.createStatement();
		        ResultSet result = statement2.executeQuery(sql2);
		        id = result.getInt("id");
		        newProduct.setId(id);
			}
			
		}catch(NumberFormatException e){
            
			System.out.println("invalid barcode: no letter in barcode");
            id = -1;
		
		}catch(Exception ex){
        
			System.out.println(ex.getMessage());
            id = -1;
		
		}finally {  
        	
	       dbClose(conn);
	       System.out.println("connection closed");
	       
	    }
		
		return id;
		
    }

//method to get the list of all products
	public List<ProductTypeImpl> getAllProductTypes() throws UnauthorizedException {
		
		List<ProductTypeImpl> inventory = new ArrayList<ProductTypeImpl>();
		Connection conn = null;  
		
	    try {  
	    	
	    	if(false){ //User exception
				
				throw new UnauthorizedException("user error");
				
			}
	    	else {
				conn = dbAccess();
	            String sql = "SELECT * FROM product";
		        Statement statement = conn.createStatement();
		        ResultSet result = statement.executeQuery(sql);
		           
		        while(result.next()) {
		        	
		     	   String n = result.getString("note");
		      	   String d = result.getString("description");
		       	   String b = result.getString("barcode");
		       	   Double p = result.getDouble("price");
		       	   Integer id = result.getInt("id");
		       	   Integer q = result.getInt("quantity");
		       	   String l = result.getString("location");
		       	   
		   		   ProductTypeImpl product = new ProductTypeImpl(n , d, b, p);
		   		   product.setId(id);
		 		   product.setQuantity(q);
		   		   product.setLocation(l);
	
		   		   inventory.add(product);
				}
	    	
	    	
	   		   
            }
	        
	    } catch (Exception e) {  
	    	
	        System.out.println(e.getMessage()); 
	        
        } finally {  
        	
	       dbClose(conn);
	       System.out.println("connection closed");
	       
	    }
	    
	    for(ProductTypeImpl p: inventory) {
	    	
	    	p.print();
	    	
	    }
	    
		return inventory;
		
    }
	
// method to get a product by barcode	
	public ProductTypeImpl getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
		
   		ProductTypeImpl product = new ProductTypeImpl();

		Connection conn = null;  
		
	    try {  

	    	if(false){ //User exception
				
				throw new UnauthorizedException("user error");
				
			}
	    	else {
	    		conn = dbAccess();
	            String sql = "SELECT * FROM product WHERE barcode = '" + barCode + "'";
		        Statement statement = conn.createStatement();
		        ResultSet result = statement.executeQuery(sql);
		        String n = result.getString("note");
		      	String d = result.getString("description");
		       	String b = result.getString("barcode");
		       	Double p = result.getDouble("price");
		       	Integer id = result.getInt("id");
		       	Integer q = result.getInt("quantity");
		       	String l = result.getString("location");
		       			
		       	product.setBarCode(b);
		       	product.setNote(n);
		       	product.setPricePerUnit(p);
		       	product.setProductDescription(d);
		   		product.setId(id);
		 		product.setQuantity(q);
		   		product.setLocation(l);

	    	}
	    	
	    	
	    } catch (Exception e) {  
	    	
	        System.out.println(e.getMessage()); 
	        
        } finally {  
        	
	       dbClose(conn);
	       System.out.println("connection closed");
	       
	    }
        return product;
    }
	
// method to get a product by description
	public List<ProductTypeImpl> getProductTypesByDescription(String description) throws UnauthorizedException {
		
		List<ProductTypeImpl> matchingProducts = new ArrayList<ProductTypeImpl>();
		Connection conn = null;  
		
		try {  

	    	if(false){ //User exception
				
				throw new UnauthorizedException("user error");
				
			}
	    	else {
	    		conn = dbAccess();
	            String sql = "SELECT DISTINCT * FROM product WHERE description LIKE '%" + description + "%'";
		        Statement statement = conn.createStatement();
		        ResultSet result = statement.executeQuery(sql);
		           
		        while(result.next()) {
		        	
		     	   String n = result.getString("note");
		      	   String d = result.getString("description");
		       	   String b = result.getString("barcode");
		       	   Double p = result.getDouble("price");
		       	   Integer id = result.getInt("id");
		       	   Integer q = result.getInt("quantity");
		       	   String l = result.getString("location");
		       	   
		   		   ProductTypeImpl product = new ProductTypeImpl(n , d, b, p);
		   		   product.setId(id);
		 		   product.setQuantity(q);
		   		   product.setLocation(l);
	
		   		   matchingProducts.add(product);
		    	}
	    	
	    	
	   		   
            }
	        
	    } catch (Exception e) {  
	    	
	        System.out.println(e.getMessage()); 
	        
        } finally {  
        	
	       dbClose(conn);
	       System.out.println("connection closed");
	       
	    }
		return matchingProducts;
    }
	
// method to update product's fields
	public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException{
		
		Boolean success = false;
		Connection conn = null;  
		
	    try {  
	    	
	    	if(id <= 0) {
	    		throw new InvalidProductIdException("invalid ID");
	    	}
	    	else if(newDescription == null || newDescription == "") {
				throw new InvalidProductDescriptionException("invalid description");
			}
			else if(newCode == null || newCode == "") {
				throw new InvalidProductCodeException("invalid barcode: barcode not inserted");
			}
			else if(newCode.length() < 12 || newCode.length() > 14) {
				Long.parseLong(newCode);				
				throw new InvalidProductCodeException("invalid barcode: wrong length");
			}
			else if(newPrice <= 0) {
				throw new InvalidPricePerUnitException("invalid price");
			}
			else if(false){ //User exception					
				throw new UnauthorizedException("user error");					
			}
		    else {
				Long.parseLong(newCode);
				conn = dbAccess();
	            String sql = "UPDATE product SET description = '" + newDescription + "', barcode = '" + newCode + "', price = " + newPrice + ", note = '" + newNote + "' WHERE id = '" + id + "'";
		        Statement statement = conn.createStatement();
		        statement.executeUpdate(sql);
		        success = true;
		    }
	    	
	    	
	        
	    }catch(NumberFormatException ex){
	    	
	    	System.out.println("barcode must contains only numbers");
	    	
        } catch (Exception e) {  
	    	
	        System.out.println(e.getMessage()); 
	        success = false;
	        
        }finally {  
        
        	
	       dbClose(conn);
	       System.out.println("connection closed");
	       
	    }
		return success;
	};

// method to delete a product
	public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        
    	Boolean success = false;
		Connection conn = null;  
		
	    try {  
	    	
	    	if(id <= 0) {
	    		throw new InvalidProductIdException("invalid ID");
	    	}
			else if(false){ //User exception					
				throw new UnauthorizedException("user error");					
			}else {
				
	    	   	conn = dbAccess();
	            String sql = "DELETE FROM product WHERE id =" + id;
	            Statement statement = conn.createStatement();
		        statement.executeUpdate(sql);
		        success = true;
		        System.out.println("product deleted");
		        
			}
	        
	    } catch (Exception e) {  
	    	
	        System.out.println(e.getMessage()); 
	        success = false;
	        
        } finally {  
        	
	       dbClose(conn);
	       System.out.println("connection closed");
	       
	    }
		return success;
    }

	
	
	public static void main(String[] args) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException {
		EZShopImpl shop = new EZShopImpl();
//		System.out.println("****"  );
//		Integer p2 = shop.createProductType("", "99999999999999", 1.5, "note");
//		System.out.println(p2);
		System.out.println("****"  );
		Integer p1 = shop.createProductType("carne", "1234567898765", 10.20, "fresca");
//		System.out.println(p1);
//		System.out.println("****"  );
//		Integer p3 = shop.createProductType("", "1234321234312", 10, "note");
//		System.out.println(p3);
//		System.out.println("****"  );
//		Integer p4 = shop.createProductType("gggggg", "11111111111111", 1.5, "note");
//		System.out.println(p4);
//		System.out.println("****"  );
	//	shop.deleteProductType(19);
//		System.out.println("inventory");
		List<ProductTypeImpl> inventory = shop.getAllProductTypes();
////		System.out.println("product");
//		ProductTypeImpl p = shop.getProductTypeByBarCode("1234567898765");
	//	p.print();
	//	shop.updateProduct(23, "pastaasa", "899899098765", 11.00, "barilla");
//		System.out.println("inventorynew");
//		List<ProductType> inventorynew = shop.getAllProductTypes();
//		System.out.println("Search for pasta");
//		List<ProductTypeImpl> matching = shop.getProductTypesByDescription("a");
//		for(ProductTypeImpl p: matching) {
//			    	
//			    	p.print();
//			    	
//		}
	}
	
	
	

   

    
    

    

    

    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        return false;
    }


}
