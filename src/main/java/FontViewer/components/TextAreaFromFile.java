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
 * TextAreaFromFile.java
 *
 * Created on 14 February 2004, 07:04
 */
package FontViewer.components;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class TextAreaFromFile extends JTextArea {
    private String filename;
    
    /** Creates a new instance of TextAreaFromFile */
    public TextAreaFromFile(String filename) {
        super();
        super.setEditable(false);
        super.setLineWrap(true);
        super.setWrapStyleWord(true);
        this.filename = filename;
        
        try {
            setContent();
        } catch (IOException ioe) {
            super.setText("Error loading " + filename);
        }
    }
    
    private void setContent() throws IOException {
        ClassLoader cl = this.getClass().getClassLoader();
        InputStreamReader fr = new InputStreamReader(cl.getResource("FontViewer/resources/texts/" + filename).openStream());
        BufferedReader in = new BufferedReader(fr);
        
        String s = "";
        
        while ((s = in.readLine()) != null) {
            super.append(s);
            super.append("\n");
        }
        
        super.setCaretPosition(0);
    }
}