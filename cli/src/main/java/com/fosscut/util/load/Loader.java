package com.fosscut.util.load;

public abstract class Loader {
    public abstract void validate(String orderPath);
    public abstract String load(String orderPath);
}
