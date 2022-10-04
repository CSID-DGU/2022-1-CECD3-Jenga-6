package com.jenga.weather.domain.s3;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;

@Slf4j
public class S3Test {

    @Test
    void getDescription() {

        Region region = Region.AP_NORTHEAST_2;
//        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("accessKey", "secretKey");

        try (S3Client s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build()) {
            ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
            ListBucketsResponse listBucketsResponse = s3.listBuckets(listBucketsRequest);

            List<Bucket> buckets = listBucketsResponse.buckets();
            for (Bucket bucket : buckets) {
                log.info("bucket name : {}", bucket.name());
                log.info("bucket creationDate : {}", bucket.creationDate());
            }
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

}
