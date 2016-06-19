package com.android.dataswitch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by ujjwalmishra on 6/14/16.
 */

/**
 * This class inherits from Thread class.
 * This class is created to close the DataSwitch thread by sending "turn off" message.
 */
public class stopServerThread extends Thread {
    @Override
    public void run() {
        super.run();
        String hostName = "127.0.0.1";
        int portNumber = 2500;

        try (
                Socket kkSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);

        ) {

            out.println("turn off");



        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);

        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);

        }
    }


}
