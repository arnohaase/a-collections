package com.ajjpj.acollections.jackson;

import com.ajjpj.acollections.util.AOption;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.ReferenceTypeSerializer;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.util.NameTransformer;


class AOptionSerializer extends ReferenceTypeSerializer<AOption<?>> {

    AOptionSerializer(ReferenceType fullType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> ser) {
        super(fullType, staticTyping, vts, ser);
    }

    private AOptionSerializer(AOptionSerializer base, BeanProperty property,
                             TypeSerializer vts,
                             JsonSerializer<?> valueSer,
                             NameTransformer unwrapper,
                             Object suppressableValue,
                             boolean suppressNulls) {
        super(base, property, vts, valueSer, unwrapper, suppressableValue, suppressNulls);
    }


    @Override protected ReferenceTypeSerializer<AOption<?>> withResolved(BeanProperty prop, TypeSerializer vts, JsonSerializer<?> valueSer, NameTransformer unwrapper) {
        return new AOptionSerializer(this, prop, vts, valueSer, unwrapper, _suppressableValue, _suppressNulls);
    }

    @Override
    public ReferenceTypeSerializer<AOption<?>> withContentInclusion(Object suppressableValue, boolean suppressNulls) {
        return new AOptionSerializer(this, _property, _valueTypeSerializer, _valueSerializer, _unwrapper, suppressableValue, suppressNulls);
    }

    @Override protected boolean _isValuePresent(AOption<?> value) {
        return value.isDefined();
    }

    @Override protected Object _getReferenced(AOption<?> value) {
        return value.get();
    }

    @Override protected Object _getReferencedIfPresent(AOption<?> value) {
        return value.orNull();
    }
}
