package io.github.keyhan.jbsbe.iso;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.apache.commons.lang3.ArrayUtils;

import com.solab.iso8583.CustomField;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.parse.LllvarParseInfo;


public class LllvarBinaryParseInfo extends LllvarParseInfo {

	@Override
	public <T> IsoValue<?> parse(final int field, final byte[] buf, final int pos, final CustomField<T> custom)
			throws ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid bin LLLVAR field %d pos %d", field, pos), pos);
		} else if (pos + 3 > buf.length) {
			throw new ParseException(
					String.format("Insufficient data for bin LLLVAR header, field %d pos %d", field, pos), pos);
		}
		final int len = decodeLength(buf, pos, 3);
		if (len < 0) {
			throw new ParseException(String.format("Invalid bin LLLVAR length %d, field %d pos %d", len, field, pos),
					pos);
		} else if (len + pos + 2 > buf.length) {
			throw new ParseException(String.format("Insufficient data for bin LLLVAR field %d, pos %d", field, pos),
					pos);
		}
		IsoBinaryData data = new IsoBinaryData();
		data.setData(ArrayUtils.subarray(buf, pos + 3, pos + 3 + len));
		return new LLLBinaryValue(IsoType.LLLBIN, data);
	}
}
