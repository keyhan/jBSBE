package io.github.keyhan.jbsbe.iso;

import java.time.LocalDateTime;

import io.github.keyhan.jbsbe.annotation.AutoStan;
import io.github.keyhan.jbsbe.annotation.Iso8583;
import io.github.keyhan.jbsbe.annotation.IsoField;
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