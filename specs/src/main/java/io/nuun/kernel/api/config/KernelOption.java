package io.nuun.kernel.api.config;


import javax.annotation.Nullable;

public class KernelOption<T>
{
    private String name;
    private T value;

    public KernelOption(String name) {
        this.name = name;
    }

    public KernelOption(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public KernelOption(KernelOption<T> option, @Nullable T value) {
        this.name = option.name;
        this.value = value == null ? option.getValue() : value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name + ": " + value;
    }
}
