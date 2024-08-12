package de.tiiita;

public class ProgressBar {

  private final int progressBarWidth;
  private final String emptyIcon;
  private final String progressIcon;

  public ProgressBar(int progressBarWidth, String emptyIcon, String progressIcon) {
    this.progressBarWidth = progressBarWidth;
    this.emptyIcon = emptyIcon;
    this.progressIcon = progressIcon;
    validateBarSettings();

  }

  public String build(int progress) {
    validateProgress(progress);

    int numOfBars = (int) (progressBarWidth * ((double) progress / 100));

    return "(" + progress + "%) [" +
        String.valueOf(progressIcon).repeat(Math.max(0, numOfBars)) +

        String.valueOf(emptyIcon).repeat(Math.max(0, progressBarWidth - numOfBars)) +
        "]";

  }

  private void validateBarSettings() {
    if (progressBarWidth > 200 || progressBarWidth < 10) {
      throw new IllegalArgumentException("Bar width cannot be higher than 200 or lower than 10");
    }
  }

  private void validateProgress(int progress) {
    if (progress > 100 || progress < 0) {
      throw new IllegalArgumentException("Progress cannot be higher than 100 or lower than 0");
    }
  }
}
