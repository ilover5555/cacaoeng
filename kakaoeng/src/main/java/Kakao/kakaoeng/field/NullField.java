package Kakao.kakaoeng.field;

public class NullField<T> extends Field<T> {

	public NullField(String fieldName, T value) {
		super(fieldName, null);
	}

	@Override
	public T getValue() {
		return null;
	}

	@Override
	public void setValue(T value) {
		throw new IllegalAccessError("NullField cant' set value");
	}

	@Override
	public void setFieldName(String fieldName) {
		throw new IllegalAccessError("NullField cant' set field name");
	}
	
}
