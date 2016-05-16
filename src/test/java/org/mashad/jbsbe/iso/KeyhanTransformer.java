package org.mashad.jbsbe.iso;

public class KeyhanTransformer extends SimpleTransformer {
	
	@Override
	public <S> void setField(int id, S value, I50Message message) {
		System.out.println("KEYHAN Transformer!!!");
		message.setField(id, value);

		
	}

}
