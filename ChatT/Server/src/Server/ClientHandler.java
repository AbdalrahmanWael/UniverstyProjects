package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private OutputStream outStream;
    private InputStream inputStream;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    private String name = "DEFAULTHANDLER";
    private Message message;

    private boolean first = true;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.message = null;
        clientHandlers.add(this);

    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            boolean stop = readStream();
            if (stop) {
                break;
            }
        }
    }

    public boolean readStream() {

        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            int infoLength = this.dataInputStream.readInt();
            if (infoLength > 0) {
                byte[] infoBytes = new byte[infoLength];
                this.dataInputStream.readFully(infoBytes, 0, infoBytes.length);

                int contentLength = this.dataInputStream.readInt();

                if (contentLength > 0) {
                    byte[] content = new byte[contentLength];
                    this.dataInputStream.readFully(content, 0, contentLength);

                    this.message = new Message(new String(infoBytes));
                    this.message.setData(content);
                    if (this.first) {
                        this.name = this.message.name;
                        first = false;
                    }
                    System.out.println("ClientHandler Thread(" + message.name + "):  " + message.getInfo());
                    broadcastMessage();
                    return false;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (IOException ex) {
            closeAll(socket, buffReader, buffWriter, outStream, inputStream);
            return true;
        }
    }

    public void broadcastLine(String messageToSend) {
        System.out.println(messageToSend);
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.name.equals(name)) {
                    clientHandler.buffWriter.write(messageToSend);
                    clientHandler.buffWriter.newLine();
                    clientHandler.buffWriter.flush();
                }
            } catch (IOException e) {
                closeAll(socket, buffReader, buffWriter, outStream, inputStream);
            }
        }
    }

    public void sendCustomMessage(Message msg) {
        try {
            //
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());

            byte[] info = msg.getInfo().getBytes(); // U8?
            byte[] content = msg.getData();

            this.dataOutputStream.writeInt(info.length);
            this.dataOutputStream.write(info);

            this.dataOutputStream.writeInt(content.length);
            this.dataOutputStream.write(content);

        } catch (IOException ex) {
            closeAll(socket, buffReader, buffWriter, outStream, inputStream);
        }
    }

    public void sendMessage(ClientHandler client) {
        try {
            this.dataOutputStream = new DataOutputStream(client.socket.getOutputStream());

            byte[] info = this.message.getInfo().getBytes(); // U8?
            byte[] content = this.message.getData();

            this.dataOutputStream.writeInt(info.length);
            this.dataOutputStream.write(info);

            this.dataOutputStream.writeInt(content.length);
            this.dataOutputStream.write(content);

        } catch (IOException ex) {
            closeAll(socket, buffReader, buffWriter, outStream, inputStream);
        }
    }

    public static void sendMessage(ClientHandler client, Message msg) {
        try {
            client.dataOutputStream = new DataOutputStream(client.socket.getOutputStream());

            byte[] info = msg.getInfo().getBytes(); // U8?
            byte[] content = msg.getData();

            client.dataOutputStream.writeInt(info.length);
            client.dataOutputStream.write(info);

            client.dataOutputStream.writeInt(content.length);
            client.dataOutputStream.write(content);

        } catch (IOException ex) {
            System.out.println("Failed to send");
            //client.closeAll(socket, buffReader, buffWriter, outStream, inputStream);
        }
    }

    public static void SendClient(String name, Message msg) {
        for (ClientHandler client : clientHandlers) {
            if (client.name.equals(name)) {
                sendMessage(client, msg);
            }
        }
        //System.out.println("Didn't find client");
    }

    public void broadcastMessage() {
        if (message.type == Message.types.TX) {
            System.out.println("Broadcasting text data: " + this.message.getDataAsString());
        } else {
            System.out.println("Broadcasting file data: " + message.getFileName());
            DatabaseManager.DownloadFile(this.message);
            System.out.println("Downloaded");
        }

        for (ClientHandler clientHandler : clientHandlers) {
            sendMessage(clientHandler);
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        System.out.println("server " + name + " has gone");
    }

    public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter, OutputStream outputStream, InputStream inputStream) {
        removeClientHandler();
        try {
            if (buffReader != null) {
                buffReader.close();
            }
            if (buffWriter != null) {
                buffWriter.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
