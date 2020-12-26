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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.List;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.sqrt;
import java.math.BigDecimal;
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
    int roundsCounter = 0;
    private Timer timer;
    double getDistance;
    JLabel capitalNameLabel, distanceLabel, scoreLabel;
    int timePassed = 0;
    boolean running = false;
    boolean nextRound = false;
    boolean activeDisplay = false;
    String distanceInKm, score;
    double sumScore = 0;
    int timeBonus = 0;
    int width = 1024;
    int totalScore = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(1024, 768));
        setFocusable(true); //dddd
        addMouseListener(this);
        //Pin.initActualPoints();
        Pin.initActualCapitals();
        Images.getImages();
        timer = new Timer(50, new GameLoop(this));
        timer.start();
        running = true;
        activeDisplay = true;
        initLayout();

    }

    public void initLayout() {

        distanceLabel = new JLabel("", JLabel.CENTER);
        capitalNameLabel = new JLabel("", JLabel.CENTER);
        scoreLabel = new JLabel("", JLabel.CENTER);
        distanceLabel.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        distanceLabel.setBackground(new Color(242, 242, 242, 255));
        distanceLabel.setOpaque(true);
        capitalNameLabel.setFont(new Font("TimesRoman", Font.BOLD, 25));
        capitalNameLabel.setBackground(new Color(242, 242, 242, 255));
        capitalNameLabel.setOpaque(true);
        scoreLabel.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        scoreLabel.setBackground(new Color(242, 242, 242, 255));
        scoreLabel.setOpaque(true);
        scoreLabel.setVisible(false);
        distanceLabel.setVisible(false);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(capitalNameLabel, gbc);
        gbc.gridx = 1;
        add(distanceLabel, gbc);
        gbc.gridx = 2;
        add(scoreLabel, gbc);
    }

    public void showPlayerPin(Graphics g) {

        if (mouseClicked) {
            g.drawImage(Images.blackPin, Pin.getPlayerX() - 12, Pin.getPlayerY() - 38, null); //przesuniecie

        }

    }

    public void showActualPin(Graphics g) {

        if (mouseClicked) {

            g.drawImage(Images.redPin, Pin.getActualX(roundsCounter) - 12, Pin.getActualY(roundsCounter) - 38, null);

        }

    }

    public void startRound(Graphics g) {
        if (running && activeDisplay) {             //if delaycounter == 0
            //g.setFont(new Font("SansSerif", Font.PLAIN, 28));
            //g.drawString(Pin.capitals.get(roundCounter).getName(), 30, 50);
            capitalNameLabel.setText(Pin.capitals.get(roundsCounter).getName());
            capitalNameLabel.repaint();
            
        }
    }

    public void endRound() {

        if (mouseClicked) {

        }

    }

    public void displayScore(Graphics g) {

        if (mouseClicked) {
            if (!nextRound) {
                //System.out.println("REPAINT");
                scoreLabel.setText("Punkty: " + score);
                distanceLabel.setText("Dystans: " + distanceInKm + " km");
                scoreLabel.setVisible(true);
                distanceLabel.setVisible(true);
            }
        }
        if (nextRound) {

            scoreLabel.setVisible(false);
            distanceLabel.setVisible(false);
            nextRound = false;
            width = 1024;
        }
    }

    public void initMap() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Images.map, 0, 0, null);
        startRound(g);
        
        g.setColor(Color.GRAY);
        g.fillRect(0, 30, 1024, 8);
        g.setColor(Color.GREEN);
        g.fillRect(0, 30, width, 8);
        
     
        showPlayerPin(g);
        showActualPin(g);

        displayScore(g);
        //System.out.println("REPAINT");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (activeDisplay) {
            mouseClicked = true;
            roundsCounter = roundsCounter + 1;
            Pin.loadPlayerXY(e.getX(), e.getY());
            sumScore();
            //System.out.println(Pin.getPlayerX() + ", " + Pin.getPlayerY());

        }

    }

    private void sumScore() {
        getDistance = Math.round(Pin.getDifferenceInKm(Pin.getPlayerX(), Pin.getPlayerY(), Pin.getActualX(roundsCounter), Pin.getActualY(roundsCounter)));
        //score += getDistance;
        sumScore = Math.floor(Math.exp(-getDistance/1000) * 100) * timeBonus;
        totalScore += sumScore;
        distanceInKm = toString(getDistance);
        score = toString(sumScore);
        System.out.println(totalScore);
        
    }

    private String toString(double d) {
        BigDecimal diffString = new BigDecimal(Double.valueOf(d).toString());
        return diffString.stripTrailingZeros().toPlainString();
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
        if (running == true) {
            if (activeDisplay == true) {
            timePassed++;
            timeBonus = 100 - timePassed;
            width -= 12;
            //System.out.println(timeBonus);
            if (timePassed == 100) {
                nextRound = true;
                mouseClicked = false;
                roundsCounter = roundsCounter + 1;
                timePassed++;
            }
            if (timePassed > 100) {
                timePassed = 0;
                activeDisplay = true;
            }
            if (mouseClicked) {
                timePassed = 0;
            }
            }
        }
       
        
        if (mouseClicked == true) {
            activeDisplay = false;
            timePassed++;
            //System.out.println(timePassed);
            if (timePassed == 50) {
                mouseClicked = false;
                nextRound = true;
                timePassed++;
            }
            if (timePassed > 50) {
                timePassed = 0;
                activeDisplay = true;
            }
        }

        repaint();
    }

}
