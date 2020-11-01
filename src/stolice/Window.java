/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stolice;

import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *wwwdd
 * @author Ania
 */
public class Window extends Canvas {
    
    
    public Window(int width, int height, String title, Game game) {
        JFrame frame = new JFrame(title);
        
        frame.setPreferredSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); //nwm
        frame.add(game);
        frame.setVisible(true);
        game.start();
    }
}
