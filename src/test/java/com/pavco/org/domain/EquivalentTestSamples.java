package com.pavco.org.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EquivalentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Equivalent getEquivalentSample1() {
        return new Equivalent().id(1L).uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).code("code1");
    }

    public static Equivalent getEquivalentSample2() {
        return new Equivalent().id(2L).uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).code("code2");
    }

    public static Equivalent getEquivalentRandomSampleGenerator() {
        return new Equivalent().id(longCount.incrementAndGet()).uuid(UUID.randomUUID()).code(UUID.randomUUID().toString());
    }
}
