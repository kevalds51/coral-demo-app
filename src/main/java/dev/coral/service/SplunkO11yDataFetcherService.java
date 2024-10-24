package dev.coral.service;

import dev.coral.client.splunk.SplunkO11yHttpClient;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class SplunkO11yDataFetcherService {

    private final SplunkO11yHttpClient splunkO11yHttpClient;
    private final String SFX_TOKEN;

    @Inject
    public SplunkO11yDataFetcherService(SplunkO11yHttpClient splunkO11yHttpClient) {
        this.splunkO11yHttpClient = splunkO11yHttpClient;
        this.SFX_TOKEN = System.getenv("SIGNALFX_API_TOKEN");
    }

    public String getTrace(String traceID) {
        return splunkO11yHttpClient.getTraceById(SFX_TOKEN, traceID);
    }
}