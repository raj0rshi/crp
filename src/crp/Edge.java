/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crp;

import java.awt.Color;

/**
 *
 * @author rajor
 */
public class Edge {

    Node V1;
    Node V2;
    Color color = Color.lightGray;

    public Edge(Node v1, Node v2) {
        V1 = v1;
        V2 = v2;
    }

    public Edge(int v1, int v2) {
        V1 = CRP.nodes.get(v1);
        V2 = CRP.nodes.get(v2);
    }
}
