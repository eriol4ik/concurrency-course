package com.github.vad.section6;

import lombok.RequiredArgsConstructor;

import java.util.Random;

public class DeadLock {
    public static void main(String[] args) {
        Intersection intersection = new Intersection();
        Thread trainA = new Thread(new TrainA(intersection));
        Thread trainB = new Thread(new TrainB(intersection));

        trainA.start();
        trainB.start();
    }

    @RequiredArgsConstructor
    public static class TrainA implements Runnable {
        private final Intersection intersection;
        private Random random = new Random();

        @Override
        public void run() {
            while (true) {
                long sleepingTime = random.nextInt(50);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                }
                intersection.takeRoadA();
            }
        }
    }

    @RequiredArgsConstructor
    public static class TrainB implements Runnable {
        private final Intersection intersection;
        private Random random = new Random();

        @Override
        public void run() {
            while (true) {
                long sleepingTime = random.nextInt(50);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                }
                intersection.takeRoadB();
            }
        }
    }

    public static class Intersection {
        private Object roadA = new Object();
        private Object roadB = new Object();

        public void takeRoadA() {
            synchronized (roadA) {
                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

                synchronized (roadB) {
                    System.out.println("Train is passing through road A");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        public void takeRoadB() {
            synchronized (roadA) {
                System.out.println("Road B is locked by thread " + Thread.currentThread().getName());

                synchronized (roadB) {
                    System.out.println("Train is passing through road B");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}
