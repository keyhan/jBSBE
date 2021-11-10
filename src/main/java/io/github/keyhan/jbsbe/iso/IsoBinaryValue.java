package io.github.keyhan.jbsbe.iso;

import java.io.IOException;
import java.io.OutputStream;

import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;

public class IsoBinaryValue extends IsoValue<IsoBinaryData> {

    private int length;

    public IsoBinaryValue(IsoType t, IsoBinaryData val, int len) {
        super(t, val, len);
        length = len;
    }

    @Override
    public void write(final OutputStream outs, final boolean binary, final boolean forceStringEncoding)
            throws IOException {
        final IsoBinaryData value = getValue();
        outs.write(value.getData());
    }

    @Override
    public String toString() {
        return new String(new char[length]).replace('\0', ' ');
    }

}
