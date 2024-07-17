package de.tiiita;

public class ProgressBar {
    public String build(int progress) {
        validateProgress(progress);
        StringBuilder barBuilder = new StringBuilder();

        barBuilder.append("|".repeat(Math.max(0, progress)));

        barBuilder.append(" (").append(progress).append("%)");
        return barBuilder.toString();
    }

    private void validateProgress(int progress) {
        if (progress > 100)
            throw new IllegalArgumentException("Progress cannot be higher than 100%");
    }
}
