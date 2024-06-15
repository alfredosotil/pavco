package com.pavco.org.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BillDetailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BillDetail getBillDetailSample1() {
        return new BillDetail()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .code("code1")
            .description("description1")
            .quantity(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static BillDetail getBillDetailSample2() {
        return new BillDetail()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .code("code2")
            .description("description2")
            .quantity(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static BillDetail getBillDetailRandomSampleGenerator() {
        return new BillDetail()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .code(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
