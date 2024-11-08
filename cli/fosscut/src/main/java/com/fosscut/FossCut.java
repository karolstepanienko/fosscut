package com.fosscut;

public class FossCut {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new FossCut().getGreeting());
    }
}
