package com.jenga.weather.domain.s3.service;

import com.jenga.weather.domain.s3.model.S3;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.ArrayList;
import java.util.List;

@Service
public class S3Service {

    public List<S3> getS3Instances() {
        List<S3> s3List = new ArrayList<>();

        Region region = Region.AP_NORTHEAST_2;
        try (S3Client s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build()) {
            ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
            ListBucketsResponse listBucketsResponse = s3Client.listBuckets(listBucketsRequest);

            listBucketsResponse.buckets().forEach(v -> {
                S3 s3 = S3.builder()
                        .resourceName(v.name())
                        .build();

                s3List.add(s3);
            });

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return s3List;
    }
}
