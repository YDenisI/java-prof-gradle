package ru.gpncr;

import com.google.common.base.Joiner;

@SuppressWarnings("java:S106")
public class HelloOtus {

    public static void main(String[] args) {
        String result = Joiner.on(", ").join("Hello", "Otus");
        System.out.println(result);
    }
}
