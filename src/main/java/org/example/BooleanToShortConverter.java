package org.example;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)//not for Hibernate Native
public class BooleanToShortConverter implements AttributeConverter<Boolean, Short> {
    @Override
    public Short convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute ? (short) 1 : (short) 0;
    }
    @Override
    public Boolean convertToEntityAttribute(Short value) {
        if(value == null){return  null;}
        return value == 1;
    }

}
