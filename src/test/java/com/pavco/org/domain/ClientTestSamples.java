package com.pavco.org.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Client getClientSample1() {
        return new Client()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .email("email1")
            .ruc("ruc1")
            .businessName("businessName1")
            .description("description1");
    }

    public static Client getClientSample2() {
        return new Client()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .email("email2")
            .ruc("ruc2")
            .businessName("businessName2")
            .description("description2");
    }

    public static Client getClientRandomSampleGenerator() {
        return new Client()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .email(UUID.randomUUID().toString())
            .ruc(UUID.randomUUID().toString())
            .businessName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
