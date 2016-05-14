package org.mashad.jbsbe.iso;

import java.util.Date;

import org.mashad.jbsbe.annotation.Iso8583;
import org.mashad.jbsbe.annotation.IsoField;

import lombok.Builder;

@Iso8583(type=0x200)
@Builder
public class PurchaseRequest {
	@IsoField(index=10)
	public Date date;
	@IsoField(index=4)
	public Long amount;
	@IsoField(index=11)
	public Integer stan;
	@IsoField(index=35)
	public String cardNumber;
	
}
