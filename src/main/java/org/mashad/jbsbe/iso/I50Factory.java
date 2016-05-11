package org.mashad.jbsbe.iso;

import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;
import lombok.ToString;

public class I50Factory {
	
	public static Map<Integer, I50Field> i50Fields = new HashMap<>();
	
	@ToString
    public static class I50Field {
        Integer length;

        @NonNull
        I50Type i50Type;

        private String name;

        public String getName() {
            return name;
        }

        public I50Field(String name, I50Type isoType, int length) {
            this.i50Type = isoType;
            this.length = length;
            this.name = name;
        }

        public I50Field(String name, I50Type isoType) {
            this.i50Type = isoType;
            this.name = name;
        }

    }
	
	public static void addField(int index, String name, I50Type isoType, int length) {
		switch (isoType) {
		case ALPHA:
		case BINARY:
		case NUMERIC:
			if(length < 1) {
				throw new IllegalArgumentException("length is not set correctly");
			}
			I50Factory.i50Fields.put(index, new I50Factory.I50Field(name, isoType, length));
			break;

		default:
			I50Factory.i50Fields.put(index, new I50Factory.I50Field(name, isoType));
			break;
		}
		I50Factory.i50Fields.put(3, new I50Factory.I50Field(name, isoType));

	}

}
