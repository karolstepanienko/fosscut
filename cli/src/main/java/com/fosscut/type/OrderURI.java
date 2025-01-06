package com.fosscut.type;

import java.net.URI;

public class OrderURI {

    URI uri;

    public OrderURI(URI uri) {
        this.uri = uri;
    }

    public String getHost() {
        return this.uri.getHost();
    }

    public int getPort() {
        return this.uri.getPort();
    }

    public String getIdentifier() {
        // remove first character from string
        return this.uri.getPath().substring(1);
    }

}
