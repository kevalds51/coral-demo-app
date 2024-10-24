package dev.coral.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import jakarta.inject.Singleton;

import java.util.*;


@ConfigurationProperties("coral")
public interface EndpointConfig {
    List<Endpoint> getEndpoints();

    interface Endpoint {
        String getName();
        String getUrl();
        List<String> getActions();
    }

    @Singleton
    class MapToEndpointConverter implements TypeConverter<Map, Endpoint> {

        @Override
        public Optional<Endpoint> convert(Map object, Class<Endpoint> targetType, ConversionContext context) {
            return Optional.of(new Endpoint() {
                @Override
                public String getName() {
                    return object.getOrDefault("name", "").toString();
                }

                @Override
                public String getUrl() {
                    return object.getOrDefault("url", "").toString();

                }

                @Override
                public List<String> getActions() {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        return objectMapper.convertValue(object.get("actions"), new TypeReference<>() {
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new ArrayList<String>();
                }
            });
        }
    }
}