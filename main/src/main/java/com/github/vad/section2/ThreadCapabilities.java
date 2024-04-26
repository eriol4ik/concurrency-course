package com.github.vad.section2;

public class ThreadCapabilities {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("We are now in thread" + Thread.currentThread().getName());
            System.out.println("Current thread priority is " + Thread.currentThread().getPriority());
        });
        thread.setName("New Worker Thread");
        thread.setPriority(Thread.MAX_PRIORITY);

        System.out.println("We are in thread: " + Thread.currentThread().getName() + " before a starting a new thread");
        thread.start();
        System.out.println("We are in thread: " + Thread.currentThread().getName() + " before a starting a new thread");


        Thread misbehavingThread = new Thread(() -> {
            throw new RuntimeException("Intentional Exception");
        });
        misbehavingThread.setName("Misbehaving Thread");
        misbehavingThread.setUncaughtExceptionHandler((t, e) -> {
            System.out.println("A critical error happened in thread " + t.getName() + " the error is " + e.getMessage());
        });
        misbehavingThread.start();
    }
}
