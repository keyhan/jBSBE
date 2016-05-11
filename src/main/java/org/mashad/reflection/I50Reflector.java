package org.mashad.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.mashad.jbsbe.annotation.Iso8583;
import org.mashad.jbsbe.annotation.IsoField;
import org.mashad.jbsbe.iso.I50Message;

public class I50Reflector {

	public I50Reflector(I50Transformer<I50Message> transformer) {
		this.transformer = transformer;
	}

	private I50Message message;

	private I50Transformer<I50Message> transformer;

	private boolean setupClass(final Object instance) {
		Annotation annotation = instance.getClass().getAnnotation(Iso8583.class);
		if (annotation instanceof Iso8583) {
			Iso8583 iso8583 = (Iso8583) annotation;
			message = new I50Message();
			message.setType(iso8583.type());
			message.setBinary(iso8583.binary());
			if (StringUtils.isNotBlank(iso8583.header())) {
				message.setIsoHeader(iso8583.header());
			}
			// System.out.println("TYPE = " + iso8583.type());
			// System.out.println("HEADER = " + iso8583.header());
			return true;
		}
		return false;
	}

	private Field[] getAllFields(final Object instance) {
		return instance.getClass().getDeclaredFields();
	}

	private Map<Integer,Field> getAllIsoFields(final Object instance) {
		Field[] fields = getAllFields(instance);
		Map<Integer, Field> result = new HashMap<>();
		IsoField isoField;
		for (Field field : fields) {
			isoField = field.getAnnotation(IsoField.class);
			if (null != isoField) {
				result.put(isoField.index(), field);
			}
		}
		return result;

	}

	private List<Field> getMappedIsoFields(final Object instance) {
		Field[] fields = getAllFields(instance);
		List<Field> result = new ArrayList<>();
		IsoField isoField;
		for (Field field : fields) {
			isoField = field.getAnnotation(IsoField.class);
			if (null != isoField && isoField.simpleMapping()) {
				result.add(field);
			}
		}
		return result;
	}

	private List<Field> getUnmappedIsoFields(final Object instance) {
		Field[] fields = getAllFields(instance);
		List<Field> result = new ArrayList<>();
		IsoField isoField;
		for (Field field : fields) {
			isoField = field.getAnnotation(IsoField.class);
			if (null != isoField && !isoField.simpleMapping()) {
				result.add(field);
			}
		}
		return result;
	}

	public boolean setupField(Field field) throws IllegalArgumentException {
		boolean result = false;
		IsoField isoField = field.getAnnotation(IsoField.class);
		if (null != isoField) {
			System.out.println("LENGTH = " + isoField.simpleMapping());
			result = true;
		}
		return result;
	}

	public I50Message createI50Message(final Object instance) throws IllegalArgumentException, IllegalAccessException {
		setupClass(instance);
		Map<Integer,Field> isoFields = getAllIsoFields(instance);
		Field field;
		for (int i = 3; i < 129; i++) {
			field = isoFields.get(i);
			IsoField isoField;
			if (null != field) {
				isoField = field.getAnnotation(IsoField.class);
				Object value = field.get(instance);
				if (isoField.simpleMapping()) {
					message.setField(i, value);
				} else {
					transformer.setField(i, value, message);
				}
			}
		}
		return message;
	}

}
