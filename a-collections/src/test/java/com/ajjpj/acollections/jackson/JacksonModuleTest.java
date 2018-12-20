package com.ajjpj.acollections.jackson;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.immutable.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.annotation.RetentionPolicy;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Regular serialization / deserialization tests are in the individual collections' tests;
 *  this is the place for special cases
 */
public class JacksonModuleTest {
    @Test void testAListFromJson() throws IOException {
        testCollectionFromJson(AList.class, AVector.class, AVector.of(1), AVector.of(1, 2, 3));
        testCollectionFromJsonSingleValue(AList.class, AVector.class, AVector.of(1), AVector.of(1, 2, 3));

        final ObjectMapper om = new ObjectMapper();
        om.registerModule(new ACollectionsModule());

        final PersonList pl = om.readValue("{\"p\":[{\"name\":\"p1\"},{\"name\":\"p2\"}]}", PersonList.class);
        assertEquals(2, pl.p.size());
        assertEquals("p1", pl.p.get(0).name);
        assertEquals("p2", pl.p.get(1).name);
    }
    @Test void testACollectionFromJson() throws IOException {
        testCollectionFromJson(ACollection.class, AVector.class, AVector.of(1), AVector.of(1, 2, 3));
        testCollectionFromJsonSingleValue(ACollection.class, AVector.class, AVector.of(1), AVector.of(1, 2, 3));
    }
    @Test void testASetFromJson() throws IOException {
        testCollectionFromJson(ASet.class, AHashSet.class, ASet.of(1), ASet.of(1, 2, 3));
        testCollectionFromJsonSingleValue(ASet.class, AHashSet.class, ASet.of(1), ASet.of(1, 2, 3));

        final ObjectMapper om = new ObjectMapper();
        om.registerModule(new ACollectionsModule());

        final PersonSet ps = om.readValue("{\"p\":[{\"name\":\"p1\"},{\"name\":\"p2\"}]}", PersonSet.class);
        assertEquals(2, ps.p.size());
        assertTrue(ps.p.exists(x -> x.name.equals("p1")));
        assertTrue(ps.p.exists(x -> x.name.equals("p2")));
    }
    @Test void testASortedSetFromJson() throws IOException {
        testCollectionFromJson(ASortedSet.class, ATreeSet.class, ASortedSet.of(1), ASortedSet.of(1, 2, 3));
        testCollectionFromJsonSingleValue(ASortedSet.class, ATreeSet.class, ASortedSet.of(1), ASortedSet.of(1, 2, 3));
    } 

    @Test void AMapFromJson() throws IOException {
        testMapFromJson(AMap.class, AHashMap.class, AHashMap.of(1, 3), AHashMap.of(1, 3, 2, 5, 3, 7));

        final ObjectMapper om = new ObjectMapper();
        om.registerModule(new ACollectionsModule());

        final PersonMap pm = om.readValue("{\"p\":{\"a\":{\"name\":\"p1\"},\"b\":{\"name\":\"p2\"}}}", PersonMap.class);
        assertEquals(2, pm.p.size());
        assertEquals("p1", pm.p.get("a").name);
        assertEquals("p2", pm.p.get("b").name);
    }
    @Test void AMapWithNonStringKeys() throws IOException {
        final ObjectMapper om = new ObjectMapper();
        om.registerModule(new ACollectionsModule());

        final MapWithEnumKeys mwe = om.readValue("{\"m\":{\"SOURCE\":\"a\",\"CLASS\":\"b\"}}", MapWithEnumKeys.class);
        assertEquals(2, mwe.m.size());
        assertEquals("a", mwe.m.get(RetentionPolicy.SOURCE));
        assertEquals("b", mwe.m.get(RetentionPolicy.CLASS));
    }
    @Test void ASortedMapFromJson() throws IOException {
        testMapFromJson(ASortedMap.class, ATreeMap.class, AHashMap.of(1, 3), AHashMap.of(1, 3, 2, 5, 3, 7));
    }

    public static void testCollectionFromJson (Class<? extends ACollection> staticClass, Class<? extends ACollection> expectedClass, ACollection<Integer> coll1, ACollection<Integer> coll123) throws IOException {
        final ObjectMapper om = new ObjectMapper();
        om.registerModule(new ACollectionsModule());

        assertTrue(expectedClass.isAssignableFrom(om.readValue("[]", staticClass).getClass()));
        assertTrue(om.readValue("[]", staticClass).isEmpty());

        assertTrue(expectedClass.isAssignableFrom(om.readValue("[1]", staticClass).getClass()));
        assertEquals(coll1, om.readValue("[1]", staticClass));

        assertTrue(expectedClass.isAssignableFrom(om.readValue("[1,2,3]", staticClass).getClass()));
        assertEquals(coll123, om.readValue("[1,2,3]", staticClass));

        assertThrows(MismatchedInputException.class, () -> om.readValue("1", staticClass));
    }

    public static void testCollectionFromJsonSingleValue (Class<? extends ACollection> staticClass, Class<? extends ACollection> expectedClass, ACollection<Integer> coll1, ACollection<Integer> coll123) throws IOException {
        final ObjectMapper om = new ObjectMapper();
        om.registerModule(new ACollectionsModule());
        om.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        assertTrue(expectedClass.isAssignableFrom(om.readValue("[]", staticClass).getClass()));
        assertTrue(om.readValue("[]", staticClass).isEmpty());

        assertTrue(expectedClass.isAssignableFrom(om.readValue("[1]", staticClass).getClass()));
        assertEquals(coll1, om.readValue("[1]", staticClass));

        assertTrue(expectedClass.isAssignableFrom(om.readValue("[1,2,3]", staticClass).getClass()));
        assertEquals(coll123, om.readValue("[1,2,3]", staticClass));

        assertTrue(expectedClass.isAssignableFrom(om.readValue("1", staticClass).getClass()));
        assertEquals(coll1, om.readValue("1", staticClass));
    }

    public static void testMapFromJson (Class<? extends AMap> staticClass, Class<? extends AMap> expectedClass, AMap coll1, AMap coll123) throws IOException {
        final ObjectMapper om = new ObjectMapper();
        om.registerModule(new ACollectionsModule());

        final Function<Map.Entry<Integer,Integer>, Map.Entry<String,Integer>> f = e -> new AbstractMap.SimpleImmutableEntry<>(e.getKey().toString(), e.getValue());

        assertTrue(expectedClass.isAssignableFrom(om.readValue("{}", staticClass).getClass()));
        assertTrue(om.readValue("{}", staticClass).isEmpty());

        assertTrue(expectedClass.isAssignableFrom(om.readValue("{\"1\":3}", staticClass).getClass()));
        //noinspection unchecked
        assertEquals(coll1.map(f).toMap(), om.readValue("{\"1\":3}", staticClass));

        assertTrue(expectedClass.isAssignableFrom(om.readValue("{\"1\":3,\"2\":5,\"3\":7}", staticClass).getClass()));
        //noinspection unchecked
        assertEquals(coll123.map(f).toMap(), om.readValue("{\"1\":3,\"2\":5,\"3\":7}", staticClass));

        assertEquals (AMap.of("a", null), om.readValue("{\"a\":null}", staticClass));
    }

    public static class TestPerson {
        public String name;
    }
    public static class PersonList {
        public AList<TestPerson> p;
    }
    public static class PersonSet {
        public ASet<TestPerson> p;
    }
    public static class PersonMap {
        public AMap<String, TestPerson> p;
    }

    public static class MapWithEnumKeys {
        public AMap<RetentionPolicy,String> m;
    }
}
