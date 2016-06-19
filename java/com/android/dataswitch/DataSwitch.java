package com.android.dataswitch;

import android.net.ConnectivityManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ujjwalmishra on 6/12/16.
 */
public class DataSwitch  extends Thread {
    private ServerSocket serverSocket;
    private int portNumber;
    private ConnectivityManager connectivityManager;
    private Method getMobileDataEnabled , setMobileDataEnabled;
    public boolean socketOpened = false;

    public  DataSwitch(int portno,ConnectivityManager connectivityManagerInstance)  {


        connectivityManager = connectivityManagerInstance;
        portNumber = portno;


        try {
            serverSocket = new ServerSocket(portNumber);
            Class connectivityManagerClass = connectivityManager.getClass();
            getMobileDataEnabled = connectivityManagerClass.getMethod("getMobileDataEnabled");
            setMobileDataEnabled = connectivityManagerClass.getMethod("setMobileDataEnabled",boolean.class);
            Log.e("Server :","Started");
            socketOpened = true;
        }
        catch(Exception e)
        {
            socketOpened = false;
            Log.e("Exception Message :",e.getMessage());
        }
    }
    public void run()
    {
        while(socketOpened)
        {
            try (

                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out =
                            new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));
            ) {

                String inputLine, outputLine;

                // Initiate conversation with client
                if(!(boolean)getMobileDataEnabled.invoke(connectivityManager))
                    out.println("off");
                else
                out.println("on");


                while ((inputLine = in.readLine()) != null) {

                    if (inputLine.equals("on"))
                    {


                        if(!(boolean)getMobileDataEnabled.invoke(connectivityManager)) {

                            setMobileDataEnabled.invoke(connectivityManager,true);
                            out.println("on");
                        }
                    }
                    else if(inputLine.equals("off"))
                    {

                        if((boolean)getMobileDataEnabled.invoke(connectivityManager)) {

                            setMobileDataEnabled.invoke(connectivityManager,false);
                            out.println("off");
                        }
                    }
                    else if(inputLine.equals("turn off"))
                    {
                        socketOpened=false;
                        out.close();
                        in.close();
                        serverSocket.close();
                        break;
                    }

                }
            } catch (IOException e) {
                Log.e("Connection Message :","Exception caught when trying to listen on port "
                        + portNumber + " or listening for a connection");
                Log.e("Exception Message :",e.getMessage());
            }
            catch(Exception e)
            {

            }
        }
    }
}
