package com.ajjpj.acollections.jackson;

import com.ajjpj.acollections.util.AOption;
import com.ajjpj.acollections.*;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.type.*;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Comparator;


/**
 * This 'Module' adds a-collections support to the Jackson library for reading / writing JSON.
 *
 * <p> To use, register this module on an {@link com.fasterxml.jackson.databind.ObjectMapper}:
 * <pre>
 * {@code ObjectMapper om = ...}
 * {@code om.registerModule(new ACollectionsModule());}
 * </pre>
 *
 * That provides the following functionality:
 * <ul>
 *     <li> All implementations of {@link ACollection} are written as JSON arrays.
 *     <li> All top-level implementations of {@link ACollection} except for
 *           {@link com.ajjpj.acollections.immutable.ARange} are read from JSON; sorted collections
 *           use {@link Comparator#naturalOrder()}. Jackson's DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY
 *           is respected, i.e. if it is set to true, a scalar value will be treated as a single element collection.
 *     <li> All implementations of {@link AMap} are written as JSON objects.
 *     <li> All top-level implementations of {@link AMap} are read from JSON; sorted
 *          maps use {@link Comparator#naturalOrder()} as their key ordering.
 *     <li> If a Java property has one of the interface types {@link ACollection}, {@link AList},
 *          {@link ASet}, {@link ASortedSet}, {@link AMap} or {@link ASortedMap}, then the corresponding default
 *          implementation will be used.
 *     <li> {@link AOption} is written and read like {@link java.util.Optional}: "x" in JSON means AOption.of("x"),
 *          null in JSON means AOption.empty(). Empty optional values are written as {@code null} by default, but
 *          Jackson's "inclusion" control is applied: Setting {@code om.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);}
 *          or {@code om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);} causes properties that are
 *          {@link AOption#empty()} to be removed from the JSON output altogether.
 * </ul>
 */
public class ACollectionsModule extends Module {
    @Override public String getModuleName () {
        return getClass().getSimpleName();
    }

    @Override public Version version () {
        return Version.unknownVersion();
    }

    @Override public void setupModule (SetupContext setupContext) {
        setupContext.addDeserializers(new Deserializers());
        setupContext.addSerializers(new Serializers());

        setupContext.addTypeModifier(new ACollectionsTypeModifier());
    }

    static class ACollectionsTypeModifier extends TypeModifier implements Serializable {
        static final long serialVersionUID = 1L;

        @Override public JavaType modifyType(JavaType type, Type jdkType, TypeBindings bindings, TypeFactory typeFactory) {
            if (type.isReferenceType() || type.isContainerType()) {
                return type;
            }

            final Class<?> raw = type.getRawClass();
            if (raw == AOption.class) {
                return ReferenceType.upgradeFrom(type, type.containedTypeOrUnknown(0));
            }
            return type;
        }
    }

}
