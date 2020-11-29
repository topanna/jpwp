/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stolice;

import java.awt.Image;
import java.awt.Point;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ania
 */
public class Pin {

    static int playerX;
    static int playerY;
    static int[] actualX = new int[50];
    static int[] actualY = new int[50];

    Image playerPin;
    Image actualPin;

    static List<Point> points;
    static ArrayList<Capital> capitals;

    Pin() {
        this.playerPin = Images.blackPin;
        this.actualPin = Images.redPin;
    }

//    public static void initActualPoints() {
//        points = new ArrayList<Point>();
//        points.add(new Point(0, 0));
//        points.add(new Point(587, 408));
//        points.add(new Point(494, 409));
//        points.add(new Point(660, 360));
//    }

    public static void initActualCapitals() {
        capitals = new ArrayList<Capital>();
        capitals.add(new Capital(1, "WARSZAWA, Polska", 587, 408));
        capitals.add(new Capital(2, "BERLIN, Niemcy", 494, 409));
        capitals.add(new Capital(3, "MIŃSK, Białoruś", 660, 360));
        capitals.add(new Capital(4, "PRAGA, Czechy", 506, 461));
        capitals.add(new Capital(5, "WIEDEŃ, Austria", 539, 500));
        capitals.add(new Capital(6, "BRATYSŁAWA, Słowacja", 554, 495));
    }

    public static int getActualX(int i) {
        Pin.actualX[i] = (int) capitals.get(i - 1).getX(); //i-1 bo najpierw klikniecie i=i+1, dopiero potem wyswietla
        return Pin.actualX[i];
    }

    public static int getActualY(int i) {
        Pin.actualY[i] = (int) capitals.get(i - 1).getY();
        return Pin.actualY[i];
    }

    public static void loadPlayerXY(int X, int Y) {
        Pin.playerX = X;
        Pin.playerY = Y;
    }

    public static int getPlayerX() {
        return Pin.playerX;
    }

    public static int getPlayerY() {
        return Pin.playerY;
    }

    public static Image getBlackPinImage() {
        return Images.blackPin;
    }

    public static Image getRedPinImage() {
        return Images.redPin;
    }
    
    public static double getDifferenceInKm(int x1, int y1, int x2, int y2) {
        int h = abs(x2 - x1);
        //System.out.println(h);
        int v = abs(y2 - y1);
        //System.out.println(v);
        double diffInPx = sqrt(h*h + v*v);
        return diffInPx/18.25 * 100; //km
    }
    
}
