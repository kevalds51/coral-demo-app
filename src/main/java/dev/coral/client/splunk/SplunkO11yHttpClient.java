package dev.coral.client.splunk;

import static io.micronaut.http.HttpHeaders.ACCEPT;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Headers;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import java.util.List;
import java.util.Map;

@Client("https://api.rc0.signalfx.com") // Base URL
public interface SplunkO11yHttpClient {
    @Get(value = "/v2/apm/trace/{traceId}/latest")
    @Headers(
        @Header(name = ACCEPT, value = "application/json")
    )
    List<Map<String, Object>> getTraceById(@Header("X-SF-Token") String sfxToken, @PathVariable String traceId);
}