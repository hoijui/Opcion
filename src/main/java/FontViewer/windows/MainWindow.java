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
 * MainWindow.java
 *
 * Created on 28 January 2004, 12:03
 */
package FontViewer.windows;
import FontViewer.components.*;
import FontViewer.resources.*;
import FontViewer.windows.dialogs.*;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.table.*;
import java.lang.ref.*;

import com.jgoodies.looks.*;
import com.jgoodies.looks.plastic.*;
import com.jgoodies.looks.plastic.theme.*;

public class MainWindow extends javax.swing.JFrame {
    // Debug constants
    private final boolean DEBUG_SIZE = false;
    
    // Constants
    private final int SYSTEM_FONTS = 0;
    private final int OTHER_FONTS = 1;
    private final int FAVOURITE_FONTS = 2;
    private final int[] FONT_SIZES = {6, 8, 9, 10, 11, 12, 14, 16, 18, 20, 24, 28, 32, 36, 42, 48, 56, 72, 84};
    private final String ADD = "Add to Favourites";
    private final String REM = "Remove from Favourites";
    
    // Variables
    private ListPanel currentPanel;
    private String fname;
    private String floc;
    private boolean typingLoc;
    
    // List view properties
    private int rows = 10;
    private int columns = 1;
    
    /** Creates new form mainWindow */
    public MainWindow() {
        try {
            // Set Look and Feel
            PlasticXPLookAndFeel laf = new PlasticXPLookAndFeel();
            laf.setMyCurrentTheme(new SkyBlue());
            UIManager.setLookAndFeel(laf);
            initComponents();
            
            // Add speical JGoodies properties
            tabbedPane.putClientProperty(Options.NO_CONTENT_BORDER_KEY, Boolean.TRUE);
            menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
            menuBar.putClientProperty(PlasticLookAndFeel.BORDER_STYLE_KEY, BorderStyle.SEPARATOR);
            menuBar.requestFocus();
        } catch (Exception e) {
            new JOptionPane().showMessageDialog(this, "Cannot load JGoodies look and feel.", "Error!", JOptionPane.ERROR_MESSAGE);
            initComponents();
        }
        
        // Flag for whether user is typing in location field
        typingLoc = false;
        
        // Disable hidden menu (used to catch keystrokes)
        hiddenMenu.setVisible(false);
        hiddenMenu.setEnabled(false);
        
        // Set current panel
        currentPanel = (ListPanel)systemFontsPanel;
        
        // Draw ListView
        ((ListViewPanel)listViewPanel).setView(systemFontsPanel);
        
        // Center window
        this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width)/2,
        (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height)/2);
    }
    
    public void addToFav() {
        addToFav(fname, floc);
    }
    
    public void removeFromFav() {
        removeFromFav(fname, floc);
    }
    
    public void addToFav(String name, String loc) {
        if (!((FavouriteFontsPanel)favouriteFontsPanel).addToFav(name, loc)) {
            new JOptionPane().showMessageDialog(this, "Font already in favourites.", "Error!", JOptionPane.ERROR_MESSAGE);
        } else {
            updateDisplay();
        }
    }
    
    public void removeFromFav(String name, String loc) {
        if (!((FavouriteFontsPanel)favouriteFontsPanel).removeFromFav(name, loc)) {
            new JOptionPane().showMessageDialog(this, "Font not found in favourites.", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setCurrentFont(String name, String loc, int position) {
        fname = name;
        floc = loc;
        ((ListViewPanel)listViewPanel).setPosition(position);
        ((SampleTextPanel)sampleTextPanel).setCurrentFont(name, loc);
    }
    
    public void setFontSize(int s) {
        if (listViewPanel != null) {
            ((ListViewPanel)listViewPanel).setFontSize(s);
        }
    }
    
    public void updateDisplay() {
        ((ListViewPanel)listViewPanel).updateDisplay();
    }
    
    public void setTyping(boolean t) {
        typingLoc = t;
        if (t == false) {
            menuBar.requestFocus();
        }
    }
    
    private void saveFavToFile(File f) {
        try {
            // Init stuff
            FavouriteFontsPanel fav = (FavouriteFontsPanel)favouriteFontsPanel;
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            String[] s = new String[3];
            
            // Sort favourites
            fav.sortAllRowsBy(0, true);

            // Write favourties
            for (int i=0; i<fav.getNumItems(); i++) {
                s = fav.getItem(i);
                bw.write(s[1] + File.separator + s[0]);
                bw.newLine();
            }
            bw.close();
        } catch (IOException ioe) {
            new JOptionPane().showMessageDialog(this, "Cannot write to file.", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        mainSplitPane = new javax.swing.JSplitPane();
        quickViewSplitPane = new javax.swing.JSplitPane();
        tabbedPane = new javax.swing.JTabbedPane();
        systemFontsPanel = new SystemFontsPanel(this);
        otherFontsPanel = new OtherFontsPanel(this);
        favouriteFontsPanel = new FavouriteFontsPanel(this);
        sampleTextPanel = new SampleTextPanel(this, FONT_SIZES);
        listViewPanel = new ListViewPanel(this, favouriteFontsPanel, rows, columns);
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        savFavsMenuItem = new javax.swing.JMenuItem();
        setSampleTextMenuItem = new javax.swing.JMenuItem();
        fileSep0 = new javax.swing.JSeparator();
        quitMenuItem = new javax.swing.JMenuItem();
        viewsMenu = new javax.swing.JMenu();
        listViewCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        helpMenu = new javax.swing.JMenu();
        addToFavHelpMenuItem = new javax.swing.JMenuItem();
        installFontsMenuItem = new javax.swing.JMenuItem();
        shortcutsMenuItem = new javax.swing.JMenuItem();
        helpSep0 = new javax.swing.JSeparator();
        aboutMenuItem = new javax.swing.JMenuItem();
        hiddenMenu = new javax.swing.JMenu();
        prevPageMenuItem = new javax.swing.JMenuItem();
        nextPageMenuItem = new javax.swing.JMenuItem();
        upMenuItem = new javax.swing.JMenuItem();
        downMenuItem = new javax.swing.JMenuItem();
        addOrRemMenuItem = new javax.swing.JMenuItem();

        getContentPane().setLayout(new java.awt.BorderLayout(0, 5));

        setTitle("Opcion Font Viewer");
        setFont(new java.awt.Font("Dialog", 0, 10));
        setIconImage(new MyImageIcon("IconSmall.png").getImage());
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        mainSplitPane.setBorder(null);
        mainSplitPane.setDividerSize(5);
        mainSplitPane.setResizeWeight(0.5);
        quickViewSplitPane.setBorder(null);
        quickViewSplitPane.setDividerSize(5);
        quickViewSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        quickViewSplitPane.setResizeWeight(0.5);
        tabbedPane.setPreferredSize(new java.awt.Dimension(320, 240));
        tabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabbedPaneMouseClicked(evt);
            }
        });

        tabbedPane.addTab("System Fonts", systemFontsPanel);

        tabbedPane.addTab("Other Fonts", otherFontsPanel);

        tabbedPane.addTab("Favourite Fonts", favouriteFontsPanel);

        quickViewSplitPane.setLeftComponent(tabbedPane);

        sampleTextPanel.setPreferredSize(new java.awt.Dimension(320, 240));
        quickViewSplitPane.setRightComponent(sampleTextPanel);

        mainSplitPane.setLeftComponent(quickViewSplitPane);

        listViewPanel.setPreferredSize(new java.awt.Dimension(320, 480));
        mainSplitPane.setRightComponent(listViewPanel);

        getContentPane().add(mainSplitPane, java.awt.BorderLayout.CENTER);

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");
        savFavsMenuItem.setMnemonic('s');
        savFavsMenuItem.setText("Save Favourites");
        savFavsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savFavsMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(savFavsMenuItem);

        setSampleTextMenuItem.setMnemonic('t');
        setSampleTextMenuItem.setText("Set Sample Text");
        setSampleTextMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSampleTextMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(setSampleTextMenuItem);

        fileMenu.add(fileSep0);

        quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        quitMenuItem.setMnemonic('q');
        quitMenuItem.setText("Quit");
        quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(quitMenuItem);

        menuBar.add(fileMenu);

        viewsMenu.setMnemonic('v');
        viewsMenu.setText("Views");
        listViewCheckBoxMenuItem.setMnemonic('l');
        listViewCheckBoxMenuItem.setSelected(true);
        listViewCheckBoxMenuItem.setText("List View");
        listViewCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listViewCheckBoxMenuItemActionPerformed(evt);
            }
        });

        viewsMenu.add(listViewCheckBoxMenuItem);

        menuBar.add(viewsMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");
        addToFavHelpMenuItem.setMnemonic('f');
        addToFavHelpMenuItem.setText("Add Font to Favourites");
        addToFavHelpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToFavHelpMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(addToFavHelpMenuItem);

        installFontsMenuItem.setMnemonic('i');
        installFontsMenuItem.setText("Installing Fonts");
        installFontsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installFontsMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(installFontsMenuItem);

        shortcutsMenuItem.setMnemonic('s');
        shortcutsMenuItem.setText("Shortcut Keys");
        shortcutsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shortcutsMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(shortcutsMenuItem);

        helpMenu.add(helpSep0);

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        hiddenMenu.setText("hidden");
        prevPageMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_COMMA, 0));
        prevPageMenuItem.setText("prevPage");
        prevPageMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevPageMenuItemActionPerformed(evt);
            }
        });

        hiddenMenu.add(prevPageMenuItem);

        nextPageMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PERIOD, 0));
        nextPageMenuItem.setText("nextPage");
        nextPageMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextPageMenuItemActionPerformed(evt);
            }
        });

        hiddenMenu.add(nextPageMenuItem);

        upMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, 0));
        upMenuItem.setText("up");
        upMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upMenuItemActionPerformed(evt);
            }
        });

        hiddenMenu.add(upMenuItem);

        downMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, 0));
        downMenuItem.setText("down");
        downMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downMenuItemActionPerformed(evt);
            }
        });

        hiddenMenu.add(downMenuItem);

        addOrRemMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, 0));
        addOrRemMenuItem.setText("addOrRem");
        addOrRemMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addOrRemMenuItemActionPerformed(evt);
            }
        });

        hiddenMenu.add(addOrRemMenuItem);

        menuBar.add(hiddenMenu);

        setJMenuBar(menuBar);

        pack();
    }//GEN-END:initComponents

    private void addToFavHelpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToFavHelpMenuItemActionPerformed
        TextAreaFromFileDialog taffd = new TextAreaFromFileDialog(this, "Help - Add Font to Favourites", "addfavHelp.txt");
        taffd.setWrap(true);
        
        // Set window size
        int x = 350; int y = 180;
        taffd.setBounds((java.awt.Toolkit.getDefaultToolkit().getScreenSize().width-x)/2, (java.awt.Toolkit.getDefaultToolkit().getScreenSize().height-y)/2, x, y);
        taffd.setVisible(true);
    }//GEN-LAST:event_addToFavHelpMenuItemActionPerformed

    private void savFavsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savFavsMenuItemActionPerformed
        if (((ListPanel)favouriteFontsPanel).getNumItems() <= 0) {
            new JOptionPane().showMessageDialog(this, "There are no favourite fonts to save.", "Error!", JOptionPane.ERROR_MESSAGE);
        } else {
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
        }
    }//GEN-LAST:event_savFavsMenuItemActionPerformed

    private void shortcutsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shortcutsMenuItemActionPerformed
        TextAreaFromFileDialog taffd = new TextAreaFromFileDialog(this, "Help - Shortcut Keys", "shortcutsHelp.txt");
        taffd.setWrap(false);
        
        // Set window size
        int x = 350; int y = 300;
        taffd.setBounds((java.awt.Toolkit.getDefaultToolkit().getScreenSize().width-x)/2, (java.awt.Toolkit.getDefaultToolkit().getScreenSize().height-y)/2, x, y);
        taffd.setVisible(true);
    }//GEN-LAST:event_shortcutsMenuItemActionPerformed

    private void addOrRemMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addOrRemMenuItemActionPerformed
        if (!typingLoc) {
            menuBar.requestFocus();
            if (currentPanel instanceof FavouriteFontsPanel) {
                removeFromFav();
            } else {
                if (!((FavouriteFontsPanel)favouriteFontsPanel).addToFav(fname, floc)) {
                    removeFromFav();
                } else {
                    updateDisplay();
                }
            }
        }
    }//GEN-LAST:event_addOrRemMenuItemActionPerformed
    
    private void downMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downMenuItemActionPerformed
        if (!typingLoc) {
            menuBar.requestFocus();
            currentPanel.selectNext();
        }
    }//GEN-LAST:event_downMenuItemActionPerformed
    
    private void nextPageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextPageMenuItemActionPerformed
        if (!typingLoc) {
            menuBar.requestFocus();
            ((ListViewPanel)listViewPanel).nextPage();
        }
    }//GEN-LAST:event_nextPageMenuItemActionPerformed

    private void prevPageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevPageMenuItemActionPerformed
        if (!typingLoc) {
            menuBar.requestFocus();
            ((ListViewPanel)listViewPanel).prevPage();
        }
    }//GEN-LAST:event_prevPageMenuItemActionPerformed

    private void upMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upMenuItemActionPerformed
        if (!typingLoc) {
            menuBar.requestFocus();
            currentPanel.selectPrev();
        }
    }//GEN-LAST:event_upMenuItemActionPerformed

    private void setSampleTextMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setSampleTextMenuItemActionPerformed
        String t = new JOptionPane().showInputDialog(this, "Set sample text as:", "Change Sample Text", JOptionPane.QUESTION_MESSAGE);
        if (t != null) {
            ((SampleTextPanel)sampleTextPanel).setSampleText(t);
            ((ListViewPanel)listViewPanel).setSampleText(t);
        }
    }//GEN-LAST:event_setSampleTextMenuItemActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if (DEBUG_SIZE) {
            System.out.println("W: " + this.getSize().width + ", " + this.getSize().height);
            System.out.println("Left top: " + mainSplitPane.getTopComponent().getSize().width + ", " + mainSplitPane.getTopComponent().getSize().height);
            System.out.println("Left btm: " + mainSplitPane.getBottomComponent().getSize().width + ", " + mainSplitPane.getBottomComponent().getSize().height);
            System.out.println("Right: " + quickViewSplitPane.getRightComponent().getSize().width + ", " + mainSplitPane.getRightComponent().getSize().height);
        }
    }//GEN-LAST:event_formComponentResized

    private void listViewCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listViewCheckBoxMenuItemActionPerformed
        if (listViewCheckBoxMenuItem.isSelected()) {
            listViewPanel.setVisible(true);
            mainSplitPane.setDividerLocation(mainSplitPane.getLastDividerLocation());
            mainSplitPane.setEnabled(true);
        } else {
            mainSplitPane.setDividerLocation(1.0);
            mainSplitPane.setEnabled(false);
            listViewPanel.setVisible(false);
        }
    }//GEN-LAST:event_listViewCheckBoxMenuItemActionPerformed

    private void tabbedPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabbedPaneMouseClicked
        // Get selected tab
        currentPanel = (ListPanel)tabbedPane.getSelectedComponent();

        // Update list
        ((ListViewPanel)listViewPanel).setView((JPanel)currentPanel);
        
        // Update sampleTextPanel
        String[] s = currentPanel.getCurrentItem();
        if (s[0] != null) {
            setCurrentFont(s[0], s[1], Integer.parseInt(s[2]));
        }
        
        // Set fav button action
        if (currentPanel instanceof FavouriteFontsPanel) {
            ((SampleTextPanel)sampleTextPanel).setFavButtonAction(REM);
        } else {
            ((SampleTextPanel)sampleTextPanel).setFavButtonAction(ADD);
        }
    }//GEN-LAST:event_tabbedPaneMouseClicked
                
    private void installFontsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installFontsMenuItemActionPerformed
        TextAreaFromFileDialog taffd = new TextAreaFromFileDialog(this, "Help - Installing Fonts", "installHelp.txt");
        taffd.setWrap(false);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        taffd.setBounds((screenSize.width-480)/2, (screenSize.height-295)/2, 480, 295);
        taffd.setVisible(true);
    }//GEN-LAST:event_installFontsMenuItemActionPerformed
    
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        new AboutDialog(this).setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed
    
    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_quitMenuItemActionPerformed
    
    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
    }//GEN-LAST:event_formWindowActivated
                                    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem addOrRemMenuItem;
    private javax.swing.JMenuItem addToFavHelpMenuItem;
    private javax.swing.JMenuItem downMenuItem;
    private javax.swing.JPanel favouriteFontsPanel;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JSeparator fileSep0;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JSeparator helpSep0;
    private javax.swing.JMenu hiddenMenu;
    private javax.swing.JMenuItem installFontsMenuItem;
    private javax.swing.JCheckBoxMenuItem listViewCheckBoxMenuItem;
    private javax.swing.JPanel listViewPanel;
    private javax.swing.JSplitPane mainSplitPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem nextPageMenuItem;
    private javax.swing.JPanel otherFontsPanel;
    private javax.swing.JMenuItem prevPageMenuItem;
    private javax.swing.JSplitPane quickViewSplitPane;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JPanel sampleTextPanel;
    private javax.swing.JMenuItem savFavsMenuItem;
    private javax.swing.JMenuItem setSampleTextMenuItem;
    private javax.swing.JMenuItem shortcutsMenuItem;
    private javax.swing.JPanel systemFontsPanel;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JMenuItem upMenuItem;
    private javax.swing.JMenu viewsMenu;
    // End of variables declaration//GEN-END:variables
}