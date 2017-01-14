package crp;

import java.util.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class GraphDraw extends JFrame {

    int width;
    int height;

    public static ArrayList<Node> nodes;
    public static ArrayList<Edge> edges;

    public GraphDraw(ArrayList nodes, ArrayList edges) { //Construct with label
        this.setTitle("Graph Draw");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.WHITE);
        this.nodes = nodes;
        this.edges = edges;
        width = 10;
        height = 10;

    }


    synchronized public void paint(Graphics g) { // draw the nodes and edges
        // System.out.println("drawing");
        FontMetrics f = g.getFontMetrics();
        int nodeHeight = height;

        for (int pp = 0; pp < edges.size(); pp++) {
            Edge e = edges.get(pp);
            g.setColor(e.color);
            Node xx = e.V1;
            Node yy = e.V2;
            g.drawLine(xx.x, xx.y, yy.x, yy.y);
        }
        //  g.setColor(Color.black);
        for (Node n : nodes) {

            int nodeWidth = width;
            g.setColor(n.color);
            g.fillOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2, nodeWidth, nodeHeight);
            g.setColor(Color.white);
            g.drawOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2, nodeWidth, nodeHeight);
            //  g.drawString(n.ID + "", n.x - f.stringWidth(n.ID + "") / 2, n.y + f.getHeight() / 2);

        }
    }

    synchronized public void EdgeColor(Edge e, Color c) {

        e.color = c;
        //this.repaint();

    }

}
