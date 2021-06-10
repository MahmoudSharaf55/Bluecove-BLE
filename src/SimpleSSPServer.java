import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
//import com.intel.bluetooth.BluetoothRFCommConnectionNotifier;
public class SimpleSSPServer {

    //start server
    private void startServer() throws IOException{
        LocalDevice local = LocalDevice.getLocalDevice();
        System.out.println("Device name: " + local.getFriendlyName());
        System.out.println("Bluetooth Address: " + local.getBluetoothAddress());
        boolean res = local.setDiscoverable(DiscoveryAgent.GIAC);
        System.out.println("Discoverability set: " + res);

        //Create a UUID for SPP
        UUID uuid = new UUID("1101", true);
//        UUID uuid = new UUID("e0cbf06ccd8b4647bb8a263b43f0f974", false); // 446118f08b1e11e29e960800200c9a66 - e0cbf06ccd8b4647bb8a263b43f0f974   -   547c6387-e93c-4caa-9049-c32d628a1c1c
        //Create the servicve url
//        String connectionString = "btgoep://localhost:" + "e0cbf06ccd8b4647bb8a263b43f0f974" +";name=obex";
        String connectionString = "btspp://localhost:" + uuid +";name=obex";

        //open server url
        StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier)Connector.open( connectionString );

        //Wait for client connection
        System.out.println("\nServer Started. Waiting for clients to connect...");
        StreamConnection connection=streamConnNotifier.acceptAndOpen();

        RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
        System.out.println("Remote device address: "+dev.getBluetoothAddress());
        System.out.println("Remote device name: "+dev.getFriendlyName(true));

        //read string from spp client
        InputStream inStream=connection.openInputStream();
        BufferedReader bReader=new BufferedReader(new InputStreamReader(inStream));
        String lineRead=bReader.readLine();
        System.out.println(lineRead);

        //send response to spp client
        OutputStream outStream=connection.openOutputStream();
        PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
        pWriter.write("Response String from SPP Server\r\n");
        pWriter.flush();

        pWriter.close();

        streamConnNotifier.close();

        /*PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
        pWriter.write("Response String from SPP Server\r\n");
        pWriter.flush();
        pWriter.close();*/

    }


    public static void main(String[] args) throws IOException {

        //display local device address and name
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("Address: "+localDevice.getBluetoothAddress());
        System.out.println("Name: "+localDevice.getFriendlyName());

        SimpleSSPServer sampleSPPServer=new SimpleSSPServer();
        sampleSPPServer.startServer();

    }
}