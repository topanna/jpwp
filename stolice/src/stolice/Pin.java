/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stolice;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ania
 */
public class Pin {

    static int playerX;
    static int playerY;
    static int[] actualX = new int[3];
    static int[] actualY = new int[3];

    Image playerPin;
    Image actualPin;

    static List<Point> points;

    Pin() {
        this.playerPin = Images.blackPin;
        this.actualPin = Images.redPin;
    }

    public static void initActualPoints() {
        points = new ArrayList<Point>();
        points.add(new Point(587, 408));
        points.add(new Point(494, 409));
        points.add(new Point(660, 360));
    }

    public static int getActualX(int i) {
        Pin.actualX[i] = (int) points.get(i).getX();
        return Pin.actualX[i];
    }

    public static int getActualY(int i) {
        Pin.actualY[i] = (int) points.get(i).getY();
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

}
