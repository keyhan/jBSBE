package io.github.keyhan.jbsbe.iso;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.impl.SimpleTraceGenerator;
import com.solab.iso8583.util.HexCodec;

import de.vandermeer.asciitable.AT_Renderer;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.asciithemes.TA_GridThemes;
import io.github.keyhan.jbsbe.iso.I50Factory.I50Field;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class I50Message extends IsoMessage {

	private static volatile LocalDate initday = LocalDate.now();

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
			case I50BINARY:
				if (value instanceof IsoBinaryData binaryData) {
					super.setField(key, new IsoBinaryValue(IsoType.BINARY, binaryData,
							I50Factory.i50Fields.get(key).length));
				}
				return this;
			case I50LLLBIN:
				if (value instanceof IsoBinaryData binaryData) {
					super.setField(key, new LLLBinaryValue(IsoType.BINARY, binaryData));
				}
				return this;
			case I50LLBIN:
				if (value instanceof IsoBinaryData binaryData) {
					super.setField(key, new LLBinaryValue(IsoType.BINARY, binaryData));
				}
				return this;
			case I50LLLLBIN:
				if (value instanceof IsoBinaryData binaryData) {
					super.setField(key, new LLLLBinaryValue(IsoType.BINARY, binaryData));
				}
				return this;
			default:
				break;
		}

		Integer length = I50Factory.i50Fields.get(key).length;
		if (null != length) {
			super.setField(key,
					new IsoValue<>(I50Type.ISOTYPEMAP.get(I50Factory.i50Fields.get(key).i50Type), value, length));
		} else {
			super.setField(key, new IsoValue<>(I50Type.ISOTYPEMAP.get(I50Factory.i50Fields.get(key).i50Type), value));
		}
		return this;

	}

	public static synchronized int generateStan() {
		LocalDate today = LocalDate.now();
		if (today.isAfter(initday)) {
			stanGenerator = new SimpleTraceGenerator(1);
			initday = today;
		}
		return stanGenerator.nextTrace();
	}

	public I50Message setStan() {
		return setField(11, generateStan());
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
		StringBuilder builder = new StringBuilder();
		if (this.getIsoHeader() != null) {
			builder.append("Header: ");
			builder.append(this.getIsoHeader());
			builder.append(", ");
		}
		builder.append("Message Type: ");
		builder.append(Integer.toHexString(this.getType()));
		for (Entry<String, Object> entry : metadata.entrySet()) {
			builder.append(", " + entry.getKey() + ": ");
			builder.append(entry.getValue());
		}
		List<Integer> indices = new ArrayList<>(I50Factory.i50Fields.keySet());
		Collections.sort(indices);
		builder.append(", Body: [");
		indices.forEach(index -> appendField(builder, index));
		builder.append("]");

		return builder.toString();
	}

	private void appendField(StringBuilder builder, int index) {
		if (this.hasField(index)) {
			builder.append("{");
			I50Field i50Field = I50Factory.i50Fields.get(index);
			Object fieldValue = this.getObjectValue(index);
			@NonNull
			I50Type i50Type = i50Field.getI50Type();
			if (fieldValue instanceof String fieldString && i50Field.getMask() != null) {
				fieldValue = CardUtils.maskCardNumber(fieldString, i50Field.getMask());

			} else if (fieldValue instanceof IsoBinaryData fieldData) {
				byte[] buffer = fieldData.getData();
				int length = buffer.length;
				fieldValue = HexCodec.hexEncode(buffer, 0, length);
				// This is used only for DATE10
			} else if (i50Type == I50Type.DATE10) {
				fieldValue = DateTimeFormatter.ofPattern("MMddHHmmSS").format((LocalDateTime) fieldValue);
			} else if (i50Type == I50Type.DATE4) {
				fieldValue = DateTimeFormatter.ofPattern("MMdd").format((LocalDate) fieldValue);
			} else if (i50Type == I50Type.DATE_EXP) {
				fieldValue = DateTimeFormatter.ofPattern("yyMM").format((YearMonth) fieldValue);
			}
			builder.append("name: ");
			builder.append(i50Field.getName());
			builder.append(", type: ");
			builder.append(i50Field.i50Type.name());
			builder.append(", value: ");
			builder.append(fieldValue.toString());
			builder.append("}");
		}
	}

	public String prettyPrint() {
		AsciiTable headerTable = new AsciiTable();
		headerTable.addRule();
		headerTable.addRow("Field", "Value");
		headerTable.addRule();
		if (null != this.getIsoHeader()) {
			headerTable.addRow("Header", this.getIsoHeader());
			headerTable.addRule();
		}
		headerTable.addRow("Message Type", Integer.toHexString(this.getType()));
		headerTable.addRule();
		for (Entry<String, Object> entry : metadata.entrySet()) {
			headerTable.addRow(entry.getKey(), entry.getValue());
			headerTable.addRule();
		}
		List<Integer> indexes = new ArrayList<>(I50Factory.i50Fields.keySet());
		Collections.sort(indexes);
		AsciiTable bodyTable = printBodyTable(indexes);
		headerTable.getContext().setGridTheme(TA_GridThemes.FULL);
		bodyTable.getContext().setGridTheme(TA_GridThemes.FULL);
		AT_Renderer renderer = AT_Renderer.create();
		renderer.setCWC(new CWC_LongestWordMin(10));
		String rtHeader = renderer.render(headerTable.getRawContent(), headerTable.getColNumber(),
				headerTable.getContext());
		String rtBody = renderer.render(bodyTable.getRawContent(), bodyTable.getColNumber(),
				bodyTable.getContext());
		return rtHeader + "\n" + rtBody;
	}

	private AsciiTable printBodyTable(List<Integer> indexes) {
		AsciiTable bodyTable = new AsciiTable();
		bodyTable.addRule();
		bodyTable.addRow("Field Number", "Name", "Type", "Length", "Value");
		bodyTable.addRule();
		int length = 0;
		for (int i : indexes) {
			if (this.hasField(i)) {
				I50Field i50Field = I50Factory.i50Fields.get(i);
				Object fieldValue = this.getObjectValue(i);
				@NonNull
				I50Type i50Type = i50Field.getI50Type();
				if (fieldValue == null) {
					continue;
				}
				length = fieldValue.toString().length();
				if (fieldValue instanceof String fieldString && i50Field.getMask() != null) {
					fieldValue = CardUtils.maskCardNumber(fieldString, i50Field.getMask());
				} else if (fieldValue instanceof IsoBinaryData fieldbianry) {
					byte[] buffer = fieldbianry.getData();
					length = buffer.length;
					fieldValue = HexCodec.hexEncode(buffer, 0, length);
				} else if (i50Type == I50Type.DATE10) {
					fieldValue = DateTimeFormatter.ofPattern("MMddHHmmSS").format((LocalDateTime) fieldValue);
					length = ((String) fieldValue).length();
				} else if (i50Type == I50Type.DATE4) {
					fieldValue = DateTimeFormatter.ofPattern("MMdd").format((LocalDate) fieldValue);
					length = ((String) fieldValue).length();
				} else if (i50Type == I50Type.DATE_EXP) {
					fieldValue = DateTimeFormatter.ofPattern("yyMM").format((YearMonth) fieldValue);
					length = ((String) fieldValue).length();
				}
				printRow(bodyTable, i, i50Field, length, fieldValue);
			}
		}
		return bodyTable;
	}

	private void printRow(AsciiTable bodyTable, int index, I50Field i50Field, int length, Object fieldValue) {
		String maxLength = "";
		Integer maxLengthValue = i50Field.length;
		if (maxLengthValue != null) {
			maxLength = "(" + maxLengthValue.toString() + ")";
		} else if ((I50Type.ISOTYPEMAP.get(i50Field.i50Type)).getLength() != 0) {
			maxLengthValue = (I50Type.ISOTYPEMAP.get(i50Field.i50Type)).getLength();
			maxLength = "(" + maxLengthValue.toString() + ")";
		}
		bodyTable.addRow(index, i50Field.getName(), i50Field.i50Type.name(), length + maxLength, fieldValue);
		bodyTable.addRule();
	}
}
