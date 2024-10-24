package dev.coral.controllers;

import dev.coral.client.splunk.SplunkO11yHttpClient;
import dev.coral.config.EndpointConfig;
import dev.coral.service.SplunkO11yDataFetcherService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@ExecuteOn(TaskExecutors.BLOCKING)
public class DynamicController {

    private final EndpointConfig endpointConfig;
    private final HttpClient httpClient;
    private final SplunkO11yDataFetcherService splunkO11yDataFetcherService;

    @Inject
    public DynamicController(EndpointConfig endpointConfig, @Client HttpClient httpClient,
                             SplunkO11yDataFetcherService splunkO11yDataFetcherService) {
        this.endpointConfig = endpointConfig;
        this.httpClient = httpClient;
        this.splunkO11yDataFetcherService = splunkO11yDataFetcherService;
    }

    @Get("/splunk/trace/{traceId}")
    public void getSplunkTrce(String traceId) {
        System.out.println("Received request to fetch traceID: " + traceId);
        System.out.println("Serialized Trace: " + splunkO11yDataFetcherService.getTrace(traceId));
    }

    @Get("/{dynamicEndpoint}")
    public String handleRequest(String dynamicEndpoint) {
        System.out.println("===================================");
        System.out.println("Dynamic request: " + dynamicEndpoint);
        EndpointConfig.Endpoint endpoint = endpointConfig.getEndpoints().stream()
                .filter(e -> e.getUrl().equalsIgnoreCase(dynamicEndpoint))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));

        StringBuilder responseBuilder = new StringBuilder();
        for (String action : endpoint.getActions()) {
            System.out.println("-----------------------------------");
            String[] parts = action.split("\\|");
            String actionType = parts[0];
            String actionValue = parts[1];
            System.out.println("Action type: " + actionType + ", Action value: " + actionValue);
            switch (actionType) {
                case "request":
                    System.out.println("Making request to: " + actionValue);
                    String response = httpClient.toBlocking().retrieve(actionValue);
                    responseBuilder.append(response);
                    break;
                case "wait_random":
                    try {
                        long waitTime = ThreadLocalRandom.current().nextLong(0, Long.parseLong(actionValue.replace("ms", "")));
                        System.out.println("Waiting for " + waitTime + "ms");
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    break;
                // Add more cases for other action types
            }
        }
        System.out.println("===================================");
        return responseBuilder.toString();
    }
}