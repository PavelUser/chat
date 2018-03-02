package com.company.client;

import com.company.server.Server;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    static final Logger log = Logger.getLogger(Client.class);
    private Socket socket;
    private DataInputStream input;
    private InputHandler inputHandler;
    private DataOutputStream output;
    private OutputHandler outputHandler;
    private Scanner scanner;

    private boolean running = true;

    private boolean isRunning() {
        return running;
    }

    private void setRunning(boolean running) {
        this.running = running;
    }

    public void close() {
        setRunning(false);
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            } finally {
                socket = null;
            }
        }
        if (inputHandler != null) {
            inputHandler = null;
        }
        if (outputHandler != null) {
            outputHandler = null;
        }

        /*
        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
        */

        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            } finally {
                input = null;
            }
        }
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            } finally {
                output = null;
            }
        }
        log.info("Клиент закрыл все ресурсы");
    }

    private class InputHandler extends Thread {

        @Override
        public void run() {
            while (isRunning()) {
                try {
                    String msg = input.readUTF();
                    log.info("input: " + msg);
                } catch (SocketException | EOFException e) {
                    log.warn(e.getMessage(), e);
                    close();
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
    }

    private class OutputHandler extends Thread {

        @Override
        public void run() {
            while (isRunning()) {
                String msg = scanner.nextLine();
                try {
                    output.writeUTF(msg);
                    log.info("output: " + msg);
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
    }


    public void run() throws Exception {
        String ip = InetAddress.getLocalHost().getHostAddress();

        socket = new Socket(ip, Server.DEFAULT_PORT);
        scanner = new Scanner(System.in);
        this.input = new DataInputStream(socket.getInputStream());
        inputHandler = new InputHandler();
        output = new DataOutputStream(socket.getOutputStream());
        outputHandler = new OutputHandler();
        outputHandler.setDaemon(true);

        inputHandler.start();
        outputHandler.start();

        while (isRunning()) {
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.run();
    }
}
