/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crp;

import static crp.GraphDraw.edges;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author rajor
 */
public class CRP {

    /**
     * @param args the command line arguments
     */
    public static final double FACT = 1.5;
    public static ArrayList<Node> nodes;
    public static ArrayList<Edge> edges;
    public static final int W_H = (int)(1920.0 / FACT);
    public static final int W_W = (int)(1080 / FACT);
    public static final int BLOCK_SIZE = 70;
    public static int ID = 0;
    public static final int NODES_PER_BLOCK = 8;
    public static final int NEIGHBOR_RAD = 60;
    public static int[][] dist;
    public static Node[][] next;

    //parametres
    public static double Alpha = .80;
    public static double Beta = 0.00;
    public static double Gama =Alpha;
    public static double CaptureProbablity = .5;
    public static int NumPack = 1000;
    public static int Degree_of_effectiveness = 1;
    static int loop = 1;

    public static int routing = 2;

    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        int N = 0;
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();

        for (int x = 60; x < W_H - BLOCK_SIZE - 60; x += BLOCK_SIZE) {
            for (int y = 70; y < +W_W - BLOCK_SIZE - 60; y += BLOCK_SIZE) {
                for (int i = 0; i < NODES_PER_BLOCK; i++) {

                    int xx = (int) (x + Math.random() * (BLOCK_SIZE));
                    int yy = (int) (y + Math.random() * BLOCK_SIZE);

                    if (nearest_node_distance(xx, yy) > 15) {
                        nodes.add(new Node(N++, xx, yy));
                    }

                }
            }
        }

        System.out.println("#Nodes:" + N);
        cal_neighbor();
        System.out.println("#Edges:" + edges.size());
        GraphDraw frame = new GraphDraw(nodes, edges);
        frame.setSize(W_H, W_W);
       // frame.setAlwaysOnTop(true);
        frame.setVisible(true);

        dist = new int[N + 1][N + 1];
        next = new Node[N + 1][N + 1];
        for (int i = 0; i <= N; i++) {
            for (int j = 0; j <= N; j++) {
                dist[i][j] = 1000;
                next[i][j] = null;
            }
        }
        for (Edge e : edges) {
            dist[e.V1.ID][e.V2.ID] = 1;
            dist[e.V2.ID][e.V1.ID] = 1;
            next[e.V1.ID][e.V2.ID] = e.V2;
            next[e.V2.ID][e.V1.ID] = e.V1;
        }

        for (int k = 0; k <= N; k++) {
            for (int i = 0; i <= N; i++) {
                for (int j = 0; j <= N; j++) {
                    if (dist[i][j] > (dist[i][k] + dist[k][j])) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }

                }
            }
        }

        for (int i = 0; i <= N; i++) {
            dist[i][i] = 0;
        }
        /*
        for (int i = 0; i <= N; i++) {
            for (int j = 0; j <= N; j++) {
                System.out.print(dist[i][j] + " ");
            }
            System.out.println("");
        }

        for (int i = 0; i <= N; i++) {
            for (int j = 0; j <= N; j++) {
                if (next[i][j] != null) {
                    System.out.print(next[i][j].ID + " ");
                } else {
                    System.out.println("*");
                }
            }
            System.out.println("");

        }
         */
