/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stolice;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Ania
 */
public class GamePanel extends JPanel implements MouseListener {



    boolean mouseClicked = false;
    boolean mouseReleased = false;

    private Timer timer;

    public GamePanel() {
        setPreferredSize(new Dimension(1024, 768));
        setFocusable(true); //dddd
        addMouseListener(this);
        Pin.initActualPoints();

        Images.getImages();
        timer = new Timer(50, new GameLoop(this));
        timer.start();

    }

    public void showPlayerPin(Graphics g) {

        if (mouseClicked) {
            g.drawImage(Images.blackPin, Pin.getPlayerX() - 12, Pin.getPlayerY() - 38, null); //przesuniecie
        }

    }

    public void showActualPin(Graphics g) {

        if (mouseClicked) {

            g.drawImage(Images.redPin, Pin.getActualX(0) - 12, Pin.getActualY(0) - 38, null);
        }

    }

    public void initMap() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Images.map, 0, 0, null);

        showPlayerPin(g);
        showActualPin(g);

        // System.out.println("REPAINT");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseClicked = true;
        Pin.loadPlayerXY(e.getX(), e.getY());

        System.out.println(Pin.getPlayerX() + ", " + Pin.getPlayerY());
        System.out.println(Pin.getActualX(0) + ", " + Pin.getActualY(0));

        //repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void doOneLoop() {
        update();
        repaint();
    }

    private void update() {
        // System.out.println("UPDATE");
    }

}
