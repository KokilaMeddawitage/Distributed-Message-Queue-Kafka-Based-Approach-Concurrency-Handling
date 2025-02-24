package com.example.demo.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PartitionMapper {

    // Airline to Partition Mapping
    private static final Map<String, Integer> AIRLINE_PARTITION_MAP = new ConcurrentHashMap<>();

    static {
        // Define fixed airline-partition mappings
        AIRLINE_PARTITION_MAP.put("G9", 0);
        AIRLINE_PARTITION_MAP.put("9P", 1);
        AIRLINE_PARTITION_MAP.put("AA", 2);
        AIRLINE_PARTITION_MAP.put("BA", 3);
        AIRLINE_PARTITION_MAP.put("UA", 4);
        // Add more mappings as needed
    }

    // Get partition for an airline
    public static int getPartitionForAirline(String airlineDesignator, int totalPartitions) {
        return AIRLINE_PARTITION_MAP.getOrDefault(airlineDesignator, Math.abs(airlineDesignator.hashCode()) % totalPartitions);
    }
}
