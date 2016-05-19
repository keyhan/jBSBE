package org.mashad.jbsbe.iso;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.mashad.jbsbe.annotation.AutoStan;
import org.mashad.jbsbe.annotation.Iso8583;
import org.mashad.jbsbe.annotation.IsoField;

import com.solab.iso8583.MessageFactory;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;


public class I50Factory<T extends SimpleTransformer> extends MessageFactory<I50Message> {

	@Override
	protected I50Message createIsoMessage(String header) {
		return new I50Message(header);
	}

	public I50Factory(Class<T> clazz) throws InstantiationException, IllegalAccessException {
		this.transformer = clazz.newInstance();
	}

	private T transformer = null;

	private I50Message setupClass(final @NonNull Object instance) {
		I50Message message = null;
		Annotation annotation = instance.getClass().getAnnotation(Iso8583.class);
		if (annotation instanceof Iso8583) {
			Iso8583 iso8583 = (Iso8583) annotation;
			message = newMessage(iso8583.type());
			message.setBinary(iso8583.binary());
			if (StringUtils.isNotBlank(iso8583.header())) {
				message.setIsoHeader(iso8583.header());
			}
		}
		return message;
	}

	private Field[] getAllFields(final @NonNull Object instance) {
		return instance.getClass().getDeclaredFields();
	}

	private boolean requiresCustomTransformer(final Object instance) {
		Map<Integer, Field> fields = getAllIsoFields(instance);
		IsoField isoField;
		for (Field field : fields.values()) {
			isoField = field.getAnnotation(IsoField.class);
			if (isoField.simpleMapping() == false) {
				return true;
			}
		}
		return false;
	}

	private Map<Integer, Field> getAllIsoFields(final Object instance) {
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

	public I50Message newMessage(final @NonNull Object instance)
			throws IllegalArgumentException, IllegalAccessException {
		@NonNull
		I50Message message = setupClass(instance);
		
		Annotation stanAnnotation = instance.getClass().getAnnotation(AutoStan.class);
		if(null != stanAnnotation) {
			message.setStan();
		}
		
		if(requiresCustomTransformer(instance) && transformer.getClass() == SimpleTransformer.class) {
			throw new IllegalArgumentException("One or several of your fields require custom transformer");
		}

		Map<Integer, Field> isoFields = getAllIsoFields(instance);
		Field field;
		for (int i = 3; i < 129; i++) {
			field = isoFields.get(i);
			if (null != field) {
				Object value = field.get(instance);
				transformer.setField(i, value, message);
			}
		}
		return message;
	}

	public static Map<Integer, I50Field> i50Fields = new HashMap<>();

	@ToString
	public static class I50Field {
		Integer length;

		@NonNull
		I50Type i50Type;

		private String name;

		private String mask;

		public String getMask() {
			return mask;
		}

		public String getName() {
			return name;
		}

		public I50Field(String name, I50Type isoType, int length, String mask) {
			this.i50Type = isoType;
			this.length = length;
			this.name = name;
			this.mask = mask;
		}

		public I50Field(String name, I50Type isoType, int length) {
			this.i50Type = isoType;
			this.length = length;
			this.name = name;
		}

		public I50Field(String name, I50Type isoType) {
			this.i50Type = isoType;
			this.name = name;
		}

		public I50Field(String name, I50Type isoType, String mask) {
			this.i50Type = isoType;
			this.name = name;
			this.mask = mask;
		}

	}

	public static void addField(int index, String name, I50Type isoType) {
		addField(index, name, isoType, 0, null);
	}

	public static void addField(int index, String name, I50Type isoType, String mask) {
		addField(index, name, isoType, 0, mask);
	}

	public static void addField(int index, String name, I50Type isoType, int length) {
		addField(index, name, isoType, length, null);
	}

	public static void addField(int index, String name, I50Type isoType, int length, String mask) {
		switch (isoType) {
		case ALPHA:
		case BINARY:
		case I50Binary:
		case NUMERIC:
			if (length < 1) {
				throw new IllegalArgumentException("length is not set correctly");
			}
			if (null != mask) {
				I50Factory.i50Fields.put(index, new I50Factory.I50Field(name, isoType, length, mask));
			} else {
				I50Factory.i50Fields.put(index, new I50Factory.I50Field(name, isoType, length));
			}
			break;

		default:
			if (null != mask) {
				I50Factory.i50Fields.put(index, new I50Factory.I50Field(name, isoType, mask));
			} else {
				I50Factory.i50Fields.put(index, new I50Factory.I50Field(name, isoType));
			}
			break;
		}
	}

}
