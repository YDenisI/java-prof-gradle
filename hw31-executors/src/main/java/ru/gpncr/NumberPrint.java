package ru.gpncr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberPrint {
    private static final Logger logger = LoggerFactory.getLogger(NumberPrint.class);
    private boolean isFirstTurn = true;
    private int currentNumber = 1;

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private synchronized void action() {
        while (!Thread.currentThread().isInterrupted()) {
            if (isFirstTurn) {

                for (currentNumber = 1; currentNumber <= 10; currentNumber++) {
                    logger.info("FIRST: " + currentNumber);
                    sleep();
                    isFirstTurn = false;
                    notifyAll();
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                for (currentNumber = 9; currentNumber >= 1; currentNumber--) {
                    logger.info("FIRST: " + currentNumber);
                    sleep();
                    isFirstTurn = false;
                    notifyAll();
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                currentNumber = 2;
                logger.info("FIRST: " + currentNumber);
                sleep();
                isFirstTurn = false;
                notifyAll();
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                logger.info("SECOND: " + currentNumber);
                sleep();
                isFirstTurn = true;
                notifyAll();
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        NumberPrint numberPrinter = new NumberPrint();

        new Thread(() -> numberPrinter.action()).start();
        new Thread(() -> numberPrinter.action()).start();
    }
}
