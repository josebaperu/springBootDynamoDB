package com.example.demo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.IntStream;

public class TestUtils extends AbstractBaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestUtils.class);

    private static List<V1_STATUS> v1_STATUSES = List.of(V1_STATUS.STATUS_1, V1_STATUS.STATUS_2, V1_STATUS.STATUS_3, V1_STATUS.STATUS_4);

    public static void createTable(AmazonDynamoDB ddb, String tableName) {
        ddb.listTables().getTableNames().forEach(t -> logger.info("table : " + t));
        ddb.deleteTable(tableName);
        CreateTableRequest request = new CreateTableRequest()                                             // RANGE is Optional
                .withKeySchema(new KeySchemaElement(ID_COLUMN_NAME, KeyType.HASH)/*, new KeySchemaElement("range", KeyType.RANGE)*/)
                .withAttributeDefinitions(
                        new AttributeDefinition(
                                ID_COLUMN_NAME, ScalarAttributeType.S))
                .withProvisionedThroughput(new ProvisionedThroughput(
                        10L, 10L))
                .withTableName(tableName);
        try {
            CreateTableResult result = ddb.createTable(request);
            logger.info("result : " + result.getTableDescription());
        } catch (AmazonServiceException e) {
            logger.error("error : " + e.getErrorMessage());
            System.exit(1);
        }
    }

    public static void insertItem(AmazonDynamoDB ddb, String tableName, int range) {

        IntStream.range(0, range).forEach(i -> {
                    int randomIdx = new Random().nextInt(v1_STATUSES.size());
                    HashMap<String, AttributeValue> item_key = new HashMap<>();
                    item_key.put(ID_COLUMN_NAME, new AttributeValue(UUID.randomUUID().toString()));
                    item_key.put(METADATA_COLUMN_NAME, new AttributeValue(""));
                    item_key.put(STATUS_COLUMN_NAME, new AttributeValue(v1_STATUSES.get(randomIdx).name()));
                    try {
                        ddb.putItem(tableName, item_key);
                    } catch (ResourceNotFoundException e) {
                        logger.error("error : " + e.getMessage());
                        System.exit(1);
                    } catch (AmazonServiceException e) {
                        logger.error("error : " + e.getMessage());
                        System.exit(1);
                    }
                }
        );
    }

    public static void scanByStatus(AmazonDynamoDB ddb, String tableName, int range, V1_STATUS STATUS) {
        ScanRequest request = new ScanRequest();
        request.setTableName(tableName);
        request.setLimit(range);
        request.setExpressionAttributeValues(Map.of(":status", new AttributeValue(STATUS.name())));
        request.setFilterExpression("#status = :status");
        request.addExpressionAttributeNamesEntry("#status", STATUS_COLUMN_NAME);
        ScanResult response = ddb.scan(request);

        logger.info("count : " + response.getItems().size() + "/" + response.getScannedCount());
        logger.info("items[" + STATUS.name() + "] -> " + response.getItems());
        System.out.println();
    }
    public static void scanAndUpdate(AmazonDynamoDB ddb, String tableName, int range, V1_STATUS STATUS, V2_STATUS UPDATE_STATUS) {


        do {
            ScanRequest request = new ScanRequest();
            request.setTableName(tableName);
            request.setLimit(range);
            request.setExpressionAttributeValues(Map.of(":status", new AttributeValue(STATUS.name())));
            request.setFilterExpression("#status = :status");
            request.addExpressionAttributeNamesEntry("#status", STATUS_COLUMN_NAME);
            ScanResult response = ddb.scan(request);
            List<Map<String,AttributeValue>> results = response.getItems();
            int statusV1results = results.size();
            if(statusV1results == 0){
                break;
            }
            results.forEach( item -> {

                HashMap<String,AttributeValueUpdate> attrUpdateMap =
                        new HashMap<>();
                attrUpdateMap.put(METADATA_COLUMN_NAME, new AttributeValueUpdate(
                        new AttributeValue("{\"isMigratedFromV1\": true}"), AttributeAction.PUT));
                attrUpdateMap.put("status", new AttributeValueUpdate(
                        new AttributeValue(UPDATE_STATUS.name()), AttributeAction.PUT));

                UpdateItemRequest updateItemRequest = new UpdateItemRequest();
                updateItemRequest.setTableName(tableName);
                updateItemRequest.setKey(Map.of(ID_COLUMN_NAME, new AttributeValue(item.get(ID_COLUMN_NAME).getS())));
                updateItemRequest.setAttributeUpdates(attrUpdateMap);
                try {
                   ddb.updateItem(updateItemRequest);
                } catch (ResourceNotFoundException e) {
                    logger.error("error updating: " + e.getMessage());
                    System.exit(1);
                } catch (AmazonServiceException e) {
                    logger.error("error updating: " + e.getMessage());
                    System.exit(1);
                }
            });

            logger.info("items[" + UPDATE_STATUS + "] -> " + response.getItems().size() + " updated");
            /*
            request.setExpressionAttributeValues(Map.of(":status", new AttributeValue(UPDATE_STATUS.name())));
            response = ddb.scan(request);
            List updateV2 = response.getItems();
            Assertions.assertEquals(statusV1results, updateV2.size());
            */
        }
        while (true);
        logger.info("update complete for [" + UPDATE_STATUS +"]");

    }

}
