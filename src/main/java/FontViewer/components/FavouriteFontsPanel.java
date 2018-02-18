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
 * FavouriteFontsPanel.java
 *
 * Created on 21 February 2004, 18:33
 */
package FontViewer.components;
import FontViewer.windows.*;

import java.util.*;
import javax.swing.table.*;

public class FavouriteFontsPanel extends javax.swing.JPanel implements ListPanel {
    private final int NOT_FOUND = -1;
    private final int COL_FONTNAME = 0;
    private final int COL_FONTLOC = 1;

    private MainWindow mw;
    private DefaultTableModel tm;
    private int sortCol;
    private boolean sortAsend;
    
    /** Creates new form FavouriteFontsPanel */
    public FavouriteFontsPanel(MainWindow mw) {
        initComponents();
        
        // Init global variables
        this.mw = mw;
        tm = (DefaultTableModel)favouritesTable.getModel();
        sortCol = COL_FONTNAME;
        sortAsend = true;
        favouritesTable.setAutoCreateColumnsFromModel(false);
    }
    
    public boolean addToFav(String name, String loc) {
        boolean found = true;
        
        // If font not already in favs, add to favs
        if (getItemNumber(name, loc) == NOT_FOUND) {
            found = false;
            tm.addRow(new Object[]{name, loc});
        }

        return (!found);
    }
    
    public boolean removeFromFav(String name, String loc) {
        boolean removed = false;
        int p = getItemNumber(name, loc);
        
        // Remove item
        if (p != NOT_FOUND) {
            tm.removeRow(p);
            removed = true;
        }
        
        // Select next/prev item
        if (p >= tm.getRowCount()) {
            favouritesTable.changeSelection(tm.getRowCount()-1, 0, false, false);
            setCurrentItem(tm.getRowCount()-1);
        } else {
            favouritesTable.changeSelection(p, 0, false, false);
            setCurrentItem(p);
        }
        
        // Update display
        mw.updateDisplay();
        
        return removed;
    }
    
    public int getItemNumber(String name, String loc) {
        int itemNum = -1;
        Object[] data = tm.getDataVector().toArray();
        String font = "[" + name + ", " + loc + "]";

        // Check selected font is already in Favourites
        for (int i=0; i<data.length; i++) {
            if (data[i].toString().equals(font)) {
                itemNum = i;
                i += data.length;
            }
        }
        
        return itemNum;
    }
    
    public String[] getItem(int itemNumber) {
        String[] s = new String[3];
        
        // Assign current item to s[]
        if ((itemNumber >= 0)&&(itemNumber < tm.getRowCount())) {
            s[0] = tm.getValueAt(itemNumber, 0).toString();
            s[1] = tm.getValueAt(itemNumber, 1).toString();
            s[2] = itemNumber+"";
        }
        
        return s;
    }
    
    public int getNumItems() {
        return tm.getRowCount();
    }

    public String[] getCurrentItem() {
        // Get current item
        int p = favouritesTable.getSelectedRow();
        String[] s = getItem(p);
        
        // Sort table
        sortAllRowsBy(sortCol, sortAsend);
        
        // Select selected item
        favouritesTable.changeSelection(getItemNumber(s[0], s[1]), 0, false, false);
        
        return s;
    }

    public int getCurrentItemNum() {
        return favouritesTable.getSelectedRow();
    }   

    private void setCurrentItem(int p) {
        String[] s = getCurrentItem();
        try {
            if (p >= 0)
                mw.setCurrentFont(s[0], s[1], Integer.parseInt(s[2]));
        } catch (Exception e) {
        }
    }
    
    public void selectItem(String name, String loc) {
        setCurrentItem(getItemNumber(name, loc));
    }
    
    public void selectNext() {
        int i = favouritesTable.getSelectedRow();
        if (i >= 0) {
            if ((i + 1) < favouritesTable.getRowCount()) {
                i += 1;
            }
            favouritesTable.changeSelection(i, 0, false, false);
            setCurrentItem(i);
        } else {
            favouritesTable.changeSelection(0, 0, false, false);
            setCurrentItem(0);
        }
    }
    
    public void selectPrev() {
        int i = favouritesTable.getSelectedRow();
        if (i >= 0) {
            if ((i - 1) >= 0) {
                i -= 1;
            }
            favouritesTable.changeSelection(i, 0, false, false);
            setCurrentItem(i);
        } else {
            favouritesTable.changeSelection(0, 0, false, false);
            setCurrentItem(0);
        }
    }
    
    // sortAllRowsBy taken from:
    //      http://javaalmanac.com/egs/javax.swing.table/Sorter.html
    public void sortAllRowsBy(int colIndex, boolean ascending) {
        Vector data = tm.getDataVector();
        Collections.sort(data, new ColumnSorter(colIndex, ascending));
        tm.fireTableStructureChanged();
    }
    
    // ColumnSorter taken from:
    //      http://javaalmanac.com/egs/javax.swing.table/Sorter.html
    public class ColumnSorter implements Comparator {
        int colIndex;
        boolean ascending;
        ColumnSorter(int colIndex, boolean ascending) {
            this.colIndex = colIndex;
            this.ascending = ascending;
        }
        public int compare(Object a, Object b) {
            Vector v1 = (Vector)a;
            Vector v2 = (Vector)b;
            Object o1 = v1.get(colIndex);
            Object o2 = v2.get(colIndex);
            
            // Treat empty strains like nulls
            if (o1 instanceof String && ((String)o1).length() == 0) {
                o1 = null;
            }
            if (o2 instanceof String && ((String)o2).length() == 0) {
                o2 = null;
            }
            
            // Sort nulls so they appear last, regardless
            // of sort order
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) {
                return 1;
            } else if (o2 == null) {
                return -1;
            } else if ((o1 instanceof String)&&(o2 instanceof String)) {
                if (ascending) {
                    return ((String)o1).compareToIgnoreCase(o2.toString());
                } else {
                    return ((String)o2).compareToIgnoreCase(o1.toString());
                }
            } else if (o1 instanceof Comparable) {
                if (ascending) {
                    return ((Comparable)o1).compareTo(o2);
                } else {
                    return ((Comparable)o2).compareTo(o1);
                }
            } else {
                if (ascending) {
                    return o1.toString().compareTo(o2.toString());
                } else {
                    return o2.toString().compareToIgnoreCase(o1.toString());
                }
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        favouritesScrollPane = new javax.swing.JScrollPane();
        favouritesTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        favouritesScrollPane.setBorder(null);
        favouritesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Font Name", "Location"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        favouritesTable.setDoubleBuffered(true);
        favouritesTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                favouritesTableKeyReleased(evt);
            }
        });
        favouritesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                favouritesTableMouseClicked(evt);
            }
        });

        favouritesScrollPane.setViewportView(favouritesTable);

        add(favouritesScrollPane, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void favouritesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_favouritesTableMouseClicked
        setCurrentItem(favouritesTable.getSelectedRow());
    }//GEN-LAST:event_favouritesTableMouseClicked

    private void favouritesTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_favouritesTableKeyReleased
        setCurrentItem(favouritesTable.getSelectedRow());
    }//GEN-LAST:event_favouritesTableKeyReleased
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        // Do nothing
    }//GEN-LAST:event_exitForm
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane favouritesScrollPane;
    private javax.swing.JTable favouritesTable;
    // End of variables declaration//GEN-END:variables
}