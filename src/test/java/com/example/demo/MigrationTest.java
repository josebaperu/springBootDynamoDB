package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static com.example.demo.TestUtils.createTable;
import static com.example.demo.TestUtils.insertItems;
import static com.example.demo.TestUtils.scanByStatus;
import static com.example.demo.TestUtils.scanAndUpdate;

@SpringBootTest
class MigrationTest extends AbstractBaseTest {
    private static final String TABLE_NAME = "OUTPUT_QC_RECORDS";

    @Test
    void demoTest() {
        int DEMO_RANGE = 15;
        createTable(TABLE_NAME);
        insertItems(TABLE_NAME, DEMO_RANGE);
        scanByStatus(TABLE_NAME, DEMO_RANGE, V1_STATUS.STATUS_1);
        scanByStatus(TABLE_NAME, DEMO_RANGE, V1_STATUS.STATUS_2);
        scanByStatus(TABLE_NAME, DEMO_RANGE, V1_STATUS.STATUS_3);
        scanByStatus(TABLE_NAME, DEMO_RANGE, V1_STATUS.STATUS_4);
    }

    @Test
    void upsertAttributesTest() {
        int UPDATE_RANGE = 80;
        createTable(TABLE_NAME); // comment this out when updating against environments
        insertItems(TABLE_NAME, UPDATE_RANGE); // comment this out when updating against environments
        scanByStatus(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_1);//
        scanByStatus(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_2);//
        scanByStatus(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_3);//
        scanByStatus(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_4);//
        scanAndUpdate(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_1, V2_STATUS.STATUS_A);
        scanAndUpdate(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_2, V2_STATUS.STATUS_B);
        scanAndUpdate(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_3, V2_STATUS.STATUS_C);
        scanAndUpdate(TABLE_NAME, UPDATE_RANGE, V1_STATUS.STATUS_4, V2_STATUS.STATUS_D);
    }
}
