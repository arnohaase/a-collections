package com.ajjpj.acollections.jackson;

import com.ajjpj.acollections.util.AOption;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.ReferenceType;


class Serializers extends com.fasterxml.jackson.databind.ser.Serializers.Base {
    @Override public JsonSerializer<?> findReferenceSerializer (SerializationConfig config, ReferenceType type, BeanDescription beanDesc, TypeSerializer contentTypeSerializer, JsonSerializer<Object> contentValueSerializer) {
        final Class<?> raw = type.getRawClass();
        if (AOption.class.isAssignableFrom(raw)) {
            boolean staticTyping = (contentTypeSerializer == null) && config.isEnabled(MapperFeature.USE_STATIC_TYPING);
            return new AOptionSerializer(type, staticTyping, contentTypeSerializer, contentValueSerializer);
        }
        return null;
    }
}
