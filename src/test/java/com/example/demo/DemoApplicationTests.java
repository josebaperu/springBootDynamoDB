package com.example.demo;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {

	static AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-1");
	static AWSStaticCredentialsProvider awsCredProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials("fakeMyKeyId", "fakeSecretAccessKey"));

	static AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder
			.standard()
			.withCredentials(awsCredProvider)
			.withEndpointConfiguration(endpointConfig)
			.build();
	static DynamoDB dynamoDB = new DynamoDB(ddb);

	static String forumTableName = "Forum";
	static String threadTableName = "Thread";

	@Test
	void contextLoads() {
		writeMultipleItemsBatchWrite();

	}
	private static void writeMultipleItemsBatchWrite() {
		try {

			// Add a new item to Forum
			TableWriteItems forumTableWriteItems = new TableWriteItems(forumTableName) // Forum
					.withItemsToPut(new Item().withPrimaryKey("Name", "Amazon RDS").withNumber("Threads", 0));

			// Add a new item, and delete an existing item, from Thread
			// This table has a partition key and range key, so need to specify
			// both of them
			TableWriteItems threadTableWriteItems = new TableWriteItems(threadTableName)
					.withItemsToPut(
							new Item().withPrimaryKey("ForumName", "Amazon RDS", "Subject", "Amazon RDS Thread 1")
									.withString("Message", "ElastiCache Thread 1 message")
									.withStringSet("Tags", new HashSet<String>(Arrays.asList("cache", "in-memory"))))
					.withHashAndRangeKeysToDelete("ForumName", "Subject", "Amazon S3", "S3 Thread 100");

			System.out.println("Making the request.");
			BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(forumTableWriteItems, threadTableWriteItems);

			do {

				// Check for unprocessed keys which could happen if you exceed
				// provisioned throughput

				Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();

				if (outcome.getUnprocessedItems().size() == 0) {
					System.out.println("No unprocessed items found");
				} else {
					System.out.println("Retrieving the unprocessed items");
					outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
				}

			} while (outcome.getUnprocessedItems().size() > 0);

		} catch (Exception e) {
			System.err.println("Failed to retrieve items: ");
			e.printStackTrace(System.err);
		}

	}

}
