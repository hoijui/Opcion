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
 * ListPanel.java
 *
 * Created on 23 February 2004, 08:32
 */
package FontViewer.components;

public interface ListPanel {
    public int getNumItems();
    
    public String[] getItem(int itemNumber);
    
    public String[] getCurrentItem();
    
    public int getCurrentItemNum();
    
    public void selectItem(String name, String loc);
    
    public void selectNext();
    
    public void selectPrev();
}