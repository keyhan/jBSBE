package org.mashad.jbsbe.iso;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class I50MessageTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		I50Factory factory = new I50Factory();
		I50Factory.addField(4, "Amount", I50Type.AMOUNT);
		I50Factory.addField(10, "Date", I50Type.DATE10);
		I50Factory.addField(35, "cardNumber", I50Type.NUMERIC, 16, "xxxx-xxxx-xxxx-####");
		I50Factory.addField(11, "stan", I50Type.NUMERIC, 6);
		
		PurchaseRequest purchaseRequest = PurchaseRequest.builder().amount(100L).date(new Date()).stan(123456).cardNumber("1234567891234567").build();
		try {
			I50Message message = factory.newMessage(purchaseRequest);
			System.out.println(message);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
		
	}

}
