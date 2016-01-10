package jsf32week15;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rvanduijnhoven on 09/01/2016.
 */
public class Server {
    public static void main(String[] args) {
        try {
            //Set up a server socket
            ServerSocket socket = new ServerSocket(8189);

            //Wait for a client connection
            Socket client = socket.accept();
            System.out.println("Client has connected.");

            try {
                OutputStream out = client.getOutputStream();
                InputStream in = client.getInputStream();

                ObjectOutputStream outObject = new ObjectOutputStream(out);
                ObjectInputStream inObject = new ObjectInputStream(in);

                //Now let's try reading something the client sent us.
                boolean finished = false;
                Object incomingObject = null;
                while (!finished)
                {
                    try {
                        incomingObject = inObject.readObject();
                        if (incomingObject instanceof Integer)
                        {
                            int x = (int)incomingObject;
                            //Now create the fractal, and send the edges back.
                            KochFractal koch = new KochFractal();
                            koch.setLevel(x);
                            koch.generateBottomEdge();
                            koch.generateRightEdge();
                            koch.generateLeftEdge();
                            List<Edge> edges = koch.getEdges();
                            outObject.writeObject(edges);
                            out.flush();
                        }
                        else
                        {
                            outObject.writeObject("That was not an integer!");
                        }
                    }
                    catch (Exception ex) {}
                }

            }
            catch (Exception ex) {ex.printStackTrace();}
            finally {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}