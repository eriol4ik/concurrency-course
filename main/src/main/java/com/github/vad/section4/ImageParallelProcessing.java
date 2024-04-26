package com.github.vad.section4;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageParallelProcessing {

    public static final String SOURCE_FILE = "many-flowers.jpg";
    public static final String DESTINATION_FILE = "output/many-flowers.jpg";

    public static void main(String[] args) throws IOException {
        BufferedImage originalImage = ImageIO.read(ImageParallelProcessing.class.getClassLoader().getResource(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        System.out.println("Checking Single Threaded...");
        checkTiming("Single Threaded", () -> recolorImageSingleThreaded(originalImage, resultImage));
        System.out.println("Checking Multi Threaded...");
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        checkTiming(numberOfThreads + " Multi Threaded", () -> recolorImageMultiThreaded(originalImage, resultImage, numberOfThreads));

        File outputFile = new File(DESTINATION_FILE);
        outputFile.mkdir();
        ImageIO.write(resultImage, "jpg", outputFile);
    }

    private static void checkTiming(String title, Runnable runnable) {
        int tries = 10;
        long[] totalTimes = new long[10];
        for (int i = 0; i < tries; i++) {
            long fromTime = System.currentTimeMillis();
            runnable.run();
            totalTimes[i] = System.currentTimeMillis() - fromTime;
        }
        long average = Arrays.stream(totalTimes).sum() / tries;
        System.out.printf("%s took in average: %sms, by tries: %s\n", title, average, Arrays.toString(totalTimes));
    }

    public static void recolorImageSingleThreaded(BufferedImage original, BufferedImage result) {
        recolorImage(original, result, 0, 0, original.getWidth(), original.getHeight());
    }

    public static void recolorImageMultiThreaded(BufferedImage original, BufferedImage result, int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        int width = original.getWidth();
        int height = original.getHeight() / numberOfThreads;
        for (int i = 0; i < numberOfThreads; i++) {
            int topCorner = height * i;
            threads.add(new Thread(() -> recolorImage(original, result, 0, topCorner, width, height)));
        }
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner, int width, int height) {
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage, resultImage, x, y);
            }
        }
    }

    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);
        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed;
        int newGreen;
        int newBlue;

        if (isShadeOfGray(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }
        resultImage.setRGB(x, y, createRgbFromColors(newRed, newGreen, newBlue));
    }

    public static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(green - blue) < 30 && Math.abs(red - blue) < 30;
    }

    public static int createRgbFromColors(int red, int green, int blue) {
        int rgb = 0;
        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;
        rgb |= 0xFF000000;

        return rgb;
    }

    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }
}
