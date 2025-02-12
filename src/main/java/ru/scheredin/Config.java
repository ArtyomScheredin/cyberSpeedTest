package ru.scheredin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record Config(
        @JsonProperty(required = true) int columns,
        @JsonProperty(required = true) int rows,
        @JsonProperty(required = true) Map<String, Symbol> symbols,
        @JsonProperty(required = true) Probabilities probabilities,
        @JsonProperty(required = true) Map<String, WinCombination> winCombinations
) {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public record Symbol(
            @JsonProperty double rewardMultiplier,
            @JsonProperty(required = true) Type type,
            @JsonProperty Impact impact,
            @JsonProperty Integer extra
    ) {
        @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
        public enum Type {
            STANDARD,
            BONUS;

            @JsonCreator
            public static Type fromString(String value) {
                for (Type type : values()) {
                    if (type.name().equalsIgnoreCase(value)) {
                        return type;
                    }
                }
                throw new IllegalArgumentException("Unknown Type: " + value);
            }
        }

        @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
        public enum Impact {
            MULTIPLY_REWARD,
            MISS,
            EXTRA_BONUS;

            @JsonCreator
            public static Impact fromString(String value) {
                for (Impact impact : values()) {
                    if (impact.name().equalsIgnoreCase(value)) {
                        return impact;
                    }
                }
                throw new IllegalArgumentException("Unknown Impact: " + value);
            }
        }
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public record Probabilities(
            @JsonProperty(required = true)
            List<StandardSymbolProbability> standardSymbols,

            @JsonProperty(required = true)
            BonusSymbols bonusSymbols) {

        @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
        public record BonusSymbols(@JsonProperty Map<String, Integer> symbols) {
        }
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public record StandardSymbolProbability(
            @JsonProperty int column,
            @JsonProperty int row,
            @JsonProperty Map<String, Integer> symbols) {
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public record WinCombination(
            @JsonProperty double rewardMultiplier,
            @JsonProperty When when,
            @JsonProperty int count,
            @JsonProperty String group,
            @JsonProperty List<List<String>> coveredAreas
    ) {

        @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
        public enum When {
            SAME_SYMBOLS,
            LINEAR_SYMBOLS;

            @JsonCreator
            public static When fromString(String value) {
                for (When when: values()) {
                    if (when.name().equalsIgnoreCase(value)) {
                        return when;
                    }
                }
                throw new IllegalArgumentException("Unknown Impact: " + value);
            }
        }
    }
}