package com.fosscut.util.load;

import java.io.IOException;

public abstract interface Loader {
    public abstract void validate(String orderPath);
    public abstract String load(String orderPath) throws IOException;
}
