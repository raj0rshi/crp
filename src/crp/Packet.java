/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crp;

/**
 *
 * @author rajor
 */
public class Packet {

    public static int globlhop_count = 0;
    public static int GlobalQueuingTime = 0;
    int S;
    int D;
    int HOP_C;
    int QueuingTime;

    public Packet(int s, int d) {
        S = s;
        D = d;
        HOP_C = 0;
        QueuingTime = 0;
    }

    public static void incGlobalHop() {
        globlhop_count++;
    }

    public static void incQueuingTime(int t) {
        GlobalQueuingTime +=t;
        
    }

}
