package com.github.vad.section5;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Random;

public class AtomicOperations {

    public static void main(String[] args) {
        Metrics metrics = new Metrics();
        Thread businessLogic1 = new BusinessLogic(metrics);
        Thread businessLogic2 = new BusinessLogic(metrics);
        Thread metricsPrinter = new MetricsPrinter(metrics);

        businessLogic1.start();
        businessLogic2.start();
        metricsPrinter.start();
    }

    @RequiredArgsConstructor
    public static class MetricsPrinter extends Thread {
        private final Metrics metrics;

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                Thread.sleep(100);
                System.out.println("Current metrics: " + metrics.getAverage());
            }
        }
    }

    @RequiredArgsConstructor
    public static class BusinessLogic extends Thread {
        private final Metrics metrics;
        private Random random = new Random();

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis();
                Thread.sleep(random.nextInt(10));
                long end = System.currentTimeMillis();
                metrics.addSample(end - start);
            }
        }
    }

    public static class Metrics {
        private long count = 0;
        @Getter
        private volatile double average = 0.0;

        public synchronized void addSample(long sample) {
            double currentSum = average * count;
            count++;
            average = (currentSum + sample) / count;
        }
    }
}
