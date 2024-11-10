package com.example.demo;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import java.util.List;

public abstract class AbstractBaseTest {
    protected static List<V1_STATUS> v1_STATUSES = List.of(V1_STATUS.STATUS_1, V1_STATUS.STATUS_2, V1_STATUS.STATUS_3, V1_STATUS.STATUS_4);

    private static final String ENDPOINT = "http://localhost:8000";
    private static final String REGION = Regions.US_EAST_1.getName();
    private static final String ACCESS_KEY = "myKey";
    private static final String SECRET_KEY = "secret";

    protected static String ID_COLUMN_NAME = "id";
    protected static String METADATA_COLUMN_NAME = "metadata";
    protected static String STATUS_COLUMN_NAME = "status";

    protected static final AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration(ENDPOINT, REGION);
    protected static final AWSStaticCredentialsProvider awsCredProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));

    protected static final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder
            .standard()
            .withCredentials(awsCredProvider)
            .withEndpointConfiguration(endpointConfig)
            .build();
}
