package dev.coral.models.splunk.trace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
class Fields {
    private String event;

}

@Setter
@Getter
class Log {
    private String timestamp;
    private Fields fields;

}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Trace {
    private String traceId;
    private String spanId;
    private Object parentId;
    private String serviceName;
    private String operationName;
    private String startTime;
    private Integer durationMicros;
    private Map<String, String> tags;
    private Map<String, String> processTags;
    private List<Log> logs = new ArrayList<>();
    private String splunk;
    private String objectType;
}