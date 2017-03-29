package org.mashad.jbsbe.schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.mashad.jbsbe.iso.I50Factory;
import org.mashad.jbsbe.schema.model.Iso8583Schema;

/**
 * Created by keyhan on 2017-03-28.
 */
public class I50Utility {

    private static final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    public static void loadFieldSchema(String schemaFile) {

        try {
            Iso8583Schema schema = objectMapper.readValue(I50Utility.class.getClassLoader()
                    .getResourceAsStream((schemaFile == null) ? "iso8583_schema.yml" : schemaFile), Iso8583Schema.class);

            schema.getFields().forEach(field -> {
                if(field.getLength() != null && field.getMask() != null) {
                    I50Factory.addField(field.getPosition(), field.getTitle(), field.getType(), field.getLength(), field.getMask());
                } else if(field.getLength() != null) {
                    I50Factory.addField(field.getPosition(), field.getTitle(), field.getType(), field.getLength());
                } else if(field.getMask() != null) {
                    I50Factory.addField(field.getPosition(), field.getTitle(), field.getType(), field.getMask());
                } else {
                    I50Factory.addField(field.getPosition(), field.getTitle(), field.getType());
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
