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
 * listViewPanel.java
 *
 * Created on 21 February 2004, 17:26
 */
package FontViewer.components;
import FontViewer.windows.*;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.lang.ref.*;

public class ListViewPanel extends javax.swing.JPanel {
    private final int NOT_FOUND = -1;    
    private final int COL_FONTNAME = 0;
    private final int COL_FONTLOC = 1;
    
    private int rows;
    private int columns;
    private int position;
    private int fsize;
    
    private String sampleText;
    private ListPanel view;
    private FavouriteFontsPanel ffp;
    private MainWindow mw;
    private AAToggleButton selectedButton;
    private AAToggleButton tmpSelectedButton;
    
    /** Creates new form listViewPanel */
    public ListViewPanel(MainWindow mw,  JPanel ffp, int rows, int columns) {
        this.mw = mw;
        this.ffp = (FavouriteFontsPanel)ffp;
        this.rows = rows;
        this.columns = columns;
        
        position = 0;
        fsize = 20;
        sampleText = java.util.ResourceBundle.getBundle("FontViewer.resources.Opcion").getString("defaultSampleText");
        
        initComponents();
        listScrollPane.getVerticalScrollBar().setUnitIncrement(100);
    }
    
    public void setSampleText(String s) {
        sampleText = s;
        updateDisplay();
    }
    
    public void setFontSize(int s) {
        fsize = s;
        updateDisplay();
    }
    
    public void setView(JPanel p) {
        view = (ListPanel)p;
        int pos = view.getCurrentItemNum();
        if (view.getNumItems() > 0) {
            if  (pos < 0) {
                position = 0;
                updateDisplay();
            } else if (pos >= 0) {
                position = pos;
                updateDisplay();
            }
        } else {
            noDisplay();
        }
    }
    
    public void setPosition(int p) {
        if ((p < position)||((position+rows) <= p)) {
            position = p;
            updateDisplay();
        }
        
        // Highlight selected font
        String[] s = view.getItem(p);
        tmpSelectedButton = (AAToggleButton)(listPanel.getComponent(p - position));
        if (selectedButton != null)
            selectedButton.setBackground(Color.WHITE);
        selectedButton = tmpSelectedButton;
        selectedButton.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
        
        // Scroll to selected font
        int spos = (p - position) * (listScrollPane.getVerticalScrollBar().getMaximum() / rows);
        spos -= (listScrollPane.getSize().height/2);
        listScrollPane.getVerticalScrollBar().setValue(spos);
    }
    
    private void noDisplay() {
        // Clear list
        listPanel.removeAll();
        listPanel.setVisible(false);
        
        // Create message
        JLabel j = new JLabel("There are no fonts to display.", JLabel.CENTER);
        WeakReference wrl = new WeakReference(j);
        
        // Update drawing status
        navInfoLabel.setText("Font 0~0 of 0");
        
        // Show message
        listPanel.add(((JLabel)wrl.get()));
        listPanel.setVisible(true);
    }
    
