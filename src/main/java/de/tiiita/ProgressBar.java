package de.tiiita;

public class ProgressBar {
    private final int progressBarWidth;

    public ProgressBar(int progressBarWidth) {
        this.progressBarWidth = progressBarWidth;
        validateBarSettings();

    }

    public String build(int progress) {
        validateProgress(progress);

        int numOfBars = (int) (progressBarWidth * ((double) progress / 100));

        String progressBar = "(" + progress + "%) [" +
                "|".repeat(Math.max(0, numOfBars)) +
                " ".repeat(Math.max(0, progressBarWidth - numOfBars)) +
                "]";

        return progressBar;

    }

    private void validateBarSettings() {
        if (progressBarWidth > 200 || progressBarWidth < 10)
            throw new IllegalArgumentException("Bar width cannot be higher than 200 or lower than 10");
    }
    private void validateProgress(int progress) {
        if (progress > 100 || progress < 0)
            throw new IllegalArgumentException("Progress cannot be higher than 100 or lower than 0");
    }
}
