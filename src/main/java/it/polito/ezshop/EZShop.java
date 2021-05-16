package it.polito.ezshop;

import java.time.LocalDate;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.view.EZShopGUI;


public class EZShop {

//    public static void main(String[] args){
//        EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
//        EZShopGUI gui = new EZShopGUI(ezShop);
//    }
	
	public static void main(String[] args) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException, InvalidOrderIdException, InvalidLocationException{
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.payOrderFor("1234567898765", 10, 5);
		ezShop.payOrderFor("1234567898765", 10, 5);
		ezShop.payOrderFor("1234567898765", 10, 5);
		ezShop.payOrderFor("1234567898765", 10, 5);
		ezShop.payOrderFor("1234567898765", 10, 5);
//		ezShop.getAllOrders();
//		ezShop.recordOrderArrival(1);
//		ezShop.issueOrder("98765432123456", 5, 10);
//		ezShop.payOrder(2);
//		ezShop.recordBalanceUpdate(20);
//		ezShop.recordBalanceUpdate(10);
//		ezShop.recordBalanceUpdate(30);
//		ezShop.getCreditsAndDebits(LocalDate.parse("2021-01-01"), LocalDate.parse("2021-12-30"));
//		ezShop.getCreditsAndDebits(null, LocalDate.parse("2021-12-30"));
	}

}
