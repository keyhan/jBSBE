package org.mashad.jbsbe.iso;

import java.time.LocalDateTime;

import org.mashad.jbsbe.annotation.AutoStan;
import org.mashad.jbsbe.annotation.Iso8583;
import org.mashad.jbsbe.annotation.IsoField;

import lombok.Builder;

@Iso8583(type=0x200)
@Builder
@AutoStan
public class PurchaseRequest2 {
	@IsoField(index=10)
	public LocalDateTime date;
	@IsoField(index=4)
	public Long amount;
	@IsoField(index=35)
	public String cardNumber;
	
}