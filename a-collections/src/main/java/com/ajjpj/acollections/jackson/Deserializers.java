package com.ajjpj.acollections.jackson;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.ALinkedList;
import com.ajjpj.acollections.immutable.ATreeSet;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.mutable.AMutableArrayWrapper;
import com.ajjpj.acollections.mutable.AMutableListWrapper;
import com.ajjpj.acollections.mutable.AMutableSetWrapper;
import com.ajjpj.acollections.mutable.AMutableSortedSetWrapper;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.Serializable;
import java.util.Comparator;
import java.util.function.Supplier;


class Deserializers extends com.fasterxml.jackson.databind.deser.Deserializers.Base implements Serializable  {
    private abstract static class SerSupp<T> implements Supplier<T>, Serializable {
    }

    @Override public JsonDeserializer<?> findCollectionDeserializer(CollectionType type,
                                                                    DeserializationConfig config,
                                                                    BeanDescription beanDesc,
                                                                    TypeDeserializer elementTypeDeserializer,
                                                                    JsonDeserializer<?> elementDeserializer) {
        Class<?> raw = type.getRawClass();

        if (ACollection.class.isAssignableFrom(raw)) {
            if (AVector.class.isAssignableFrom(raw))
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
            if (AHashSet.class.isAssignableFrom(raw))
                return new ACollectionDeserializer(new SerSupp<ACollectionBuilder>() {
                    @Override public ACollectionBuilder get () {
                        return AHashSet.builder();
                    }
                }, type, elementTypeDeserializer, elementDeserializer);
            if (ATreeSet.class.isAssignableFrom(raw))
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
