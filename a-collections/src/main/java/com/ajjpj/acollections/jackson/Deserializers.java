package com.ajjpj.acollections.jackson;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.immutable.*;
import com.ajjpj.acollections.mutable.*;
import com.ajjpj.acollections.util.AOption;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.ReferenceType;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Supplier;


class Deserializers extends com.fasterxml.jackson.databind.deser.Deserializers.Base implements Serializable  {
    private abstract static class SerSupp<T> implements Supplier<T>, Serializable {
    }

    @Override public JsonDeserializer<?> findReferenceDeserializer (ReferenceType refType, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer contentTypeDeserializer, JsonDeserializer<?> contentDeserializer) {
        if (refType.hasRawClass(AOption.class)) return new AOptionDeserializer(refType, null, contentTypeDeserializer, contentDeserializer);
        return null;
    }

    @Override public JsonDeserializer<?> findMapDeserializer (MapType type,
                                                              DeserializationConfig config,
                                                              BeanDescription beanDesc,
                                                              KeyDeserializer keyDeserializer,
                                                              TypeDeserializer elementTypeDeserializer,
                                                              JsonDeserializer<?> elementDeserializer) {
        final Class<?> raw = type.getRawClass();

        if (AMap.class.isAssignableFrom(raw)) {
            if (AHashMap.class.isAssignableFrom(raw) || raw.equals(AMap.class))
                return new AMapDeserializer(type, keyDeserializer, elementTypeDeserializer, elementDeserializer, new SerSupp<ACollectionBuilder<Map.Entry, ? extends AMap>>() {
                    @Override public ACollectionBuilder<Map.Entry, ? extends AMap> get () {
                        //noinspection unchecked
                        return (ACollectionBuilder) AHashMap.builder();
                    }
                });
            if (ATreeMap.class.isAssignableFrom(raw) || raw.equals(ASortedMap.class))
                return new AMapDeserializer(type, keyDeserializer, elementTypeDeserializer, elementDeserializer, new SerSupp<ACollectionBuilder<Map.Entry, ? extends AMap>>() {
                    @Override public ACollectionBuilder<Map.Entry, ? extends AMap> get () {
                        //noinspection unchecked
                        return (ACollectionBuilder) ATreeMap.builder();
                    }
                });
            if (AMutableMapWrapper.class.isAssignableFrom(raw))
                return new AMapDeserializer(type, keyDeserializer, elementTypeDeserializer, elementDeserializer, new SerSupp<ACollectionBuilder<Map.Entry, ? extends AMap>>() {
                    @Override public ACollectionBuilder<Map.Entry, ? extends AMap> get () {
                        //noinspection unchecked
                        return (ACollectionBuilder) AMutableMapWrapper.builder();
                    }
                });
            if (AMutableSortedMapWrapper.class.isAssignableFrom(raw))
                return new AMapDeserializer(type, keyDeserializer, elementTypeDeserializer, elementDeserializer, new SerSupp<ACollectionBuilder<Map.Entry, ? extends AMap>>() {
                    @Override public ACollectionBuilder<Map.Entry, ? extends AMap> get () {
                        //noinspection unchecked
                        return (ACollectionBuilder) AMutableSortedMapWrapper.builder();
                    }
                });
        }
        return null;
    }

    @Override public JsonDeserializer<?> findCollectionDeserializer(CollectionType type,
                                                                    DeserializationConfig config,
                                                                    BeanDescription beanDesc,
                                                                    TypeDeserializer elementTypeDeserializer,
                                                                    JsonDeserializer<?> elementDeserializer) {
        final Class<?> raw = type.getRawClass();

        if (AOption.class.isAssignableFrom(raw))
            return new AOptionDeserializer(type, null, elementTypeDeserializer, elementDeserializer);

        if (ACollection.class.isAssignableFrom(raw)) {
            if (AVector.class.isAssignableFrom(raw) || raw.equals(AList.class) || raw.equals(ACollection.class))
                return new ACollectionDeserializer(new SerSupp<ACollectionBuilder>() {
                    @Override public ACollectionBuilder get () {
                        return AVector.builder();
                    }
                }, type, elementTypeDeserializer, elementDeserializer);
            if (ALinkedList.class.isAssignableFrom(raw))
                return new ACollectionDeserializer(new SerSupp<ACollectionBuilder>() {
                    @Override public ACollectionBuilder get () {
                        return ALinkedList.builder();
                    }
                }, type, elementTypeDeserializer, elementDeserializer);
            if (AHashSet.class.isAssignableFrom(raw) || raw.equals(ASet.class))
                return new ACollectionDeserializer(new SerSupp<ACollectionBuilder>() {
                    @Override public ACollectionBuilder get () {
                        return AHashSet.builder();
                    }
                }, type, elementTypeDeserializer, elementDeserializer);
            if (ATreeSet.class.isAssignableFrom(raw) || raw.equals(ASortedSet.class))
                return new ACollectionDeserializer(new SerSupp<ACollectionBuilder>() {
                    @Override public ACollectionBuilder get () {
                        return ATreeSet.builder();
                    }
                }, type, elementTypeDeserializer, elementDeserializer);

            if (AMutableArrayWrapper.class.isAssignableFrom(raw))
                return new ACollectionDeserializer(new SerSupp<ACollectionBuilder>() {
                    @Override public ACollectionBuilder get () {
                        return AMutableArrayWrapper.builder();
                    }
                }, type, elementTypeDeserializer, elementDeserializer);
            if (AMutableListWrapper.class.isAssignableFrom(raw))
                return new ACollectionDeserializer(new SerSupp<ACollectionBuilder>() {
                    @Override public ACollectionBuilder get () {
                        return AMutableListWrapper.builder();
                    }
                }, type, elementTypeDeserializer, elementDeserializer);
            if (AMutableSetWrapper.class.isAssignableFrom(raw))
                return new ACollectionDeserializer(new SerSupp<ACollectionBuilder>() {
                    @Override public ACollectionBuilder get () {
                        return AMutableSetWrapper.builder();
                    }
                }, type, elementTypeDeserializer, elementDeserializer);
            if (AMutableSortedSetWrapper.class.isAssignableFrom(raw))
                return new ACollectionDeserializer(new SerSupp<ACollectionBuilder>() {
                    @Override public ACollectionBuilder get () {
                        return AMutableSortedSetWrapper.builder(Comparator.naturalOrder());
                    }
                }, type, elementTypeDeserializer, elementDeserializer);
        }

        return null;
    }
}
