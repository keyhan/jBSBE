package org.mashad.jbsbe.iso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.mashad.jbsbe.iso.I50Factory.I50Field;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.impl.SimpleTraceGenerator;
import com.solab.iso8583.util.HexCodec;

import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthLongestWordMinCol;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class I50Message extends IsoMessage {

	private static volatile Calendar initday = Calendar.getInstance();

	public I50Message(String header) {
		this.header = header;
	}

	private static volatile SimpleTraceGenerator stanGenerator = new SimpleTraceGenerator(1);

	private int pinPosition = -1;

	private Map<String, Object> metadata = new HashMap<>();

	private int macPosition;

	private String header;

	public <S> I50Message setField(int key, S value) {
		switch (I50Factory.i50Fields.get(key).i50Type) {
		case I50Binary:
			if (value instanceof IsoBinaryData) {
				super.setField(key, new IsoBinaryValue(IsoType.BINARY, (IsoBinaryData) value,
						I50Factory.i50Fields.get(key).length));
			}
			return this;
		case I50LLLBIN:
			if (value instanceof IsoBinaryData) {
				super.setField(key, new LLLBinaryValue(IsoType.BINARY, (IsoBinaryData) value));
			}
			return this;
		case I50LLBIN:
			if (value instanceof IsoBinaryData) {
				super.setField(key, new LLBinaryValue(IsoType.BINARY, (IsoBinaryData) value));
			}
			return this;
		case I50LLLLBIN:
			if (value instanceof IsoBinaryData) {
				super.setField(key, new LLLLBinaryValue(IsoType.BINARY, (IsoBinaryData) value));
			}
			return this;
		default:
			break;
		}

		Integer length = I50Factory.i50Fields.get(key).length;
		if (null != length) {
			super.setField(key,
					new IsoValue<>(I50Type.getIsoType(I50Factory.i50Fields.get(key).i50Type), value, length));
		} else {
			super.setField(key, new IsoValue<>(I50Type.getIsoType(I50Factory.i50Fields.get(key).i50Type), value));
		}
		return this;

	}

	public synchronized static int generateStan() {
		Calendar today = Calendar.getInstance();
		Date now = new Date();
		today.setTime(now);
		if (!DateUtils.isSameDay(today, initday)) {
			stanGenerator = new SimpleTraceGenerator(1);
			initday = today;
		}
		return stanGenerator.nextTrace();
	}

	public IsoBinaryData getMac() {
		return getObjectValue(getMacPosition());
	}

	public I50Message setMac(final byte[] mac) {
		return setMac(new IsoBinaryData(mac));
	}

	public I50Message setMac(IsoBinaryData isoBinaryData) {
		return setField(getMacPosition(), isoBinaryData);
	}

	public byte[] getMacBuffer() {
		byte[] macBuffer = this.writeData();

		// Remove the dummy MAC from the MAC-buffer
		int macBufferLen = macBuffer.length - 8;
		macBuffer = Arrays.copyOf(macBuffer, macBufferLen);
		return macBuffer;
	}

	public boolean isErroneous() {
		return this.getType() >= 0x9000;
	}

	@Override
	public String toString() {
		V2_AsciiTable header_table = new V2_AsciiTable();
		V2_AsciiTable body_table = new V2_AsciiTable();

		header_table.addRule();
		header_table.addRow("Field", "Value");
		header_table.addRule();
		if (null != this.getIsoHeader()) {
			header_table.addRow("Header", this.getIsoHeader());
			header_table.addRule();
		}
		header_table.addRow("Message Type", Integer.toHexString(this.getType()));
		header_table.addRule();
		for (String key : metadata.keySet()) {
			header_table.addRow(key, metadata.get(key));
			header_table.addRule();
		}

		int length = 0;
		body_table.addRule();
		body_table.addRow("Field Number", "Name", "Type", "Length", "Value");
		body_table.addRule();
		List<Integer> indexes = new ArrayList<>(I50Factory.i50Fields.keySet());
		Collections.sort(indexes);
		//for (int i : I50Factory.i50Fields.keySet()) {
		for (int i : indexes) {
			if (this.hasField(i)) {

				I50Field i50Field = I50Factory.i50Fields.get(i);
				Object fieldValue = this.getObjectValue(i);

				if (fieldValue == null) {
					continue;
				}

				length = fieldValue.toString().length();
				if (fieldValue instanceof String && i50Field.getMask() != null) {
					fieldValue = CardUtils.maskCardNumber((String) fieldValue, i50Field.getMask());

				} else if (fieldValue instanceof IsoBinaryData) {
					byte[] buffer = ((IsoBinaryData) fieldValue).getData();
					length = buffer.length;
					fieldValue = HexCodec.hexEncode(buffer, 0, length);
					// This is used only for DATE10
				} else if (fieldValue instanceof Date) {
					length = 10;
				}
				String maxLength = "";
				// String maxLength = "unlimited";
				Integer maxLengthValue = i50Field.length;
				if (maxLengthValue != null) {
					maxLength = "(" + maxLengthValue.toString() + ")";
				} else if ((I50Type.getIsoType(i50Field.i50Type)).getLength() != 0) {
					maxLengthValue = (I50Type.getIsoType(i50Field.i50Type)).getLength();
					maxLength = "(" + maxLengthValue.toString() + ")";
				}
				body_table.addRow(i, i50Field.getName(), i50Field.i50Type.name(), length + maxLength, fieldValue);
				body_table.addRule();
			}

		}
		V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
		rend.setTheme(V2_E_TableThemes.UTF_DOUBLE.get());
		rend.setWidth(new WidthLongestWordMinCol(10));
		RenderedTable rt_header = rend.render(header_table);
		RenderedTable rt_body = rend.render(body_table);
		return rt_header.toString() + rt_body.toString();
	}

	// @Override
	// public String toString() {
	// final StringBuilder debugMsg = new StringBuilder();
	// if (null != this.getIsoHeader()) {
	// debugMsg.append("\n Header - " +
	// this.getIsoHeader()).append(this.getIsoHeader());
	// }
	// debugMsg.append("\nMTI:
	// ").append(Integer.toHexString(this.getType())).append('\n');
	// for (int i : hifFields.keySet()) {
	// if (this.hasField(i)) {
	// final String fieldNo = StringUtils.leftPad(String.valueOf(i), 3, ' ');
	// Object fieldValue = this.getObjectValue(i);
	//
	// if (i == 35) {
	// fieldValue = CardUtils.maskCardNumber(fieldValue.toString(), '*');
	// }
	//
	// if (fieldValue instanceof IsoBinaryData) {
	// fieldValue = "Binary Data: " + Hex.encodeHexString(((IsoBinaryData)
	// fieldValue).getData());
	// }
	// debugMsg.append("Field ").append(fieldNo).append(": ")
	// .append(null != fieldValue ? fieldValue.toString() : null).append("\n");
	// }
	// }
	// return debugMsg.toString();
	// }
}
