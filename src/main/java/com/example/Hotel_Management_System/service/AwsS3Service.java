package com.example.Hotel_Management_System.service;

// AWS credentials and configuration classes
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;

// AWS S3 related classes
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

// Custom exception for your application
import com.example.Hotel_Management_System.exception.OurException;

// Spring annotations
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// File upload handling
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service // Marks this class as a Spring service component
public class AwsS3Service {

    // Name of the S3 bucket where images will be stored
    private final String bucketName = "nexus-haven-hotel-images";

    // AWS access key loaded from application.properties / application.yml
    @Value("${aws.s3.access-key}")
    public String awsS3AccessKey;

    // AWS secret key loaded from application.properties / application.yml
    @Value("${aws.s3.secret-key}")
    public String awsS3SecretKey;

    // Method to upload an image file to AWS S3
    public String saveImageToS3(MultipartFile photo) {

        // This will store the public URL of the uploaded image
        String s3LocationImage = null;

        try {
            // Get the original file name of the uploaded image
            String s3FileName = photo.getOriginalFilename();

            // Create AWS credentials using access key and secret key
            BasicAWSCredentials awsCredentials =
                    new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);

            // Build the Amazon S3 client with credentials and region
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.EU_NORTH_1) // AWS region where bucket exists
                    .build();

            // Convert MultipartFile to InputStream
            InputStream inputStream = photo.getInputStream();

            // Set metadata for the uploaded file
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg"); // Specify file type

            // Create a request object for uploading file to S3
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, inputStream, metadata);

            // Upload the file to S3
            s3Client.putObject(putObjectRequest);

            // Generate the public URL of the uploaded image
            s3LocationImage =
                    "https://" + bucketName + ".s3.amazonaws.com/" + s3FileName;

        } catch (Exception e) {
            // Print error details in console
            e.printStackTrace();

            // Throw custom exception if upload fails
            throw new OurException("Unable to upload image to S3 bucket: " + e.getMessage());
        }

        // Return the image URL stored in S3
        return s3LocationImage;
    }
}
