package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        // Step 1: Create RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Step 2: API URL
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        // Step 3: Create request body
        Map<String, String> body = new HashMap<>();
        body.put("name", "Radhika");   // change if needed
        body.put("regNo", "123456");   // put your real reg no
        body.put("email", "your@email.com");

        // Step 4: Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Step 5: Combine body + headers
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        // Step 6: Send POST request
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        
     // Get response body
        Map<String, Object> responseBody = response.getBody();

        // Extract values
        String webhookUrl = (String) responseBody.get("webhook");
        String accessToken = (String) responseBody.get("accessToken");

     // STEP 6: Your SQL Query
        String finalQuery = "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT FROM EMPLOYEE e1 JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT AND e2.DOB > e1.DOB GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME ORDER BY e1.EMP_ID DESC;";
     // STEP 7: Send final query

        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_JSON);
        headers2.set("Authorization", accessToken);

        // Create body
        Map<String, String> finalBody = new HashMap<>();
        finalBody.put("finalQuery", finalQuery);

        // Combine
        HttpEntity<Map<String, String>> request2 = new HttpEntity<>(finalBody, headers2);

        // Send POST request
        ResponseEntity<String> response2 = restTemplate.postForEntity(webhookUrl, request2, String.class);

        // Print response
        System.out.println("Final Response: " + response2.getBody());
        // Print them
        System.out.println("Webhook URL: " + webhookUrl);
        System.out.println("Access Token: " + accessToken);
        // Step 7: Print response
        System.out.println("Response: " + response.getBody());
    }
}