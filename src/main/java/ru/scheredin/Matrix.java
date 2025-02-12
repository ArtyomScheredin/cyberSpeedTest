package ru.scheredin;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Matrix {
    private static final SecureRandom random = new SecureRandom();
    private final String[][] table;
    private Bonus extraBonus;
    private Bonus multiplierBonus;
    private String appliedBonus;

    private record Bonus(String name, Config.Symbol symbol) {}

    public Matrix(Config config) {
        int columns = config.columns();
        int rows = config.rows();
        table = new String[columns][rows];


        for (int column = 0; column < columns; column++)
            for (int row = 0; row < rows; row++)
                table[column][row] = getRandomSymbol(config, column, row);
    }


    public double applyBonus(double reward) {
        if ((multiplierBonus == null && extraBonus == null) || reward == 0) {
            return reward;
        }
        double multiplied = reward * (multiplierBonus == null ? 1 : multiplierBonus.symbol.rewardMultiplier());
        double summed = reward + (extraBonus == null ? 0 : extraBonus.symbol.extra());
        if (multiplied > summed) {
            appliedBonus = multiplierBonus.name;
            return multiplied;
        } else {
            appliedBonus = extraBonus.name;
            return summed;
        }
    }

    public String getAppliedBonus() {
        return appliedBonus;
    }

    public String[][] getTable() {
        return table;
    }

    /**
     * chooses random symbol based on given column and row indices and the provided probability configuration
     */
    private String getRandomSymbol(Config config, int column, int row) {
        Map<String, Integer> probs = calculateProbabilities(config, column, row);

        int totalWeight = 0;
        for (Integer value : probs.values()) {
            totalWeight += value;
        }
        int randomIndex = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (var entry : probs.entrySet()) {
            currentWeight += entry.getValue();
            if (randomIndex < currentWeight) {
                updateBonuses(entry.getKey(), config);
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Random selection failed");
    }

    private void updateBonuses(String name, Config config) {
        Config.Symbol symbol = config.symbols().get(name);
        if (symbol.type() == Config.Symbol.Type.STANDARD) {
            return;
        }
        if (symbol.impact() == Config.Symbol.Impact.EXTRA_BONUS) {
            if (extraBonus == null || symbol.extra() > extraBonus.symbol.extra()) {
                extraBonus = new Bonus(name, symbol);
            }
        } else if (symbol.impact() == Config.Symbol.Impact.MULTIPLY_REWARD) {
            if (multiplierBonus == null || symbol.rewardMultiplier() > multiplierBonus.symbol.rewardMultiplier()) {
                multiplierBonus = new Bonus(name, symbol);
            }
        }
    }

    /**
     * @return mapping symbol name to its probability
     */
    private static Map<String, Integer> calculateProbabilities(Config config, int column, int row) {
        List<Config.StandardSymbolProbability> stdProbs = config
                .probabilities()
                .standardSymbols();

        Optional<Map<String, Integer>> probsOpt = stdProbs
                .stream()
                .filter(p -> p.column() == column && p.row() == row)
                .findFirst()
                .map(Config.StandardSymbolProbability::symbols);

        if (probsOpt.isEmpty()) {
            return calculateProbabilities(config, 0,0);
        }

        Map<String, Integer> bonuses = config.probabilities().bonusSymbols().symbols();
        probsOpt.ifPresent(p -> p.putAll(bonuses));
        return probsOpt.get();
    }


    @Override
    public String toString() {
        return "as";
    }
}
