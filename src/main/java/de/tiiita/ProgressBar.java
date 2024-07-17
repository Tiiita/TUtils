package de.tiiita;

import java.util.concurrent.CompletableFuture;

public class ProgressBar {
    private String bar;
    private final boolean loadingIconAnimation;
    private String currentLoadingIconTick = "|";

    public ProgressBar(boolean loadingIconAnimation)  {
        this.loadingIconAnimation = loadingIconAnimation;
    }

    public String build(int progress) {
        StringBuilder barBuilder = new StringBuilder();
        changeIconTick();
        if (loadingIconAnimation) {
            barBuilder.append("[").append(currentLoadingIconTick).append("]    ");
        }

        for (int i = 0; i < progress; ++i) {
            barBuilder.append("|");
        }

        barBuilder.append(" (").append(progress).append("%)");
        return barBuilder.toString();
    }

    private void validateProgress(int progress) {
        if (progress > 100)
            throw new IllegalArgumentException("Progress cannot be higher than 100%");
    }

    private void changeIconTick() {
        switch (currentLoadingIconTick) {
            case "|":
                currentLoadingIconTick = "/";
                break;
            case "/":
                currentLoadingIconTick = "—";
                break;
            case "—":
                currentLoadingIconTick = "\\";
                break;
            default:
                currentLoadingIconTick = "|";
                break;
        }
    }

}
