package ch.hslu.swde.wda.persister.impl.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ch.hslu.swde.wda.domain.Customer;
import ch.hslu.swde.wda.util.DbHelper;



public class PersisterCustomerIT {
	
	
	
	@Disabled
	public void testSaveCustomer() throws Exception {
		Customer customer1 = new Customer("Müller", "Peter","mail@test.com", "Zürichstrasse.70","Pfaffhausen",8118, "0796951893");
		PersisterCustomerImpl persist1 = new PersisterCustomerImpl();
		persist1.saveCustomer(customer1);
		assertEquals(8118, customer1.getZip());
	}
//	
//	@AfterEach
//	public void deleteCustomer() {
//		DbHelper.deleteCustomer();
//	}

}
