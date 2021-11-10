package io.github.keyhan.jbsbe.iso;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;

import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;

public class LLLLBinaryValue extends IsoValue<IsoBinaryData> {
	
    public LLLLBinaryValue(IsoType t, IsoBinaryData val) {
        super(t, val);
    }
    
    @Override
    public void write(final OutputStream outs, final boolean binary, final boolean forceStringEncoding)
            throws IOException {
        byte[] bytes = getValue().getData();
        String str = StringUtils.leftPad(String.valueOf(bytes.length), 4, '0');
        byte[] dest = new byte[bytes.length + str.getBytes().length];

        System.arraycopy(str.getBytes(), 0, dest, 0, str.getBytes().length);
        System.arraycopy(bytes, 0, dest, str.getBytes().length, bytes.length);
        outs.write(dest);
    }

    @Override
    public String toString() {
        return new String(new char[this.getValue().getData().length]).replace('\0', ' ');
    }

}
