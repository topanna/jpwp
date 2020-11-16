/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stolice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Ania
 */
public class GamePanel extends JPanel implements MouseListener {

    boolean mouseClicked = false;
    boolean mouseReleased = false;
    int i = 0;
    private Timer timer;
    double diff;
    JLabel scoreLabel;
    String distance = "";

    public GamePanel() {
        setPreferredSize(new Dimension(1024, 768));
        setFocusable(true); //dddd
        addMouseListener(this);
        //Pin.initActualPoints();
        Pin.initActualCapitals();
        Images.getImages();
        timer = new Timer(100, new GameLoop(this));
        timer.start();
        scoreLabel = new JLabel(distance, JLabel.CENTER);
        scoreLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
        scoreLabel.setBackground(Color.YELLOW);
        scoreLabel.setOpaque(true);
        scoreLabel.setBounds(12, 676, 1000, 80);
        this.setLayout(null);
        this.add(scoreLabel);

    }

    public void showPlayerPin(Graphics g) {

        if (mouseClicked) {
            g.drawImage(Images.blackPin, Pin.getPlayerX() - 12, Pin.getPlayerY() - 38, null); //przesuniecie

        }

    }

    public void showActualPin(Graphics g) {

        if (mouseClicked) {

            g.drawImage(Images.redPin, Pin.getActualX(i) - 12, Pin.getActualY(i) - 38, null);

        }

    }

    public void startRound(Graphics g) {

        g.setFont(new Font("SansSerif", Font.PLAIN, 28));
        g.drawString(Pin.capitals.get(i).getName(), 30, 50);
        scoreLabel.setText(Pin.capitals.get(i).getName());

    }

    public void displayScore(Graphics g) {

        if (mouseClicked) {

            scoreLabel.setText("Dystans: " + diff);
        }

    }

    public void initMap() {
    }

    @Override
    public void paintComponent(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paintComponent(g);
        g.drawImage(Images.map, 0, 0, null);
        startRound(g);
        showPlayerPin(g);
        showActualPin(g);

        displayScore(g);
        // System.out.println("REPAINT");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseClicked = true;
        i = i + 1;
        Pin.loadPlayerXY(e.getX(), e.getY());

        System.out.println(Pin.getPlayerX() + ", " + Pin.getPlayerY());

        System.out.println(Pin.getActualX(i) + ", " + Pin.getActualY(i));

        diff = Pin.getDifferenceInPx(Pin.getPlayerX(), Pin.getPlayerY(), Pin.getActualX(i), Pin.getActualY(i));
        System.out.println(diff);

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

    }

}
