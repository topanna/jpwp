/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stolice;

/**
 *
 * @author Ania
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;

public class Start {
    Graphics g;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Stolice Europy");
        frame.setSize(new Dimension(1024, 768));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Map map = new Map();
        frame.add(map); //getcontentpane
        frame.pack();
        frame.setVisible(true);

    }
}
