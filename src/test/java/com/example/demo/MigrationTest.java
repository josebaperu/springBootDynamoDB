package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MigrationTest extends AbstractBaseTest {
    private static final String TABLE_NAME = "OUTPUT_QC_RECORDS";
    private static final int DEMO_RANGE = 15;

    @Test
    void demoTest() {
        TestUtils.createTable(ddb, TABLE_NAME);
        TestUtils.insertItem(ddb, TABLE_NAME, DEMO_RANGE);
        TestUtils.scanByStatus(ddb, TABLE_NAME, DEMO_RANGE, V1_STATUS.STATUS_1);
        TestUtils.scanByStatus(ddb, TABLE_NAME, DEMO_RANGE, V1_STATUS.STATUS_2);
        TestUtils.scanByStatus(ddb, TABLE_NAME, DEMO_RANGE, V1_STATUS.STATUS_3);
        TestUtils.scanByStatus(ddb, TABLE_NAME, DEMO_RANGE, V1_STATUS.STATUS_4);

    }

    @Test
    void upsertAttributesTest() {
        int range_for_update = 80;
        TestUtils.createTable(ddb, TABLE_NAME); // comment this out when updating against environments
        TestUtils.insertItem(ddb, TABLE_NAME, range_for_update); // comment this out when updating against environments
        TestUtils.scanByStatus(ddb, TABLE_NAME, range_for_update, V1_STATUS.STATUS_1);//
        TestUtils.scanByStatus(ddb, TABLE_NAME, range_for_update, V1_STATUS.STATUS_2);//
        TestUtils.scanByStatus(ddb, TABLE_NAME, range_for_update, V1_STATUS.STATUS_3);//
        TestUtils.scanByStatus(ddb, TABLE_NAME, range_for_update, V1_STATUS.STATUS_4);//
        TestUtils.scanAndUpdate(ddb, TABLE_NAME, range_for_update, V1_STATUS.STATUS_1, V2_STATUS.STATUS_A);
        TestUtils.scanAndUpdate(ddb, TABLE_NAME, range_for_update, V1_STATUS.STATUS_2, V2_STATUS.STATUS_B);
        TestUtils.scanAndUpdate(ddb, TABLE_NAME, range_for_update, V1_STATUS.STATUS_3, V2_STATUS.STATUS_C);
        TestUtils.scanAndUpdate(ddb, TABLE_NAME, range_for_update, V1_STATUS.STATUS_4, V2_STATUS.STATUS_D);



    }

}