//routing part starts

        Node S = nodes.get(15 + (int) (Math.random() * N / 6));
        Node D = nodes.get(3 * N / 4 + (int) (Math.random() * N / 4));

        S = nearest_node(W_W / 4, W_H / 4);
        D = nearest_node(4 * W_W / 3, W_H / 4);
        S.color = Color.BLUE;

        D.color = Color.MAGENTA;

        while (loop-- > 0) {

            for (Edge e : edges) {
                e.color = e.initColor;
            }
            Alpha -= .05;
            Gama = Alpha;
            System.out.print(Alpha + "\t");

            if (routing == 1 || routing == 0) {
                Packet.globlhop_count = 0;
                Packet.GlobalQueuingTime = 0;
                int pack_send = 0;
                int message_delivered = 0;
                while (true) {

                    if (NumPack != pack_send) {
                        Packet p = new Packet(S.ID, D.ID);
                        S.sendMessage(p);
                        pack_send++;
                    }
                    for (Node n : frame.nodes) {

                        int QueuingTime = 0;
                        while (n.MessageQueue.size() > 0) {
                            QueuingTime++;
                            Packet p = n.MessageQueue.remove();
                            double r = Math.random();
                            if (p.D != n.ID || r >CaptureProbablity) {
                                //  System.out.println("routing from " + n.ID + " to " + D.ID);
                                Node Next_Hop = RoutingProtocol(n, p);

                                Next_Hop = RoutingProtocol(n, p);

                                Next_Hop.sendMessage(p);
                                p.HOP_C++;
                                Packet.incGlobalHop();
                                Packet.incQueuingTime(QueuingTime);
                                Edge e = find_edge(n.ID, Next_Hop.ID);
                                frame.EdgeColor(e, Color.red);
                            } else {
                                // n.sendMessage(p);
                                message_delivered++;
                              //  Packet.incGlobalHop();
                             //   Packet.incQueuingTime(QueuingTime);
                                //System.out.println("Message Delivered:" + message_delivered);
                            }
                        }

                    }

                    if (message_delivered == NumPack) {
                        // System.out.println("break");
                        frame.repaint();
                        break;
                    }

                    // Thread.sleep(500);
                }
                double Hop_C = Packet.globlhop_count;
                int QueuingTime = Packet.GlobalQueuingTime;
                double HopC_Avg = Hop_C / NumPack;
                double QueuingTime_Avg = QueuingTime / NumPack;
                System.out.print(HopC_Avg + "\t" + QueuingTime_Avg + "\t");
            }
            if (routing == 2 || routing == 0) {
                Packet.globlhop_count = 0;
                Packet.GlobalQueuingTime = 0;
                int pack_send = 0;
                int message_delivered = 0;
                while (true) {

                    if (NumPack != pack_send) {
                        Packet p = new Packet(S.ID, D.ID);
                        S.sendMessage(p);
                        pack_send++;
                    }
                    for (Node n : frame.nodes) {
                        int QueuingTime = 0;
                        while (n.MessageQueue.size() > 0) {
                            QueuingTime++;
                            Packet p = n.MessageQueue.remove();
                            double r = Math.random();
                            if (p.D != n.ID || r < CaptureProbablity) {
                                //   System.out.println("routing from " + n.ID + " to " + D.ID);
                                Node Next_Hop = RoutingProtocol(n, p);

                                Next_Hop = RoutingProtocol2(n, p);

                                Next_Hop.sendMessage(p);
                                p.HOP_C++;
                                Packet.incGlobalHop();
                                Packet.incQueuingTime(QueuingTime);
                                Edge e = find_edge(n.ID, Next_Hop.ID);
                                frame.EdgeColor(e, Color.blue);
                            } else {
                                // n.sendMessage(p);
                                message_delivered++;
                             //   Packet.incGlobalHop();
                              //  Packet.incQueuingTime(QueuingTime);
                                //  System.out.println("Message Delivered:" + message_delivered);
                            }
                        }

                    }

                    if (message_delivered == NumPack) {
                        //  System.out.println("break");
                        frame.repaint();
                        break;
                    }

                    // Thread.sleep(500);
                }
                double Hop_C = Packet.globlhop_count;
                double QueuingTime = Packet.GlobalQueuingTime;
                double HopC_Avg = Hop_C / NumPack;
                double QueuingTime_Avg = QueuingTime / NumPack;
                System.out.println(HopC_Avg + "\t" + QueuingTime_Avg);
            }
            Thread.sleep(10000);
        }

    }

    public static Node RoutingProtocol(Node n, Packet p) {
        Node SP_Next = next[n.ID][p.D];
        int RandomIndex = (int) (Math.random() * Integer.MAX_VALUE) % n.Adj.size();
        Node Random = n.Adj.get(RandomIndex);

        if (Math.random() > Alpha) {
            return SP_Next;
        }

        return Random;
    }

    public static Node RoutingProtocol2(Node n, Packet p) {
        Node SP_Next = next[n.ID][p.D];
        int RandomIndex = (int) (Math.random() * Integer.MAX_VALUE) % n.Adj.size();
        Node Random = n.Adj.get(RandomIndex);

        double ds = dist[n.ID][p.S];
        double dd = dist[n.ID][p.D];
        double d = Math.min(ds, dd);
        //  d = dd;
        double AlphaCrp = 1;
        if (d > 0) {
            AlphaCrp = Beta + Gama / (Math.pow(d, Degree_of_effectiveness));
        }
        // System.out.println("d:" + d + "Alpha:" + AlphaCrp);
        if (Math.random() < AlphaCrp) {
            // System.out.println("Rand Routing");
            return Random;
        }
        //System.out.println("SP Routing");
        return SP_Next;
    }

    static void cal_neighbor() {
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                Node I = nodes.get(i);
                Node J = nodes.get(j);
                if (dist(I, J) <= NEIGHBOR_RAD) {
                    edges.add(new Edge(I, J));
                    if (!I.Adj.contains(J)) {
                        I.Adj.add(J);
                    }
                    if (!J.Adj.contains(I)) {
                        J.Adj.add(I);
                    }

                }
            }

        }
    }

    static double dist(Node x, Node y) {
        return Math.sqrt((x.x - y.x) * (x.x - y.x) + (x.y - y.y) * (x.y - y.y));
    }

    static Edge find_edge(double v1, double v2) {
        for (int pp = 0; pp < edges.size(); pp++) {
            Edge e = edges.get(pp);
            if ((e.V1.ID == v1) && (e.V2.ID == v2)) {
                return e;
            }
            if ((e.V1.ID == v2) && (e.V2.ID == v1)) {
                return e;
            }
        }
        return null;
    }

    static double nearest_node_distance(int x1, int y1) {
        double min = Double.MAX_VALUE;
        int nn = -1;

        for (int j = 0; j < nodes.size(); j++) {
            Node x = nodes.get(j);
            double dist = Math.sqrt((x.x - x1) * (x.x - x1) + (x.y - y1) * (x.y - y1));
            if (dist <= min) {

                min = dist;
                nn = j;
            }
        }
        return min;
    }

    static Node nearest_node(int x1, int y1) {
        double min = Double.MAX_VALUE;
        Node nn = null;

        for (int j = 0; j < nodes.size(); j++) {
            Node x = nodes.get(j);
            double dist = Math.sqrt((x.x - x1) * (x.x - x1) + (x.y - y1) * (x.y - y1));
            if (dist <= min) {

                min = dist;
                nn = x;
            }
        }
        return nn;
    }

}
