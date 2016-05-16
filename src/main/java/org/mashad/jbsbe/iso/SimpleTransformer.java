package org.mashad.jbsbe.iso;

public class SimpleTransformer {


	public <S> void setField(int id, S value, I50Message message) {
		message.setField(id, value);

		
	}



}
