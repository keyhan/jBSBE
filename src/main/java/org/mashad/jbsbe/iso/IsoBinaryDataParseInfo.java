package org.mashad.jbsbe.iso;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Arrays;

import com.solab.iso8583.CustomField;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.parse.FieldParseInfo;

public class IsoBinaryDataParseInfo extends FieldParseInfo {
	
	 private int len;

	public IsoBinaryDataParseInfo(IsoType t, int len) {
		super(t, len);
		this.len = len;
	}

	@Override
     public <T> IsoValue<?> parseBinary(int field, byte[] buf, int pos, CustomField<T> custom)
             throws ParseException {
         IsoBinaryData data = new IsoBinaryData();
         data.setData(Arrays.copyOfRange(buf, pos, pos + len));
         IsoBinaryValue binaryValue = new IsoBinaryValue(IsoType.BINARY, data, len);
         return binaryValue;
     }

     @Override
     public <T> IsoValue<?> parse(int field, byte[] buf, int pos, CustomField<T> custom)
             throws ParseException, UnsupportedEncodingException {
         return parseBinary(field, buf, pos, custom);
     }

}
