package ru.scheredin;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static Config parseConfig(String file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try (FileReader reader = new FileReader(file)) {
            return objectMapper.readValue(reader, Config.class);
        }
    }

    public static Map<String, String> parseArguments(String[] args) {
        Map<String, String> arguments = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                String key = args[i];
                String value = "";

                if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                    value = args[i + 1];
                    i++;
                }

                arguments.put(key, value);
            }
        }

        return arguments;
    }
}
