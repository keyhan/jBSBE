package io.github.keyhan.jbsbe.iso;

import io.github.keyhan.jbsbe.iso.I50Message;
import io.github.keyhan.jbsbe.iso.SimpleTransformer;

public class KeyhanTransformer extends SimpleTransformer {
	
	@Override
	public <S> void setField(int id, S value, I50Message message) {
		System.out.println("KEYHAN Transformer!!!");
		message.setField(id, value);

		
	}

}
