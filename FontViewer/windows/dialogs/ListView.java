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
 * ListView.java
 *
 * Created on 29 January 2004, 17:52
 */
package FontViewer.windows.dialogs;
import FontViewer.components.*;

import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.lang.ref.*;

import com.jgoodies.plaf.*;
import com.jgoodies.plaf.plastic.*;

public class ListView extends javax.swing.JDialog {
    private final int SYSTEM = 0;
    private final int FILE = 1;
    
    private int rows;
    private int columns;
    private int fontSize;
    private int fontNum;
    private int drawToNum;
    private String sampleText = "Sample Text";
    private Vector selectedFonts;
    private Object[] list;
    JFrame parent;
    
    /** Creates new form ListView */
    public ListView(JFrame parent, int rows, int columns, Object[] o) {
        // Initialize global variables
        super(parent);
        this.parent = parent;
        this.setModal(true);
        this.rows = rows;
        this.columns = columns;
        this.list = o;
        fontSize = 20;
        fontNum = 0;
        drawToNum = fontNum + rows;
        selectedFonts = new Vector();
        initComponents();

        // Add speical JGoodies properties
        menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        menuBar.putClientProperty(PlasticLookAndFeel.BORDER_STYLE_KEY, BorderStyle.SEPARATOR);
        
        // Draw fonts
        drawFonts();
        // Update GUI
        fontSizeTextField.setText(""+fontSize);
        fontsPerPageTextField.setText(""+rows);
        navInfoLabel.setText("Font " + (fontNum+1) + "~" + drawToNum + " of " + list.length);
        pack();
        
        // Hide navigation menu (was only put in place for shortcut key purposes)
        hiddenMenu.setVisible(false);
        hiddenMenu.setEnabled(false);
        
        // Center window
        this.setLocation((GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width - this.getSize().width)/2,
                         (GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height - this.getSize().height)/2);
    }

