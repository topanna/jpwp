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

/**
 *
 * @author Ania
 */
public class Map extends JPanel implements MouseListener {

    Image img = null;
    Image blackPin = null;
    Image redPin = null;
    int x = 0;

    List<Point> points;
    int[] playerPoint = new int[2];
    int[] actualPoint = new int[2];
    
    //int pin = 0;
    
    boolean mouseClicked = false;
    boolean mouseReleased = false;

    public Map() {
        setPreferredSize(new Dimension(1024, 768));
        setFocusable(true); //dddd
        addMouseListener(this);
        points = new ArrayList<Point>();
        points.add(new Point(587, 408));
        points.add(new Point(494, 409));
        points.add(new Point(660, 360));

    }

    public void showPin(int x, int y, Image img, Graphics g) {

       
        if (mouseClicked) {
            g.drawImage(img, x, y, null);
        }

    }
    
    public void showRedPin(int x, int y, Image img, Graphics g) {

       
        if (mouseReleased) {
            g.drawImage(img, x, y, null);
        }

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        try {
            //img = ImageIO.read(new File("build\\classes\\res\\map.png"));
            //img = new ImageIcon(this.getClass().getResource("map.png")).getImage();
            img = ImageIO.read(getClass().getResource("/res/map.png"));

            
            blackPin = ImageIO.read(getClass().getResource("/res/pinblack.png"));
            redPin = ImageIO.read(getClass().getResource("/res/pinred.png"));
        } catch (IOException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        }

        g.drawImage(img, 0, 0, null);

        showPin(playerPoint[0] - 12, playerPoint[1] - 38, blackPin, g);
        showRedPin(actualPoint[0] - 12, actualPoint[1] - 38, redPin, g);
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseClicked = true;
        playerPoint[0] = e.getX();
        playerPoint[1] = e.getY();
        actualPoint[0] = (int) points.get(0).getX();
        actualPoint[1] = (int) points.get(0).getY();
        
        System.out.println(playerPoint[0] + ", " + playerPoint[1]);
        System.out.println(actualPoint[0] + ", " + actualPoint[1]);
        
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseReleased = true;
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
//        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
