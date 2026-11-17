package com.edavalos.mtx.mailjet;

import com.edavalos.mtx.mailjet.config.MtxEmailConfig;
import com.edavalos.mtx.mailjet.transport.MtxEmailTransport;
import com.edavalos.mtx.mailjet.transport.MtxSmtpEmailTransport;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MtxMailJet implements AutoCloseable {
    private final MtxEmailTransport transport;
    private final ExecutorService executor;

    public MtxMailJet(MtxEmailConfig config) {
        this.transport = new MtxSmtpEmailTransport(config);
        this.executor = Executors.newFixedThreadPool(5);
    }

    static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.printf("args: %s\n", Arrays.toString(args));
    }

    public CompletableFuture<Void> send(MtxEmail email) {
        return CompletableFuture.runAsync(() -> {
            transport.send(email);
        }, executor);
    }

    @Override
    public void close() {
        executor.shutdown();
    }
}
