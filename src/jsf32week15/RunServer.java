package jsf32week15;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rvanduijnhoven on 09/01/2016.
 */
public class RunServer {
    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }
}
