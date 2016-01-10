package jsf32week15;

import javafx.scene.paint.Color;

import java.io.Serializable;

public class Edge implements Serializable{
    public double X1, Y1, X2, Y2;
    public transient Color color;
    public double color1, color2, color3;

    public Edge(double X1, double Y1, double X2, double Y2, Color color) {
        this.X1 = X1;
        this.Y1 = Y1;
        this.X2 = X2;
        this.Y2 = Y2;
        this.color = color;
        this.color1 = color.getRed();
        this.color2 = color.getGreen();
        this.color3 = color.getBlue();
    }

    @Override
    public String toString()
    {
        String c = String.valueOf(color1) + "," + String.valueOf(color2) + "," + String.valueOf(color3);
        String x = String.valueOf(X1) + "," + String.valueOf(Y1) + "," + String.valueOf(X2) + "," + String.valueOf(Y2) + "," + c;
        return x;
    }
}
