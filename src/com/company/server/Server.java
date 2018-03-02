package com.company.server;

import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private final Logger log = Logger.getLogger(Server.class);

    public static final int DEFAULT_PORT = 2000;
    private boolean running = true;

    private ServerSocket listener;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private InputHandler inputHandler;

    private static final List<String> STOP_COMMANDS = Arrays.asList("close", "exit", "пока");
    private static final Map<String, String> ANSWERS;

    static {
        ANSWERS = new HashMap<>();
        ANSWERS.put("привет", "здоров");
        ANSWERS.put("как дела?", "нормально");
        ANSWERS.put("че делаешь?", "пиво пью");
        ANSWERS.put("пока", "пока");
    }

    private static final String DEFAULT_ANSWER = "хз";

    private boolean isRunning() {
        return running;
    }

    private void setRunning(boolean running) {
        this.running = running;
    }

    public void close() {
        setRunning(false);
        if (listener != null) {
            try {
                listener.close();
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            } finally {
                listener = null;
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            } finally {
                socket = null;
            }
        }
        if (inputHandler != null) {
            inputHandler.interrupt();
            inputHandler = null;
        }
        if(input != null){
            try {
                input.close();
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            }finally {
                input = null;
            }
        }
        if(output != null){
            try {
                output.close();
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            }finally {
                input = null;
            }
        }
        log.info("Сервер закрыл все ресурсы");
    }

    private class InputHandler extends Thread {
        @Override
        public void run() {
            while (isRunning()) {
                try {
                    String msg = input.readUTF();
                    handleMsg(msg);
                    System.err.println(msg);
                }  catch (SocketException e) {
                    log.warn(e.getMessage(), e);
                    close();
                }catch (Exception e){
                    log.warn(e.getMessage(), e);
                }
            }
        }

        @Override
        public void interrupt() {
            super.interrupt();
            log.info("Поток InputHandler остановлен");
        }

        void handleMsg(String msg) {
            log.info("input: " + msg);
            String lowerCaseMsg = msg.toLowerCase();
            boolean stop = STOP_COMMANDS.contains(lowerCaseMsg);
            String answer = ANSWERS.get(lowerCaseMsg);
            if (answer == null) {
                answer = stop ? "Пришла команда закрыть сервер" : DEFAULT_ANSWER;
            }
            sendMsg(answer);
            if(stop){
                close();
            }
        }
    }

    private void sendMsg(String msg) {
        log.info("output: " + msg);
        try {
            output.writeUTF(msg);
            output.flush();
        } catch (Exception e) {
            log.warn("Сообщение '" + msg + "' не получилось отправить");
            log.warn(e.getMessage(), e);
        }
    }

    public void run() {
        try {
            listener = new ServerSocket(DEFAULT_PORT);
            socket = listener.accept();
            input = new DataInputStream(socket.getInputStream());
            output= new DataOutputStream(socket.getOutputStream());
            inputHandler = new InputHandler();
            inputHandler.setDaemon(true);
            inputHandler.start();
            showIP();
            while (isRunning()) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void showIP() {

        try {
            log.info("ip address: " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException host) {
            log.error("IP сервера не получен", host);
        }
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
