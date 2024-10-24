package dev.coral.utils.metrics;

public class MTSQueryGenerator {

    public static String generateQueryForService(String serviceName) {
        Long currentTime = System.currentTimeMillis();
        Long anHourAgoTime = currentTime - 3600 * 1000;
        return String.format("service.name:%s AND created:[%d TO %d]", serviceName, anHourAgoTime, currentTime);
    }

    public static String generateQueryForService(String serviceName, Long secondsInPast) {
        Long currentTime = System.currentTimeMillis();
        Long anHourAgoTime = currentTime - (secondsInPast * 1000);
        return String.format("service.name:%s AND created:[%d TO %d]", serviceName, anHourAgoTime, currentTime);
    }

    public static String appendCountToQuery(String query, int count) {
        return String.format(query+"&limit=%d", count);
    }
}
