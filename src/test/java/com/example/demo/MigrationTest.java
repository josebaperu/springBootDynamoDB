package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MigrationTest extends AbstractBaseTest {
    private static final String TABLE_NAME = "OUTPUT_QC_RECORDS";

    @Test
    void demoTest() {
        int DEMO_RANGE = 15;
        TestUtils.createTable(TABLE_NAME);
        TestUtils.insertItems(TABLE_NAME, DEMO_RANGE);
        TestUtils.scanByStatus(TABLE_NAME, DEMO_RANGE, V1_STATUS.STATUS_1);
        TestUtils.scanByStatus(TABLE_NAME, DEMO_RANGE, V1_STATUS.STATUS_2);
        TestUtils.scanByStatus(TABLE_NAME, DEMO_RANGE, V1_STATUS.STATUS_3);
        TestUtils.scanByStatus(TABLE_NAME, DEMO_RANGE, V1_STATUS.STATUS_4);
    }

    @Test
    void upsertAttributesTest() {
        int UPDATE_RANGE = 80;
        TestUtils.createTable(TABLE_NAME); // comment this out when updating against environments
        TestUtils.insertItems(TABLE_NAME, UPDATE_RANGE); // comment this out when updating against environments
        TestUtils.scanByStatus(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_1);//
        TestUtils.scanByStatus(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_2);//
        TestUtils.scanByStatus(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_3);//
        TestUtils.scanByStatus(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_4);//
        TestUtils.scanAndUpdate(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_1, V2_STATUS.STATUS_A);
        TestUtils.scanAndUpdate(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_2, V2_STATUS.STATUS_B);
        TestUtils.scanAndUpdate(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_3, V2_STATUS.STATUS_C);
        TestUtils.scanAndUpdate(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_4, V2_STATUS.STATUS_D);
    }
}
