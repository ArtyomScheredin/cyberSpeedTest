package ru.scheredin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    private Config config;
    private Matrix matrix;

    @BeforeEach
    void setUp() {
        Map<String, Config.Symbol> symbols = new HashMap<>();
        symbols.put("symbol1", new Config.Symbol(2, Config.Symbol.Type.STANDARD, Config.Symbol.Impact.MISS, 1));
        symbols.put("symbol2", new Config.Symbol(3, Config.Symbol.Type.BONUS, Config.Symbol.Impact.MULTIPLY_REWARD, 2));

        Map<String, Config.WinCombination> winCombinations = new HashMap<>();
        winCombinations.put("group1", new Config.WinCombination(2, Config.WinCombination.When.SAME_SYMBOLS, 3, "group1", List.of(List.of("symbol1"))));

        Map<String, Integer> bonusSymbols = new HashMap<>();
        bonusSymbols.put("symbol2", 30);

        List<Config.StandardSymbolProbability> standardSymbolProbabilities = List.of(
                new Config.StandardSymbolProbability(0, 0, new HashMap<>(Map.of("symbol1", 10, "symbol2", 20)))
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
    }

    @Test
    void testMatrixInitialization() {
        assertNotNull(matrix.getTable());
        assertEquals(config.columns(), matrix.getTable().length);
        assertEquals(config.rows(), matrix.getTable()[0].length);
    }

    @Test
    void testApplyBonusWithMultiplier() {
        int reward = 5;
        matrix.applyBonus(reward);
        assertNotNull(matrix.getAppliedBonus());
    }

    @Test
    void testApplyBonusWithExtraBonus() {
        int reward = 5;
        matrix.applyBonus(reward);
        assertNotNull(matrix.getAppliedBonus());
    }

    @Test
    void testGetRandomSymbol() {
        String symbol = matrix.getTable()[0][0];
        assertTrue(symbol.equals("symbol1") || symbol.equals("symbol2"));
    }

    @Test
    void testBonusesUpdatedProperly() {
        matrix.applyBonus(10);
        String appliedBonus = matrix.getAppliedBonus();
        assertNotNull(appliedBonus);
        assertTrue(appliedBonus.equals("symbol2") || appliedBonus.equals("symbol1"));
    }
}