    public void updateDisplay() {
        Font f = null;
        AAToggleButton tb;
        WeakReference wrf = null;
        WeakReference wrb = null;
        
        // Clear list
        listPanel.removeAll();
        listPanel.setVisible(false);
        
        // Set when to stop drawing
        int last = position+rows;
        int items = view.getNumItems();
        if (last > items) {
            last = items;
        }
        // Draw everything if less items than rows
        if (view.getNumItems() <= rows) {
            position = 0;
        }
        
        // Update drawing status
        navInfoLabel.setText("Font " + (position+1) + "~" + last + " of " + items);
        
        // Draw buttons
        for (int i=position; i<last; i++) {
            // Font item
            String[] font = view.getItem(i);
            
            // Assign font to variable, or create font if working with files
            if (font[COL_FONTLOC].equals("System Font")) {
                f = new Font(font[COL_FONTNAME].toString(), Font.PLAIN, fsize);
                wrf = new WeakReference(f);
            } else {
                try {
                    f = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(font[COL_FONTLOC] + File.separator + font[COL_FONTNAME]));
                    wrf = new WeakReference(f);
                } catch (Exception e) {
                    f = null;
                }
            }
            
            if (f != null) {
                // Set up toggle buttons
                tb = new AAToggleButton(sampleText, font[COL_FONTNAME], font[COL_FONTLOC]);
                wrb = new WeakReference(tb);
                ((AAToggleButton)wrb.get()).setBackground(Color.WHITE);
                ((AAToggleButton)wrb.get()).setFont(f.deriveFont(Font.PLAIN, (float)fsize));
                
                if (!(view instanceof FavouriteFontsPanel)) {
                    /* When in non-fav tab */
                    // Toggle button if this font has been selected before
                    if (ffp.getItemNumber(font[COL_FONTNAME], font[COL_FONTLOC]) != NOT_FOUND) {
                        ((AAToggleButton)wrb.get()).setSelected(true);
                    }
                    
                    // When a button is selected add the selected font to favs
                    // When a button is unselected remove the font from favs
                    ((AAToggleButton)wrb.get()).addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            AAToggleButton source = (AAToggleButton)evt.getSource();
                            view.selectItem(source.getFName(), source.getFLoc());
                            if (source.isSelected()) {
                                ffp.addToFav(source.getFName(), source.getFLoc());
                            } else {
                                ffp.removeFromFav(source.getFName(), source.getFLoc());
                            }
                        }
                    });
                } else {
                    /* When in fav tab */
                    // When a button is selected remove the font favs
                    ((AAToggleButton)wrb.get()).addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            AAToggleButton source = (AAToggleButton)evt.getSource();
                            ffp.removeFromFav(source.getFName(), source.getFLoc());
                        }
                    });
                }
            } else {
                tb = new AAToggleButton("Font could not be loaded.", "Font could not be loaded.", "N/A");
            }
            
            listPanel.add(((AAToggleButton)wrb.get()));
        }
        
        listPanel.setVisible(true);
    }
    
    public void nextPage() {
        // Change position
        if ((position + rows) < view.getNumItems()) {
            position += rows;
            updateDisplay();
        }
    }
    
    public void prevPage() {
        // Change position
        if ((position - rows) >= 0) {
            position -= rows;
            updateDisplay();
        } else if (((position - rows) < 0) && (position != 0)) {
            position = 0;
            updateDisplay();
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        optionsPanel = new javax.swing.JPanel();
        fontsPerPageLabel = new javax.swing.JLabel();
        fontsPerPageTextField = new javax.swing.JTextField();
        listScrollPane = new javax.swing.JScrollPane();
        listPanel = new javax.swing.JPanel();
        navigationPanel = new javax.swing.JPanel();
        prevButton = new javax.swing.JButton();
        navInfoLabel = new javax.swing.JLabel();
        nextButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(2, 2));

        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        optionsPanel.setLayout(new java.awt.GridLayout(1, 0));

        optionsPanel.setPreferredSize(new java.awt.Dimension(10, 20));
        fontsPerPageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fontsPerPageLabel.setText("Fonts/Page");
        fontsPerPageLabel.setToolTipText("Fonts to show per page");
        optionsPanel.add(fontsPerPageLabel);

        fontsPerPageTextField.setText(rows+"");
        fontsPerPageTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fontsPerPageTextFieldKeyReleased(evt);
            }
        });

        optionsPanel.add(fontsPerPageTextField);

        add(optionsPanel, java.awt.BorderLayout.NORTH);

        listScrollPane.setBorder(null);
        listScrollPane.setMinimumSize(new java.awt.Dimension(300, 22));
        listPanel.setLayout(new java.awt.GridLayout(1, 1));

        listPanel.setMinimumSize(new java.awt.Dimension(300, 0));
        listPanel.setLayout(new java.awt.GridLayout(rows, columns, 2, 0));
        listScrollPane.setViewportView(listPanel);

        add(listScrollPane, java.awt.BorderLayout.CENTER);

        navigationPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        navigationPanel.setPreferredSize(new java.awt.Dimension(250, 26));
        prevButton.setText("<");
        prevButton.setToolTipText("Previous page");
        prevButton.setFocusPainted(false);
        prevButton.setPreferredSize(new java.awt.Dimension(41, 20));
        prevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevButtonActionPerformed(evt);
            }
        });

        navigationPanel.add(prevButton);

        navInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        navInfoLabel.setText("Font 0~0 of 0");
        navInfoLabel.setPreferredSize(new java.awt.Dimension(150, 20));
        navigationPanel.add(navInfoLabel);

        nextButton.setText(">");
        nextButton.setToolTipText("Next page");
        nextButton.setFocusPainted(false);
        nextButton.setPreferredSize(new java.awt.Dimension(41, 20));
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        navigationPanel.add(nextButton);

        add(navigationPanel, java.awt.BorderLayout.SOUTH);

    }//GEN-END:initComponents

    private void fontsPerPageTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fontsPerPageTextFieldKeyReleased
        if (evt.getKeyCode() == evt.VK_ENTER) {
            try {
                int i = Integer.parseInt(fontsPerPageTextField.getText());
                rows = i;
                listPanel.setLayout(new java.awt.GridLayout(rows, columns, 2, 0));
                updateDisplay();
            } catch (NumberFormatException nfe) {
                new JOptionPane().showMessageDialog(this, "Not a valid number.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_fontsPerPageTextFieldKeyReleased

    private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevButtonActionPerformed
        prevPage();
    }//GEN-LAST:event_prevButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        nextPage();
    }//GEN-LAST:event_nextButtonActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        // Do nothing
    }//GEN-LAST:event_exitForm
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel fontsPerPageLabel;
    private javax.swing.JTextField fontsPerPageTextField;
    private javax.swing.JPanel listPanel;
    private javax.swing.JScrollPane listScrollPane;
    private javax.swing.JLabel navInfoLabel;
    private javax.swing.JPanel navigationPanel;
    private javax.swing.JButton nextButton;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JButton prevButton;
    // End of variables declaration//GEN-END:variables
}