package org.mashad.jbsbe.schema;

import org.junit.Test;
import org.mashad.jbsbe.iso.I50Factory;
import org.mashad.jbsbe.iso.I50Message;
import org.mashad.jbsbe.iso.PurchaseRequest;
import org.mashad.jbsbe.iso.SimpleTransformer;

import java.util.Date;

/**
 * Created by keyhan on 2017-03-28.
 */
public class SchemaImporterTest {

    @Test
    public void readFromFile() throws Exception {
        I50Factory<SimpleTransformer> factory = new I50Factory<>(SimpleTransformer.class);
        I50Utility.loadFieldSchema(null);
        PurchaseRequest purchaseRequest = PurchaseRequest.builder().amount(100L).date(new Date())
                .cardNumber("1234567891234567").build();
        I50Message message = factory.newMessage(purchaseRequest);
        System.out.println("message = " + message);
    }
}
