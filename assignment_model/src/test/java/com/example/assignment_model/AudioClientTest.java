package com.example.assignment_model;

import com.example.assignment_model.model.Audio;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AudioClientTest {

    @LocalServerPort
    private int port;

    private static String baseUrl;
    private static final String artistName1 = "eminem";
    private static final String artistName2 = "usher";
    private static final String artistName3 = "akon";
    private static final Audio newAudio = new Audio("taylor swift","Love Story","Fearless",1,2008,350,1500);

    @BeforeAll
    public static void init() {
        baseUrl = "http://localhost:" + 9001 + "/audioApi/";
    }

    @Test
    public void testConcurrentRequests() {o
        int numClients = 50; // total number of clients
        int numGets = 5; // number of clients sending GET requests
        int numPosts = 1; // number of clients sending POST requests
        ExecutorService executorService = Executors.newFixedThreadPool(numClients);
        Random random = new Random();
        for (int i = 0; i < numClients; i++) {
            int r = random.nextInt(numGets + numPosts);
            if (r < numGets) {
                executorService.submit(() -> {
                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity<>(headers);
                    String artistName;
                    int artistIndex = random.nextInt(3);
                    if (artistIndex == 0) {
                        artistName = artistName1;
                    } else if (artistIndex == 1) {
                        artistName = artistName2;
                    } else {
                        artistName = artistName3;
                    }
                    ResponseEntity<Audio> response = restTemplate.exchange(baseUrl + "audio/" + artistName, HttpMethod.GET, entity, Audio.class);
                    System.out.println("Response for artist " + artistName + " : " + Objects.requireNonNull(response.getBody()).getTrackTitle());
                });
            } else {
                executorService.submit(() -> {
                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Audio> entity = new HttpEntity<>(newAudio, headers);
                    ResponseEntity<String> response = restTemplate.exchange(baseUrl + "newAudio", HttpMethod.POST, entity, String.class);
                    System.out.println("Response for creating audio item: " + response.getBody());
                });
            }
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
    }


    @Test
    public void testGetAllAudioItems() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "audios", HttpMethod.GET, entity, String.class);
        System.out.println("Response for all audio items: " + response.getBody());
    }

    @Test
    public void testCreateAudioItem() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Audio> entity = new HttpEntity<>(newAudio, headers);
        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "newAudio", HttpMethod.POST, entity, String.class);
        System.out.println("Response for creating audio item: " + response.getBody());
    }
}
