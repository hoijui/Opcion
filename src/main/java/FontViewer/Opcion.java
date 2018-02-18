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
 * Opcion.java
 *
 * Created on 30 January 2004, 16:40
 */
package FontViewer;

import FontViewer.windows.*;

public class Opcion {
    public static void main(String args[]) {
        Splash s = new Splash("Logo.gif");
        s.setVisible(true);
        MainWindow mw = new MainWindow();
        s.dispose();
        mw.setVisible(true);
    }
}