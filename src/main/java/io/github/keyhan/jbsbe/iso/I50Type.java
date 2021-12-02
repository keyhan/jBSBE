package io.github.keyhan.jbsbe.iso;

import java.util.AbstractMap;
import java.util.Map;

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
	, I50BINARY//
	, I50LLLBIN//
	, I50LLBIN//
	, I50LLLLBIN//
	;

	public static final Map<I50Type, IsoType> ISOTYPEMAP = Map.ofEntries(
		new AbstractMap.SimpleEntry<I50Type, IsoType>(BINARY, IsoType.BINARY),
		new AbstractMap.SimpleEntry<I50Type, IsoType>(ALPHA, IsoType.ALPHA),
		new AbstractMap.SimpleEntry<I50Type, IsoType>(AMOUNT, IsoType.AMOUNT),
		new AbstractMap.SimpleEntry<I50Type, IsoType>(NUMERIC, IsoType.NUMERIC),
		new AbstractMap.SimpleEntry<I50Type, IsoType>(DATE4, IsoType.DATE4),
		new AbstractMap.SimpleEntry<I50Type, IsoType>(DATE10, IsoType.DATE10),
		new AbstractMap.SimpleEntry<I50Type, IsoType>(TIME, IsoType.TIME),
		new AbstractMap.SimpleEntry<I50Type, IsoType>(DATE_EXP, IsoType.DATE_EXP),
		new AbstractMap.SimpleEntry<I50Type, IsoType>(LLBIN, IsoType.LLBIN),
		new AbstractMap.SimpleEntry<I50Type, IsoType>(LLLBIN, IsoType.LLLBIN),
		new AbstractMap.SimpleEntry<I50Type, IsoType>(LLLLBIN, IsoType.LLLLBIN),
		new AbstractMap.SimpleEntry<I50Type, IsoType>(LLVAR, IsoType.LLVAR),
		new AbstractMap.SimpleEntry<I50Type, IsoType>(LLLVAR, IsoType.LLLVAR),
		new AbstractMap.SimpleEntry<I50Type, IsoType>(LLLLVAR, IsoType.LLLLVAR));
}
