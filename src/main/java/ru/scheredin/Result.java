package ru.scheredin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record Result(
        @JsonProperty String[][] matrix,
        @JsonProperty double reward,
        @JsonProperty Map<String, List<String>> appliedWinningCombinations,
        @JsonProperty String appliedBonusSymbol
) {
}
