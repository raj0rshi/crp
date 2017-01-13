/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crp;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author rajor
 */
public class Node {

    int x;
    int y;
    int ID;
    Color color = Color.darkGray;
    public Queue<Packet> MessageQueue = new LinkedList<Packet>();
    public LinkedList<Node> Adj = new LinkedList<Node>();

    public Node(int id, int X, int Y) {
        x = X;
        y = Y;
        ID = id;
    }

    public Node(int id) {
        x = 0;
        y = 0;
        ID = id;
    }

    void sendMessage(Packet e) {
        MessageQueue.add(e);
    }
}