    private void drawFonts() {
        int objectType = 0;
        Font f = null;
        AAToggleButton tb;
        WeakReference wrf = null;
        WeakReference wrb = null;
        
        listPanel.removeAll();
        listPanel.repaint();
        
        for (int i=fontNum; i<drawToNum; i++) {
            // Assign font to variable, or create font if working with files
            if (list[i] instanceof String) {
                f = new Font(list[i].toString(), Font.PLAIN, fontSize);
                wrf = new WeakReference(f);
                objectType = SYSTEM;
            } else if (list[i] instanceof java.io.File) {
                try {
                    f = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream((File)list[i]));
                    wrf = new WeakReference(f);
                    objectType = FILE;
                } catch (Exception e) {
                    f = null;
                }
            }

            if (f != null) {
                // Set up toggle buttons
                tb = new AAToggleButton(sampleText);
                wrb = new WeakReference(tb);
                ((AAToggleButton)wrb.get()).setBackground(Color.WHITE);
                ((AAToggleButton)wrb.get()).setFont(f.deriveFont(Font.PLAIN, (float)fontSize));
                if (objectType == FILE) {
                    ((AAToggleButton)wrb.get()).setToolTipText(((Font)wrf.get()).getName() + " (" + ((File)list[i]).getName() + ")");
                } else {
                    ((AAToggleButton)wrb.get()).setToolTipText(((Font)wrf.get()).getName());
                }
                
                // Toggle button if this font has been selected before
                if (selectedFonts.contains(((AAToggleButton)wrb.get()).getToolTipText())) {
                    ((AAToggleButton)wrb.get()).setSelected(true);
                }
                
                // When a button is selected add the selected font to a vector
                // When a button is unselected remove the font from the vector
                ((AAToggleButton)wrb.get()).addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        JToggleButton source = (JToggleButton)evt.getSource();
                        if (source.isSelected()) {
                            selectedFonts.add(source.getToolTipText());
                        } else {
                            selectedFonts.remove(source.getToolTipText());
                        }
                        // Return focus to menubar so that navigation shortcuts can
                        // be used
                        menuBar.requestFocus();
                    }
                });
            } else {
                tb = new AAToggleButton("Font could not be loaded.");
            }
            
            listPanel.add(((AAToggleButton)wrb.get()));
        }
    }
    
    private void updateFontSize() {
        fontSizeTextField.setText(""+fontSize);
        drawFonts();
    }
    
    private void updateRows() {
        fontsPerPageTextField.setText(""+rows);
        drawFonts();
    }
    
    private void updateNavInfo() {
        if ((fontNum + rows) < list.length) {
            drawToNum = fontNum + rows;
        } else {
            drawToNum = list.length;
        }
        
        navInfoLabel.setText("Font " + (fontNum+1) + "~" + drawToNum + " of " + list.length);
        navInfoLabel.setToolTipText(navInfoLabel.getText());
        drawFonts();
    }
    
    private void showNextPage() {
        if ((fontNum + rows) < list.length) {
            fontNum += rows;
            updateNavInfo();
        }
    }
    
    private void showPrevPage() {
        if (fontNum != 0) {
            if ((fontNum - rows) >= 0) {
                fontNum -= rows;
            } else {
                fontNum = 0;
            }
            updateNavInfo();
        }
    }
    
    public void pack() {
        // Get windows bounds
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();
        
        // Do normal pack
        super.pack();
        
        int w = this.getWidth();
        int h = this.getHeight();
        boolean changed = false;
        
        // Check if need to resize
        if (w > bounds.width) {
            w = bounds.width;
            changed = true;
            this.setLocation(0, (int)this.getLocation().getY());
        }
        if (h > bounds.height) {
            h =  bounds.height;
            changed = true;
            this.setLocation((int)this.getLocation().getX(), 0);
        }
        
        if (changed) {
            // Change size
            this.setSize(w, h);
       }
       
       // Check if need to move so window visible
       Point p = this.getLocation();
       int px = p.x;
       int py = p.y;
       boolean moved = false;

       if ((px + w) > bounds.width) {
           px = (bounds.width - w) / 2;
           moved = true;
       }
       if ((py + h) > bounds.height) {
           py = (bounds.height - h) / 2;
           moved = true;
       }

       if (moved) {
           // Move window
           this.setLocation(px, py);
       }
    }
    
    private void saveFavToFile(File f) {
        if (selectedFonts.size() == 0) {
            new JOptionPane().showMessageDialog(this, "There are no favourite fonts to save.", "Error!", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                Collections.sort(selectedFonts);
                for (int i=0; i<selectedFonts.size(); i++) {
                    bw.write(selectedFonts.get(i).toString());
                    bw.newLine();
                }
                bw.close();
            } catch (IOException ioe) {
                new JOptionPane().showMessageDialog(this, "Cannot write to file.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private StringBuffer getSortedFavs() {
        Collections.sort(selectedFonts);
        StringBuffer favs = new StringBuffer(selectedFonts.size() * 15);
        for (int i=0; i<selectedFonts.size(); i++) {
            favs.append(selectedFonts.get(i) + "\n");
        }
        
        return favs;
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
        fontSizeLabel = new javax.swing.JLabel();
        fontSizeTextField = new javax.swing.JTextField();
        listScrollPane = new javax.swing.JScrollPane();
        listPanel = new javax.swing.JPanel();
        navigationPanel = new javax.swing.JPanel();
        prevButton = new javax.swing.JButton();
        navInfoLabel = new javax.swing.JLabel();
        nextButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        optionsMenu = new javax.swing.JMenu();
        goToFontMenuItem = new javax.swing.JMenuItem();
        changeSampleTextMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        viewFavsMenuItem = new javax.swing.JMenuItem();
        saveFavsMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        closeViewMenuItem = new javax.swing.JMenuItem();
        hiddenMenu = new javax.swing.JMenu();
        prevMenuItem = new javax.swing.JMenuItem();
        nextMenuItem = new javax.swing.JMenuItem();
        quitMenuItem = new javax.swing.JMenuItem();

        getContentPane().setLayout(new java.awt.BorderLayout(2, 2));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("List View");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        optionsPanel.setLayout(new java.awt.GridLayout(1, 0));

        optionsPanel.setPreferredSize(new java.awt.Dimension(10, 20));
        fontsPerPageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fontsPerPageLabel.setText("Fonts/Page");
        fontsPerPageLabel.setToolTipText("Fonts to show per page");
        optionsPanel.add(fontsPerPageLabel);

        fontsPerPageTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fontsPerPageTextFieldKeyPressed(evt);
            }
        });

        optionsPanel.add(fontsPerPageTextField);

        fontSizeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fontSizeLabel.setText("Font Size");
        fontSizeLabel.setToolTipText("Font display size");
        optionsPanel.add(fontSizeLabel);

        fontSizeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fontSizeTextFieldKeyPressed(evt);
            }
        });

        optionsPanel.add(fontSizeTextField);

        getContentPane().add(optionsPanel, java.awt.BorderLayout.NORTH);

        listScrollPane.setMinimumSize(new java.awt.Dimension(300, 22));
        listScrollPane.setPreferredSize(null);
        listPanel.setLayout(new java.awt.GridLayout(1, 1));

        listPanel.setMinimumSize(new java.awt.Dimension(300, 0));
        listPanel.setLayout(new java.awt.GridLayout(rows, columns, 2, 0));
        listScrollPane.setViewportView(listPanel);

        getContentPane().add(listScrollPane, java.awt.BorderLayout.CENTER);

        navigationPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        navigationPanel.setPreferredSize(new java.awt.Dimension(250, 20));
        prevButton.setText("<");
        prevButton.setToolTipText("Previous page");
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
        nextButton.setPreferredSize(new java.awt.Dimension(41, 20));
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        navigationPanel.add(nextButton);

        getContentPane().add(navigationPanel, java.awt.BorderLayout.SOUTH);

        optionsMenu.setMnemonic('o');
        optionsMenu.setText("Options");
        goToFontMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        goToFontMenuItem.setText("Go To Font #");
        goToFontMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToFontMenuItemActionPerformed(evt);
            }
        });

        optionsMenu.add(goToFontMenuItem);

        changeSampleTextMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        changeSampleTextMenuItem.setText("Change Sample Text");
        changeSampleTextMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeSampleTextMenuItemActionPerformed(evt);
            }
        });

        optionsMenu.add(changeSampleTextMenuItem);

        optionsMenu.add(jSeparator1);

        viewFavsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        viewFavsMenuItem.setMnemonic('v');
        viewFavsMenuItem.setText("View Favourites");
        viewFavsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewFavsMenuItemActionPerformed(evt);
            }
        });

        optionsMenu.add(viewFavsMenuItem);

        saveFavsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveFavsMenuItem.setMnemonic('s');
        saveFavsMenuItem.setText("Save Favourites");
        saveFavsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFavsMenuItemActionPerformed(evt);
            }
        });

        optionsMenu.add(saveFavsMenuItem);

        optionsMenu.add(jSeparator2);

        closeViewMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.CTRL_MASK));
        closeViewMenuItem.setMnemonic('c');
        closeViewMenuItem.setText("Close View");
        closeViewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeViewMenuItemActionPerformed(evt);
            }
        });

        optionsMenu.add(closeViewMenuItem);

        menuBar.add(optionsMenu);

        hiddenMenu.setText("hidden menu");
        prevMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PAGE_UP, 0));
        prevMenuItem.setText("Previous");
        prevMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevMenuItemActionPerformed(evt);
            }
        });

        hiddenMenu.add(prevMenuItem);

        nextMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PAGE_DOWN, 0));
        nextMenuItem.setText("Next");
        nextMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextMenuItemActionPerformed(evt);
            }
        });

        hiddenMenu.add(nextMenuItem);

        quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        quitMenuItem.setText("Item");
        quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitMenuItemActionPerformed(evt);
            }
        });

        hiddenMenu.add(quitMenuItem);

        menuBar.add(hiddenMenu);

        setJMenuBar(menuBar);

        pack();
    }//GEN-END:initComponents

    private void goToFontMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToFontMenuItemActionPerformed
        String fontNumString = new JOptionPane().showInputDialog(this, "Enter font number to go to", "Go To Font #", JOptionPane.QUESTION_MESSAGE);
        if (fontNumString != null) {
            try {
                int fn = Integer.parseInt(fontNumString);
                if ((fn > 0)&&(fn <=list.length)) {
                    if (fontNum != (fn - 1)) {
                        fontNum = fn - 1;
                        updateNavInfo();
                    }
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException nfe) {
                new JOptionPane().showMessageDialog(this, ("Invalid font number, please enter a number between 1 and " + list.length), "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_goToFontMenuItemActionPerformed

    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_quitMenuItemActionPerformed

    private void closeViewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeViewMenuItemActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeViewMenuItemActionPerformed

    private void nextMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextMenuItemActionPerformed
        showNextPage();
    }//GEN-LAST:event_nextMenuItemActionPerformed

    private void prevMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevMenuItemActionPerformed
        showPrevPage();
    }//GEN-LAST:event_prevMenuItemActionPerformed
            
    private void viewFavsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewFavsMenuItemActionPerformed
        new TextAreaDialog(parent, "Favourite Fonts", getSortedFavs().toString()).show();
    }//GEN-LAST:event_viewFavsMenuItemActionPerformed
    
    private void saveFavsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFavsMenuItemActionPerformed
        // Create new file chooser
        JFileChooser fc = new JFileChooser(new File(""));
        // Show save dialog; this method does not return until the dialog is closed
        fc.showSaveDialog(this);
        if (fc.getSelectedFile() != null) {
            File f = fc.getSelectedFile();
            if (f.exists()) {
                // Ask user if they want to overwrite
                if ((new JOptionPane().showConfirmDialog(this, ("The file " + f.getName() + " already exists, do you\nwant to overwrite?"), "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) == JOptionPane.YES_OPTION) {
                    saveFavToFile(f);
                }
            } else {
                saveFavToFile(f);
            }
        }
    }//GEN-LAST:event_saveFavsMenuItemActionPerformed
    
    private void changeSampleTextMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeSampleTextMenuItemActionPerformed
        String t = new JOptionPane().showInputDialog(this, "Set sample text as:", "Change Sample Text", JOptionPane.QUESTION_MESSAGE);
        if (t != null) {
            sampleText = t;
            drawFonts();
            pack();
        }
    }//GEN-LAST:event_changeSampleTextMenuItemActionPerformed
    
    private void fontSizeTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fontSizeTextFieldKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            try {
                int tfs = Integer.parseInt(fontSizeTextField.getText());
                if (tfs > 0) {
                    fontSize = tfs;
                    drawFonts();
                    pack();
                } else {
                    fontSizeTextField.setText(""+fontSize);
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException nfe) {
                new JOptionPane().showMessageDialog(this, "Invalid font size.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_fontSizeTextFieldKeyPressed
    
    private void fontsPerPageTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fontsPerPageTextFieldKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            try {
                int tmpRows = Integer.parseInt(fontsPerPageTextField.getText());
                if (tmpRows > 0) {
                    rows = tmpRows;
                    listPanel.setLayout(new java.awt.GridLayout(rows, columns, 2, 0));
                    if (rows > list.length) {
                        fontNum = 0;
                    }
                    updateNavInfo();
                    pack();
                } else {
                    fontsPerPageTextField.setText(""+rows);
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException nfe) {
                new JOptionPane().showMessageDialog(this, "Invalid fonts per page number.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_fontsPerPageTextFieldKeyPressed
    
    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        showNextPage();
    }//GEN-LAST:event_nextButtonActionPerformed
    
    private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevButtonActionPerformed
        showPrevPage();
    }//GEN-LAST:event_prevButtonActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.dispose();
    }//GEN-LAST:event_exitForm
      
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem changeSampleTextMenuItem;
    private javax.swing.JMenuItem closeViewMenuItem;
    private javax.swing.JLabel fontSizeLabel;
    private javax.swing.JTextField fontSizeTextField;
    private javax.swing.JLabel fontsPerPageLabel;
    private javax.swing.JTextField fontsPerPageTextField;
    private javax.swing.JMenuItem goToFontMenuItem;
    private javax.swing.JMenu hiddenMenu;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel listPanel;
    private javax.swing.JScrollPane listScrollPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel navInfoLabel;
    private javax.swing.JPanel navigationPanel;
    private javax.swing.JButton nextButton;
    private javax.swing.JMenuItem nextMenuItem;
    private javax.swing.JMenu optionsMenu;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JButton prevButton;
    private javax.swing.JMenuItem prevMenuItem;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JMenuItem saveFavsMenuItem;
    private javax.swing.JMenuItem viewFavsMenuItem;
    // End of variables declaration//GEN-END:variables
}