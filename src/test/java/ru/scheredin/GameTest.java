package ru.scheredin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class GameTest {

    private Config config;
    private Matrix matrix;
    private Game game;

    @BeforeEach
    void setUp() {
        Map<String, Config.Symbol> symbols = new HashMap<>();
        symbols.put("symbol1", new Config.Symbol(2, Config.Symbol.Type.STANDARD, null, null));
        symbols.put("symbol2", new Config.Symbol(3, Config.Symbol.Type.BONUS, Config.Symbol.Impact.MULTIPLY_REWARD, 2));

        Map<String, Config.WinCombination> winCombinations = new HashMap<>();
        winCombinations.put("group1", new Config.WinCombination(2, Config.WinCombination.When.SAME_SYMBOLS, 3, "group1", List.of(List.of("symbol1"))));

        Map<String, Integer> bonusSymbols = new HashMap<>();
        bonusSymbols.put("symbol2", 30);

        List<Config.StandardSymbolProbability> standardSymbolProbabilities = List.of(
                new Config.StandardSymbolProbability(0, 0, new HashMap<>(Map.of("symbol1", 20, "symbol2", 20)))
        );

        config = new Config(
                5,
                3,
                symbols,
                new Config.Probabilities(
                        standardSymbolProbabilities,
                        new Config.Probabilities.BonusSymbols(bonusSymbols)
                ),
                winCombinations
        );

        matrix = new Matrix(config);
        game = new Game(config);
    }

    @Test
    void testGamble_withSameSymbols() {
        Matrix mockMatrix = new Matrix(config);
        int bet = 10;

        Result result = game.gamble(mockMatrix, bet);

        assertNotNull(result);
        assertTrue(result.reward() > 0);
        assertTrue(result.appliedWinningCombinations().containsKey("symbol1"));
    }
}
