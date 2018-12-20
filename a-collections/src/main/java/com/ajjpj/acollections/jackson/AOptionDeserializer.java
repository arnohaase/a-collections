package com.ajjpj.acollections.jackson;

import com.ajjpj.acollections.util.AOption;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.ReferenceTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;


class AOptionDeserializer extends ReferenceTypeDeserializer<AOption> {
    public AOptionDeserializer (JavaType fullType, ValueInstantiator vi, TypeDeserializer typeDeser, JsonDeserializer<?> deser) {
        super(fullType, vi, typeDeser, deser);
    }

    @Override protected ReferenceTypeDeserializer<AOption> withResolved (TypeDeserializer typeDeser, JsonDeserializer<?> valueDeser) {
        return new AOptionDeserializer(_fullType, _valueInstantiator, typeDeser, valueDeser);
    }

    @Override public AOption getNullValue (DeserializationContext ctxt) {
        return AOption.none();
    }

    @Override public AOption referenceValue (Object contents) {
        return AOption.of(contents);
    }

    @Override public AOption updateReference (AOption reference, Object contents) {
        return AOption.of(contents);
    }

    @Override public Object getReferenced (AOption reference) {
        return reference.get();
    }
}
