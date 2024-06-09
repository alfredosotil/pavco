package com.pavco.org.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BillTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Bill getBillSample1() {
        return new Bill().id(1L).uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).code("code1").notes("notes1");
    }

    public static Bill getBillSample2() {
        return new Bill().id(2L).uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).code("code2").notes("notes2");
    }

    public static Bill getBillRandomSampleGenerator() {
        return new Bill()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .code(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
