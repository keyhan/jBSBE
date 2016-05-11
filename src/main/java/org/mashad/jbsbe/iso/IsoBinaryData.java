package org.mashad.jbsbe.iso;

public class IsoBinaryData {

	public IsoBinaryData(final byte[] data) {
		setData(data);
	}

	public IsoBinaryData() {
		//
	}

	private byte[] data;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		byte[] copy = {};
		if (null != data) {
			copy = new byte[data.length];
			System.arraycopy(data, 0, copy, 0, data.length);
		}
		this.data = copy;
	}

	@Override
	public String toString() {
		return new String(data);
	}
}
