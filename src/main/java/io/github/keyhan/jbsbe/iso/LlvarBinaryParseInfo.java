package io.github.keyhan.jbsbe.iso;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.apache.commons.lang3.ArrayUtils;

import com.solab.iso8583.CustomField;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.parse.LlvarParseInfo;


public class LlvarBinaryParseInfo extends LlvarParseInfo {
	@Override
	public <T> IsoValue<?> parse(final int field, final byte[] buf, final int pos, final CustomField<T> custom)
			throws ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid bin LLVAR field %d pos %d", field, pos), pos);
		} else if (pos + 2 > buf.length) {
			throw new ParseException(
					String.format("Insufficient data for bin LLVAR header, field %d pos %d", field, pos), pos);
		}
		final int len = decodeLength(buf, pos, 2);
		if (len < 0) {
			throw new ParseException(String.format("Invalid bin LLVAR length %d, field %d pos %d", len, field, pos),
					pos);
		} else if (len + pos + 1 > buf.length) {
			throw new ParseException(String.format("Insufficient data for bin LLVAR field %d, pos %d", field, pos),
					pos);
		}
		IsoBinaryData data = new IsoBinaryData();
		data.setData(ArrayUtils.subarray(buf, pos + 2, pos + 2 + len));
		LLLBinaryValue binaryValue = new LLLBinaryValue(IsoType.LLLBIN, data);
		return binaryValue;
	}
	
}