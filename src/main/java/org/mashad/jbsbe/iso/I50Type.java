package org.mashad.jbsbe.iso;

import com.solab.iso8583.IsoType;

public enum I50Type {
	BINARY//
	, ALPHA//
	, AMOUNT//
	, NUMERIC//
	, DATE4//
	, TIME//
	, DATE10//
	, DATE_EXP//
	, LLBIN//
	, LLLBIN//
	, LLLLBIN//
	, LLVAR//
	, LLLVAR//
	, LLLLVAR//

	, I50Binary//
	, I50LLLBIN//

	;

	public static IsoType getIsoType(I50Type type) {
		switch (type) {
		case BINARY:
			return IsoType.BINARY;
		case ALPHA:
			return IsoType.ALPHA;
		case AMOUNT:
			return IsoType.AMOUNT;
		case NUMERIC:
			return IsoType.NUMERIC;
		case DATE4:
			return IsoType.DATE4;
		case DATE10:
			return IsoType.DATE10;
		case TIME:
			return IsoType.TIME;
		case DATE_EXP:
			return IsoType.DATE_EXP;
		case LLBIN:
			return IsoType.LLBIN;
		case LLLBIN:
			return IsoType.LLLBIN;
		case LLLLBIN:
			return IsoType.LLLLBIN;
		case LLVAR:
			return IsoType.LLVAR;
		case LLLVAR:
			return IsoType.LLLVAR;
		case LLLLVAR:
			return IsoType.LLLLVAR;

		default:
			return null;
		}
	}
}
