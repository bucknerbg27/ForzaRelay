package wauz.forza.relay;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Properties;

public class ForzaDataOutReader {
    private boolean stopProcessing = false;
    private  InetAddress targetIP;
    private int targetPort;

    public void stopProcessing() {
        this.stopProcessing = true;
    }

    public ForzaDataOutReader() {

     }

    public void startRelay(int port, InetAddress tAddress, int tPort) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    // Create a socket to listen on the port.
                    DatagramSocket dsocket = new DatagramSocket(port);
                    DatagramSocket ssocket = new DatagramSocket();

                    //Excess Bytes will be discarded
                    byte[] buffer = new byte[500];

                    // Create a packet to receive data into the buffer
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    while (!stopProcessing) {
                        // Wait to receive a datagram
                        dsocket.receive(packet);
                        final byte[] thisBuffer = packet.getData();
                        //immediately forward data
                        Platform.runLater(() -> {
                            try {
                                DatagramPacket spacket = new DatagramPacket(thisBuffer,thisBuffer.length, tAddress,tPort);
                                ssocket.send(spacket);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        // Reset the length of the packet before reusing it.
                        packet.setLength(buffer.length);
                    }
                    System.out.println("closed socket listener");
                } catch (Exception e) {
                    System.err.println(e);
                    System.exit(1);
                }
            }

        }.start();

    }

    private ByteBuffer getBytes(byte[] buffer, int offset, int length) {
        return ByteBuffer.wrap(Arrays.copyOfRange(buffer, offset, length)).order(ByteOrder.LITTLE_ENDIAN);
    }

}
