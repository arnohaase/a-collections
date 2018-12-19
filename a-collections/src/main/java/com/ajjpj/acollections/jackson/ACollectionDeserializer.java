package com.ajjpj.acollections.jackson;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.ACollectionBuilder;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.function.Supplier;


class ACollectionDeserializer extends StdDeserializer<ACollection> implements ContextualDeserializer {
    private final Supplier<ACollectionBuilder> builderSupplier;
    private final JsonDeserializer<?> valueDeser;
    private final TypeDeserializer typeDeser;
    private final CollectionType containerType;

    ACollectionDeserializer (Supplier<ACollectionBuilder> builderSupplier, CollectionType type, TypeDeserializer typeDeser, JsonDeserializer<?> deser) {
        super(type);
        this.builderSupplier = builderSupplier;
        this.containerType = type;
        this.typeDeser = typeDeser;
        this.valueDeser = deser;
    }

    @Override public JsonDeserializer<?> createContextual (DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<?> deser = valueDeser;
        TypeDeserializer typeDeser = this.typeDeser;
        if (deser == null) {
            deser = ctxt.findContextualValueDeserializer(containerType.getContentType(), property);
        }
        if (typeDeser != null) {
            typeDeser = typeDeser.forProperty(property);
        }
        if (deser == valueDeser && typeDeser == this.typeDeser) {
            return this;
        }
        return new ACollectionDeserializer(builderSupplier, containerType, typeDeser, deser);
    }

    @Override public Boolean supportsUpdate (DeserializationConfig config) {
        return false;
    }

    @Override public Object deserializeWithType (JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromArray(p, ctxt);
    }

    @Override public ACollection deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Should usually point to START_ARRAY
        if (p.isExpectedStartArrayToken()) {
            return deserializeFromArray(p, ctxt);
        }
        // But may support implicit arrays from single values?
        if (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            return deserializeFromSingleValue(p, ctxt);
        }

        //noinspection unchecked
        return (ACollection) ctxt.handleUnexpectedToken(_valueClass, p);
    }

    private ACollectionBuilder builder() {
        return builderSupplier.get();
    }

    private ACollection deserializeFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
        final ACollectionBuilder builder = builder();

        JsonToken t;
        while ((t = p.nextToken()) != JsonToken.END_ARRAY) {
            //noinspection unchecked
            builder.add(deserElement(t, p, ctxt));
        }
        return (ACollection) builder.build();
    }

    private ACollection deserializeFromSingleValue (JsonParser p, DeserializationContext ctxt) throws IOException {
        //noinspection unchecked
        return (ACollection)builder()
                .add(deserElement(p.getCurrentToken(), p, ctxt))
                .build();
    }

    private Object deserElement(JsonToken t, JsonParser p, DeserializationContext ctxt) throws IOException {
        if (t == JsonToken.VALUE_NULL) {
            return null;
        }
        else if (typeDeser == null) {
            return valueDeser.deserialize(p, ctxt);
        }
        else {
            return valueDeser.deserializeWithType(p, ctxt, typeDeser);
        }
    }
}
