package org.mashad.jbsbe.schema;

import java.time.LocalDateTime;

import org.junit.Test;
import org.mashad.jbsbe.iso.I50Factory;
import org.mashad.jbsbe.iso.I50Message;
import org.mashad.jbsbe.iso.PurchaseRequest;
import org.mashad.jbsbe.iso.SimpleTransformer;

/**
 * Created by keyhan on 2017-03-28.
 */
public class SchemaImporterTest {

    @Test
    public void readFromFile() throws Exception {
        I50Factory<SimpleTransformer> factory = new I50Factory<>(SimpleTransformer.class);
        I50Utility.loadFieldSchema(null);
        PurchaseRequest purchaseRequest = PurchaseRequest.builder().amount(100L).date(LocalDateTime.now())
                .cardNumber("1234567891234567").build();
        I50Message message = factory.newMessage(purchaseRequest);
        System.out.println("message = " + message);
    }
}
