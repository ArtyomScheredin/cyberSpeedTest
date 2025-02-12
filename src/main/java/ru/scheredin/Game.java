package ru.scheredin;

import java.util.*;

public class Game {
    private final TreeMap<Integer, String> sameSymbols = new TreeMap<>();
    private final Map<String, Config.WinCombination> linearSymbols = new HashMap<>();
    private final Map<String, Config.Symbol> symbols;
    private final Map<String, Config.WinCombination> winCombinations;


    public Game(Config config) {
        this.winCombinations = config.winCombinations();
        this.symbols = config.symbols();
        sortCombinations();
    }

    public Result gamble(Matrix matrix, int bet) {
        Map<String, Integer> counters = new HashMap<>();
        for (String[] row : matrix.getTable()) {
            for (String el : row) {
                counters.compute(el, (k, v) -> v == null ? 1 : v + 1);
            }
        }

        Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
        double reward = checkSameSymbols(bet, counters, appliedWinningCombinations);
        reward = matrix.applyBonus(reward);

        return new Result(matrix.getTable(), reward, appliedWinningCombinations, matrix.getAppliedBonus());
    }


    private double checkSameSymbols(int bet, Map<String, Integer> counters, Map<String, List<String>> appliedWinningCombinations) {
        double reward = 0;
        for (Map.Entry<String, Integer> entry : counters.entrySet()) {
            String symbol = entry.getKey();
            Integer freq = entry.getValue();
            if (symbols.get(symbol).type() == Config.Symbol.Type.BONUS) {
                //do not count bonus symbols
                continue;
            }
            Map.Entry<Integer, String> combination = sameSymbols.floorEntry(freq);

            if (combination != null) {
                reward = countWin(bet, appliedWinningCombinations, combination, symbol, reward);
            }
        }
        return reward;
    }

    private double countWin(int bet,
                         Map<String, List<String>> appliedWinningCombinations,
                         Map.Entry<Integer, String> combination,
                         String symbol,
                         double reward) {
        String combinationName = combination.getValue();
        double multiplier = symbols.get(symbol).rewardMultiplier();
        Config.WinCombination winCombination = winCombinations.get(combinationName);

        //calculate reward
        reward += bet * multiplier * winCombination.rewardMultiplier();

        //update list of applied combinations
        appliedWinningCombinations.putIfAbsent(symbol, new ArrayList<>());
        appliedWinningCombinations.get(symbol).add(combinationName);
        return reward;
    }

    private void sortCombinations() {
        for (var entry : winCombinations.entrySet()) {
            if (entry.getValue().when() == Config.WinCombination.When.SAME_SYMBOLS) {
                sameSymbols.put(entry.getValue().count(), entry.getKey());
            } else {
                linearSymbols.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
