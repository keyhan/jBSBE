package io.github.keyhan.jbsbe.iso;

import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.junit.Before;
import org.junit.Test;

import io.github.keyhan.jbsbe.iso.I50Factory;
import io.github.keyhan.jbsbe.iso.I50Message;
import io.github.keyhan.jbsbe.iso.I50Type;
import io.github.keyhan.jbsbe.iso.SimpleTransformer;

public class I50MessageTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		I50Factory<SimpleTransformer> factory;
		try {
			factory = new I50Factory<>(SimpleTransformer.class);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
			fail("Failed to load transformer into factory class");
			return;
		}
		I50Factory.addField(4, "Amount", I50Type.AMOUNT);
		I50Factory.addField(10, "Date", I50Type.DATE10);
		I50Factory.addField(35, "cardNumber", I50Type.NUMERIC, 16, "xxxx-xxxx-xxxx-####");
		I50Factory.addField(11, "stan", I50Type.NUMERIC, 6);

		PurchaseRequest purchaseRequest = PurchaseRequest.builder().amount(100L).date(LocalDateTime.now()).stan(123456)
				.cardNumber("1234567891234567").build();
		try {
			I50Message message = factory.newMessage(purchaseRequest);
			System.out.println(message);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			fail("Not able to create message");
		}

	}
	
	@Test
	public void testCustomTransformer() {
		I50Factory<KeyhanTransformer> factory;
		try {
			factory = new I50Factory<>(KeyhanTransformer.class);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
			fail("Failed to load transformer into factory class");
			return;
		}
		I50Factory.addField(4, "Amount", I50Type.AMOUNT);
		I50Factory.addField(10, "Date", I50Type.DATE10);
		I50Factory.addField(35, "cardNumber", I50Type.NUMERIC, 16, "xxxx-xxxx-xxxx-####");
		I50Factory.addField(11, "stan", I50Type.NUMERIC, 6);

		PurchaseRequest purchaseRequest = PurchaseRequest.builder().amount(100L).date(LocalDateTime.now()).stan(123456)
				.cardNumber("1234567891234567").build();
		try {
			I50Message message = factory.newMessage(purchaseRequest);
			System.out.println(message);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			fail("Not able to create message");
		}
		
	}
	
	
	@Test
	public void testAutoStan() {
		I50Factory<SimpleTransformer> factory;
		try {
			factory = new I50Factory<>(SimpleTransformer.class);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
			fail("Failed to load transformer into factory class");
			return;
		}
		I50Factory.addField(4, "Amount", I50Type.AMOUNT);
		I50Factory.addField(10, "Date", I50Type.DATE10);
		I50Factory.addField(35, "cardNumber", I50Type.NUMERIC, 16, "xxxx-xxxx-xxxx-####");
		I50Factory.addField(11, "stan", I50Type.NUMERIC, 6);

		PurchaseRequest2 purchaseRequest = PurchaseRequest2.builder().amount(100L).date(LocalDateTime.now())
				.cardNumber("1234567891234567").build();
		try {
			I50Message message = factory.newMessage(purchaseRequest);
			if(message.getObjectValue(11) == null) {
				fail("Stan has to be set automatically");
			}
			System.out.println(message);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			fail("Not able to create message");
		}
		
	}

	@Test
	public void testExpirationDate() {
		I50Factory<SimpleTransformer> factory;
		try {
			factory = new I50Factory<>(SimpleTransformer.class);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
			fail("Failed to load transformer into factory class");
			return;
		}
		I50Factory.addField(4, "Amount", I50Type.AMOUNT);
		I50Factory.addField(5, "Expiration Date", I50Type.DATE_EXP);
		I50Factory.addField(10, "Date", I50Type.DATE10);
		I50Factory.addField(35, "cardNumber", I50Type.NUMERIC, 16, "xxxx-xxxx-xxxx-####");
		I50Factory.addField(11, "stan", I50Type.NUMERIC, 6);

		PurchaseRequest3 purchaseRequest = PurchaseRequest3.builder().amount(100L).date(LocalDateTime.now())
				.expirationDate(YearMonth.of(2022, 5)).cardNumber("1234567891234567").build();
		try {
			I50Message message = factory.newMessage(purchaseRequest);
			System.out.println(message);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			fail("Not able to create message");
		}
		
	}
	

}
