package chatt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    // private classes for the client
    private Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private OutputStream outStream;
    private InputStream inputStream;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    private String name;

    public Client(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
    }

    public void sendMessage(Message msg) {
        try {
            //
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());

            byte[] info = msg.getInfo().getBytes();
            byte[] content = msg.getData();

            this.dataOutputStream.writeInt(info.length);
            this.dataOutputStream.write(info);

            this.dataOutputStream.writeInt(content.length);
            this.dataOutputStream.write(content);

        } catch (IOException ex) {
            closeAll(socket, buffReader, buffWriter, outStream, inputStream);
        }
    }

    public boolean readStream(Message message) {
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

                    message = new Message(new String(infoBytes));
                    message.setData(content);

                    MessageManager.MessageRecieved(message);

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

    public void readMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = null;
                while (socket.isConnected()) {
                    boolean stop = readStream(message);
                    if (stop) {
                        break;
                    }
                }
            }
        }).start();
    }
    
    public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter, OutputStream outputStream, InputStream inputStream) {

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
