package ru.gpncr;

@SuppressWarnings({"java:S115"})
public enum Denomination {
    Denomination_5000(5000),
    Denomination_2000(2000),
    Denomination_1000(1000),
    Denomination_500(500),
    Denomination_200(200),
    Denomination_100(100);

    public final int value;

    Denomination(int value) {
        this.value = value;
    }
}
