package com.github.vad.section5;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class ResourseSharing {
    public static void main(String[] args) {
        InventoryHolder inventoryHolder = new InventoryHolder();
        List<Thread> threads = List.of(new IncrementingThread(inventoryHolder), new DecrementingThread(inventoryHolder));

        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println(inventoryHolder.getItems());
    }

    @RequiredArgsConstructor
    public static class IncrementingThread extends Thread {
        private final InventoryHolder inventoryHolder;

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryHolder.increment();
            }
        }
    }

    @RequiredArgsConstructor
    public static class DecrementingThread extends Thread {
        private final InventoryHolder inventoryHolder;

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryHolder.decrement();
            }
        }
    }

    @Getter
    private static class InventoryHolder {
        private int items = 0;

        private synchronized void increment() {
            items++;
        }

        private synchronized void decrement() {
            items--;
        }
    }
}
