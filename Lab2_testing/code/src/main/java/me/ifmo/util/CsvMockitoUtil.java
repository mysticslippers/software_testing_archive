package me.ifmo.util;

import me.ifmo.function.MathFunction;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

public final class CsvMockitoUtil {

    private CsvMockitoUtil() {}

    public static MathFunction mockFromCsv(String filePath) throws IOException {
        MathFunction mock = Mockito.mock(MathFunction.class);

        Map<Double, Double> values = new HashMap<>();
        List<Double> sortedKeys = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean skipHeader = true;

            while ((line = bufferedReader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue;
                }

                double x = Double.parseDouble(parts[0].trim());

                String rawValue = parts[1].trim();
                double value;

                if ("NaN".equalsIgnoreCase(rawValue) || "ERROR".equalsIgnoreCase(rawValue)) {
                    value = Double.NaN;
                } else {
                    value = Double.parseDouble(rawValue);
                }

                values.put(x, value);
                sortedKeys.add(x);
            }
        }

        Collections.sort(sortedKeys);

        when(mock.calculate(anyDouble(), anyDouble()))
                .thenAnswer(invocation -> {
                    double x = invocation.getArgument(0);

                    int idx = Collections.binarySearch(sortedKeys, x);
                    if (idx >= 0) {
                        return values.get(sortedKeys.get(idx));
                    }

                    int insertPos = -idx - 1;

                    if (insertPos == 0) {
                        return values.get(sortedKeys.get(0));
                    }
                    if (insertPos >= sortedKeys.size()) {
                        return values.get(sortedKeys.get(sortedKeys.size() - 1));
                    }

                    double left = sortedKeys.get(insertPos - 1);
                    double right = sortedKeys.get(insertPos);

                    double nearest = Math.abs(x - left) < Math.abs(x - right) ? left : right;
                    return values.get(nearest);
                });

        return mock;
    }
}