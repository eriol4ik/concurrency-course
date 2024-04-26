package com.github.vad.section6;

public class DataRace {

    public static void main(String[] args) {
        DataClass dataClass = new DataClass();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                dataClass.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                dataClass.checkForDataRace();
            }
        });
        thread1.start();
        thread2.start();
    }

    public static class DataClass {
        private int x = 0;
        private int y = 0;

        // Does not guarantee to have these two increments in specific order: can be
        public void increment() {
            x++;
            y++;
        }

        public void checkForDataRace() {
            if (y > x) {
                System.out.println("Data race is detected - y > x: " + y + " > " + x);
            }
        }
    }
}
