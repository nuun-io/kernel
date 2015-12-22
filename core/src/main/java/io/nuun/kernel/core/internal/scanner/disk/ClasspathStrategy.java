package io.nuun.kernel.core.internal.scanner.disk;

public class ClasspathStrategy {
    private final Strategy strategy;
    private final boolean additional;

    public ClasspathStrategy() {
        this.strategy = Strategy.ALL;
        this.additional = true;
    }

    public ClasspathStrategy(String strategy, boolean additional) {
        if (strategy == null) {
            this.strategy = Strategy.ALL;
        }
        else {
            this.strategy = Strategy.valueOf(strategy.toUpperCase());
        }
        this.additional = additional;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public boolean isAdditional() {
        return additional;
    }

    public enum Strategy {
        ALL,
        SYSTEM,
        CLASSLOADER,
        NONE
    }
}
