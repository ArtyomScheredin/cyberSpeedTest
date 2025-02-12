package ru.scheredin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.Map;

import static ru.scheredin.Parser.parseArguments;


public class Main {
    public static final String CONFIG_ARG = "--config";
    public static final String BETTING_AMOUNT_ARG = "--betting-amount";
    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static void main(String[] args) throws IOException {
        Map<String, String> arg = parseArguments(args);
        Config config = Parser.parseConfig(getFile(arg));

        Matrix matrix = new Matrix(config);
        int bet = getBet(arg);
        Result result = new Game(config).gamble(matrix, bet);
        System.out.println(mapper.writeValueAsString(result));
    }

    private static String getFile(Map<String, String> arg) {
        String filePath = arg.get(CONFIG_ARG);
        if (filePath == null) {
            throw new IllegalArgumentException("No path to config provided");
        }
        return filePath;
    }

    private static int getBet(Map<String, String> arg) {
        try {
            return Integer.parseInt(arg.get(BETTING_AMOUNT_ARG));
        } catch (NumberFormatException e) {
            System.out.println(BETTING_AMOUNT_ARG + " hasn't been passed correctly");
            throw new RuntimeException(e);
        }
    }
}