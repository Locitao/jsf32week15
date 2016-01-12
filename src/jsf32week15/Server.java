package jsf32week15;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rvanduijnhoven on 12/01/2016.
 */
public class Server {
    private ExecutorService pools;
    public void startServer() {
        pools = Executors.newFixedThreadPool(4);
        try {
            //Set up a server socket
            ServerSocket socket = new ServerSocket(8189);

            //Wait for a client connection
            Socket newClient = socket.accept();
            System.out.println("Client has connected.");
            pools.execute(() -> {
                try {
                    Socket client = newClient;
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
                            if (client.isClosed())
                            {
                                client = socket.accept();
                                out = client.getOutputStream();
                                in = client.getInputStream();

                                outObject = new ObjectOutputStream(out);
                                inObject = new ObjectInputStream(in);
                            }
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
                        finally {
                            client.close();
                            System.out.println("Client has disconnected.");
                        }
                    }

                }
                catch (Exception ex) {ex.printStackTrace();}
                finally {
                    try {
                        newClient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
