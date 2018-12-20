package com.ajjpj.acollections.jackson;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AMap;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.MapType;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Supplier;


class AMapDeserializer extends JsonDeserializer<AMap> implements ContextualDeserializer {
    private final MapType _mapType;
    private final KeyDeserializer _keyDeserializer;
    private final JsonDeserializer<?> _valueDeserializer;
    private final TypeDeserializer _typeDeserializerForValue;

    private final Supplier<ACollectionBuilder<Map.Entry, ? extends AMap>> builderSupplier;

    AMapDeserializer (MapType type, KeyDeserializer keyDeser, TypeDeserializer typeDeser, JsonDeserializer<?> deser, Supplier<ACollectionBuilder<Map.Entry, ? extends AMap>> builderSupplier) {
        _mapType = type;
        _keyDeserializer = keyDeser;
        _typeDeserializerForValue = typeDeser;
        _valueDeserializer = deser;
        this.builderSupplier = builderSupplier;
    }

    @Override public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        KeyDeserializer keyDeser = _keyDeserializer;
        JsonDeserializer<?> deser = _valueDeserializer;
        TypeDeserializer typeDeser = _typeDeserializerForValue;

        if (keyDeser == null) {
            keyDeser = ctxt.findKeyDeserializer(_mapType.getKeyType(), property);
        }
        if (deser == null) {
            deser = ctxt.findContextualValueDeserializer(_mapType.getContentType(), property);
        }
        if (typeDeser != null) {
            typeDeser = typeDeser.forProperty(property);
        }
        return new AMapDeserializer(_mapType, keyDeser, typeDeser, deser, builderSupplier);
    }

    @Override public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        // note: call "...FromObject" because expected output structure
        // for value is JSON Object (regardless of contortions used for type id)
        return typeDeserializer.deserializeTypedFromObject(p, ctxt);
    }

    @Override public AMap deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken t = p.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            // This ambiguity is due to Jackson's way of including type information in a JSON object as the first 'property'. It
            //  is read and handled by the framework, leaving the actual deserializer with a JsonParser pointing either to the
            //  object start or the first 'real' property. Moving from object start to the next token is the recommended way of
            //  normalizing and handling both cases without distinguishing between them.
            t = p.nextToken();
        }
        if (t != JsonToken.FIELD_NAME && t != JsonToken.END_OBJECT) {
            return (AMap) ctxt.handleUnexpectedToken(_mapType.getRawClass(), p);
        }
        return _deserializeEntries(p, ctxt);
    }

    private AMap _deserializeEntries(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException
    {
        final KeyDeserializer keyDes = _keyDeserializer;
        final JsonDeserializer<?> valueDes = _valueDeserializer;
        final TypeDeserializer typeDeser = _typeDeserializerForValue;

        final ACollectionBuilder<Map.Entry, ? extends AMap> builder = builderSupplier.get();
        for (; p.getCurrentToken() == JsonToken.FIELD_NAME; p.nextToken()) {
            final String fieldName = p.getCurrentName();
            final Object key = (keyDes == null) ? fieldName : keyDes.deserializeKey(fieldName, ctxt);

            p.nextToken();
            final Object value = typeDeser == null ? valueDes.deserialize(p, ctxt) : valueDes.deserializeWithType(p, ctxt, typeDeser);
            builder.add(new AbstractMap.SimpleImmutableEntry<>(key, value));
        }

        return builder.build();
    }
}
