package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.exceptions.*;

public class ExceptionsClassTest {

	@Test
	public void testCase1() {
		assertThrows(InvalidCreditCardException.class, () -> {throw new InvalidCreditCardException();});
		assertThrows(InvalidCreditCardException.class, () -> {throw new InvalidCreditCardException("InvalidCreditCard");});

		assertThrows(InvalidCustomerCardException.class, () -> {throw new InvalidCustomerCardException();});
		assertThrows(InvalidCustomerCardException.class, () -> {throw new InvalidCustomerCardException("InvalidCustomerCard");});

		assertThrows(InvalidCustomerIdException.class, () -> {throw new InvalidCustomerIdException();});
		assertThrows(InvalidCustomerIdException.class, () -> {throw new InvalidCustomerIdException("InvalidCustomerId");});

		assertThrows(InvalidCustomerNameException.class, () -> {throw new InvalidCustomerNameException();});
		assertThrows(InvalidCustomerNameException.class, () -> {throw new InvalidCustomerNameException("InvalidCustomerName");});

		assertThrows( InvalidDiscountRateException.class, () -> {throw new  InvalidDiscountRateException();});
		assertThrows( InvalidDiscountRateException.class, () -> {throw new  InvalidDiscountRateException("InvalidDiscountRate");});

		assertThrows(InvalidLocationException.class, () -> {throw new InvalidLocationException();});
		assertThrows(InvalidLocationException.class, () -> {throw new InvalidLocationException("InvalidLocation");});

		assertThrows(InvalidOrderIdException.class, () -> {throw new InvalidOrderIdException();});
		assertThrows(InvalidOrderIdException.class, () -> {throw new InvalidOrderIdException("InvalidOrderId");});

		assertThrows(InvalidPasswordException.class, () -> {throw new InvalidPasswordException();});
		assertThrows(InvalidPasswordException.class, () -> {throw new InvalidPasswordException("InvalidPassword");});
		
		assertThrows(InvalidPaymentException.class, () -> {throw new InvalidPaymentException();});
		assertThrows(InvalidPaymentException.class, () -> {throw new InvalidPaymentException("InvalidPayment");});
		
		assertThrows(InvalidPricePerUnitException.class, () -> {throw new InvalidPricePerUnitException();});
		assertThrows(InvalidPricePerUnitException.class, () -> {throw new InvalidPricePerUnitException("InvalidPricePerUnit");});

		assertThrows(InvalidProductCodeException.class, () -> {throw new InvalidProductCodeException();});
		assertThrows(InvalidProductCodeException.class, () -> {throw new InvalidProductCodeException("InvalidProductCode");});

		assertThrows(InvalidProductDescriptionException.class, () -> {throw new InvalidProductDescriptionException();});
		assertThrows(InvalidProductDescriptionException.class, () -> {throw new InvalidProductDescriptionException("InvalidProductDescription");});

		assertThrows(InvalidProductIdException.class, () -> {throw new InvalidProductIdException();});
		assertThrows(InvalidProductIdException.class, () -> {throw new InvalidProductIdException("InvalidOrderId");});

		assertThrows(InvalidQuantityException.class, () -> {throw new InvalidQuantityException();});
		assertThrows(InvalidQuantityException.class, () -> {throw new InvalidQuantityException("InvalidQuantity");});

		assertThrows(InvalidRoleException.class, () -> {throw new InvalidRoleException();});
		assertThrows(InvalidRoleException.class, () -> {throw new InvalidRoleException("InvalidRole");});

		assertThrows(InvalidTransactionIdException.class, () -> {throw new InvalidTransactionIdException();});
		assertThrows(InvalidTransactionIdException.class, () -> {throw new InvalidTransactionIdException("InvalidTransactionId");});

		assertThrows(InvalidUserIdException.class, () -> {throw new InvalidUserIdException();});
		assertThrows(InvalidUserIdException.class, () -> {throw new InvalidUserIdException("InvalidUserId");});

		assertThrows(InvalidUsernameException.class, () -> {throw new InvalidUsernameException();});
		assertThrows(InvalidUsernameException.class, () -> {throw new InvalidUsernameException("InvalidUsername");});

		assertThrows(UnauthorizedException.class, () -> {throw new UnauthorizedException();});
		assertThrows(UnauthorizedException.class, () -> {throw new UnauthorizedException("Unauthorized");});


	}

}
