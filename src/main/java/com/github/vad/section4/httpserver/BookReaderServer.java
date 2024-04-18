package com.github.vad.section4.httpserver;

import com.github.vad.section4.ImageParallelProcessing;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BookReaderServer {
    private static final String INPUT_FILE = "The_Phantom_of_the_Opera.txt";
    private static final int NUMBER_OF_THREADS = 16;

    public static void main(String[] args) throws IOException, URISyntaxException {
        String text = new String(Files.readAllBytes(Paths.get(ImageParallelProcessing.class.getClassLoader().getResource(INPUT_FILE).toURI())));
        startServer(text);
    }

    public static void startServer(String text) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/search", new WordCountHandler(text));
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        server.setExecutor(executor);
        server.start();
    }
}
