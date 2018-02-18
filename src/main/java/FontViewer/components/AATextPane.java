/*
  Opcion Font Viewer
  Copyright (C) 2004 Paul Chiu. All Rights Reserved.

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
*/

/*
 * AATextPane.java
 *
 * Created on 19 Feburary 2004, 11:30
 */
package FontViewer.components;

import java.awt.*;
import javax.swing.*;

public class AATextPane extends JTextPane{
    public void paintComponent(Graphics g) {
        Graphics2D g2d = null;
        if ( g instanceof Graphics2D ) {
            g2d = ( Graphics2D)g ;
            g2d.addRenderingHints(new RenderingHints(
            RenderingHints.KEY_ANTIALIASING ,
            RenderingHints.VALUE_ANTIALIAS_ON ));
        }
        super.paintComponent(g);
    }
}