package com.github.vad.section3;

import lombok.AllArgsConstructor;

import java.math.BigInteger;

public class LongComputationInterruption {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new LongComputationTask(new BigInteger("200000"), new BigInteger("100000000")));
//        thread.setDaemon(true); // -- so we don't block our app from exiting
        thread.start();
        Thread.sleep(100);
        thread.interrupt();
    }

    @AllArgsConstructor
    private static class LongComputationTask implements Runnable {
        private BigInteger base;
        private BigInteger power;

        @Override
        public void run() {
            System.out.println(base + " ^ " + power + " = " + pow(base, power));
        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;
            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) < 0; i = i.add(BigInteger.ONE)) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Prematurely interrupted calculation");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return result;
        }
    }
}
