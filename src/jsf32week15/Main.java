package jsf32week15;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.RunnableFuture;

/**
 * @author rvanduijnhoven
 * Week 12 assignment.
 */
@SuppressWarnings("Duplicates")
public class Main extends Application {

    private double zoomTranslateX = 0.0;
    private double zoomTranslateY = 0.0;
    private double zoom = 1.0;
    private Canvas kochPanel;
    private final int kpWidth = 400;
    private final int kpHeight = 400;
    private int currentLevel = 1;
    List<Edge> edges;
    KochFractal koch;
    private int level = 0;


    @Override
    public void start(Stage primaryStage) {
        //Create four buttons, for each of the read/writing ways.
        Button btnTextNoBuffer = new Button();
        btnTextNoBuffer.setText("Enter a level, and edges ye shall receive!");
        Button btnTextWithBuffer = new Button();
        btnTextWithBuffer.setText("Save and load to text file, with buffer.");
        Button btnBinaryNoBuffer = new Button();
        btnBinaryNoBuffer.setText("Save and load to binary file, no buffer.");
        Button btnBinaryWithBuffer = new Button();
        btnBinaryWithBuffer.setText("Save and load to binary file, with buffer.");

        //Textfield to enter the number of edges + label
        TextField nrOfEdges = new TextField();
        Label lbl = new Label();
        lbl.setText("Enter your desired level.");

        //Label to present the read/write time.
        Label speed = new Label();
        speed.setText("The write speed will be shown here.");
        //New canvas
        kochPanel = new Canvas(kpWidth,kpHeight);
        kochPanel.setTranslateX(100);


        //position the elements
        btnTextNoBuffer.setTranslateY(-80);
        btnTextWithBuffer.setTranslateY(-40);
        btnBinaryWithBuffer.setTranslateY(40);
        btnTextNoBuffer.setTranslateX(-240);
        btnTextWithBuffer.setTranslateX(-240);
        btnBinaryNoBuffer.setTranslateX(-240);
        btnBinaryWithBuffer.setTranslateX(-240);
        nrOfEdges.setTranslateY(-220);
        nrOfEdges.setTranslateX(-310);
        nrOfEdges.setMaxWidth(50);
        lbl.setTranslateX(-240);
        lbl.setTranslateY(-200);
        speed.setTranslateY(-240);


        StackPane root = new StackPane();
        root.getChildren().add(btnTextNoBuffer);
//        root.getChildren().add(btnTextWithBuffer);
//        root.getChildren().add(btnBinaryNoBuffer);
//        root.getChildren().add(btnBinaryWithBuffer);
        root.getChildren().add(nrOfEdges);
        root.getChildren().add(lbl);
        root.getChildren().add(kochPanel);
        root.getChildren().add(speed);

        Scene scene = new Scene(root, 700, 500);

        primaryStage.setTitle("Edges and stuff");
        primaryStage.setScene(scene);
        primaryStage.show();

        //Event handlers for buttons
        btnTextNoBuffer.setOnMouseClicked(event -> {
            int i = Integer.parseInt(nrOfEdges.getText());
//            currentLevel = i;
//            clearKochPanel();
//            createKochFractal(i);
//            double x = 0;
//            try {
//                x = saveTextFileNoBuffer();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            speed.setText(String.valueOf((x / 1000000)));
//            //drawAllEdges();
            String s = getEdges(i);
            clearKochPanel();
            drawAllEdges();
            speed.setText(s);
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Edge edgeAfterZoomAndDrag(Edge e) {
        return new Edge(
                e.X1 * zoom + zoomTranslateX,
                e.Y1 * zoom + zoomTranslateY,
                e.X2 * zoom + zoomTranslateX,
                e.Y2 * zoom + zoomTranslateY,
                Color.WHITE);
    }

    public void clearKochPanel() {
        GraphicsContext gc = kochPanel.getGraphicsContext2D();
        gc.clearRect(0.0,0.0,kpWidth,kpHeight);
        gc.setFill(Color.BLACK);
        gc.fillRect(0.0,0.0,kpWidth,kpHeight);
    }

    public void drawEdge(Edge e) {
        // Graphics
        GraphicsContext gc = kochPanel.getGraphicsContext2D();

        // Adjust edge for zoom and drag
        Edge e1 = edgeAfterZoomAndDrag(e);

        // Set line color
        Color c = new Color(e.color1, e.color2, e.color3, 1);
        gc.setStroke(c);

        // Set line width depending on level
        if (currentLevel <= 3) {
            gc.setLineWidth(1.5);
        }
        else if (currentLevel <=5 ) {
            gc.setLineWidth(1.2);
        }
        else {
            gc.setLineWidth(1.0);
        }

        // Draw line
        gc.strokeLine(e1.X1 * 400,e1.Y1 * 400,e1.X2 * 400,e1.Y2 * 400);
    }

    public void drawAllEdges()
    {
        edges.forEach(this::drawEdge);
    }

    public String getEdges(int x)
    {
        String returnstring = "Success!";
        try {
            Socket socket = new Socket("localhost", 8189);
            try {
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream();

                //Order matters here!
                ObjectOutputStream outObject = new ObjectOutputStream(out);
                ObjectInputStream inObject = new ObjectInputStream(in);
                outObject.writeObject(x);
                outObject.flush();
                edges = (List<Edge>)inObject.readObject();
                System.out.println("Received edges.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                socket.close();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnstring;
    }

}
