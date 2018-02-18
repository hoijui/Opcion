/*
 * MyImageIcon.java
 *
 * Created on 21 February 2004, 17:53
 */

package FontViewer.resources;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author  Paul
 */
public class MyImageIcon extends javax.swing.ImageIcon {
    private ImageIcon ic;
    
    public MyImageIcon(String imageName) {
        ic = new ImageIcon(this.getClass().getClassLoader().getResource("FontViewer/resources/icons/" + imageName));
    }
    
    public ImageIcon getImageIcon() {
        return ic;
    }
    
    public Image getImage() {
        return ic.getImage();
    }
}
