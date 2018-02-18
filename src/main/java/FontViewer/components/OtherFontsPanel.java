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
 * OtherFontsPanel.java
 *
 * Created on 21 February 2004, 18:30
 */
package FontViewer.components;
import FontViewer.windows.*;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.lang.ref.*;

public class OtherFontsPanel extends javax.swing.JPanel implements ListPanel {
    private File currentDirectory;
    private String[] fnames;
    private String[] fontnames;
    private MainWindow mw;
    
    /** Creates new form OtherFontsPanel */
    public OtherFontsPanel(MainWindow mw) {
        this.mw = mw;
        
        initComponents();
    }
    
    public String[] getItem(int itemNumber) {
        String[] s = new String[3];
        
        // Assign current item to s[]
        if ((itemNumber >= 0)&&(itemNumber < fnames.length)) {
            s[0] = fnames[itemNumber];
            s[1] = currentDirectory.toString();
            s[2] = itemNumber+"";
        }

        return s;        
    }
    
    public int getNumItems() {
        int items = 0;
        
        if (fnames != null) {
            items = fnames.length;
        }
        
        return items;
    }

    public String[] getCurrentItem() {
        String[] s = new String[3];
        int p = otherFontsList.getSelectedIndex();
        
        if (p >= 0) {
            s[0] = fnames[p];
            s[1] = currentDirectory.toString();
            s[2] = p+"";
        }
        
        return s;
    }

    public int getCurrentItemNum() {
        return otherFontsList.getSelectedIndex();
    }
    
    // This method is too slow, need to read file names manually
    private void updateFontNames() {
        Font f = null;
        WeakReference<Font> wrf = null;
        String fontfile = "";
        fontnames = new String[fnames.length];
        
        for (int i=0; i<fnames.length; i++) {
            try {
                fontfile = currentDirectory.toString() + File.separator + fnames[i];
                System.out.println("FF: " + fontfile);
                f = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(fontfile));
                wrf = new WeakReference<>(f);
                fontnames[i] = ((Font)wrf.get()).getName();
            } catch (IOException ioe) {
                fontnames[i] = "Cannot load " + fnames[i] + " [IOException]";
            } catch (FontFormatException ffe) {
                fontnames[i] = "Cannot load " + fnames[i] + " [FontFormatException]";
            }
        }
    }
    
    private void updateDisplay() {
        fnames = currentDirectory.list(new FontViewer.filters.FontFileFilter());
        FontViewer.util.QuickSort.sort(fnames, true);
        // updateFontNames(); Too slow
        // java.util.Arrays.sort(fnames); Java Arrays sort is case senstive, not that desirable
        
        if (fnames.length == 0) {
            String[] message = {"This folder does not contain any fonts."};
            otherFontsList.setListData(message);
            otherFontsList.setEnabled(false);
        } else {
            otherFontsList.setListData(fnames);
            otherFontsList.setEnabled(true);
        }
        
        mw.updateDisplay();
    }
    
    public void selectItem(String name, String loc) {
        otherFontsList.setSelectedValue(name, true);
        int p = otherFontsList.getSelectedIndex();
        if (p >= 0)
            mw.setCurrentFont(fnames[p].toString(), currentDirectory.toString(), p);
    }
    
    public void selectNext() {
        int i = otherFontsList.getSelectedIndex();
        if (i >= 0) {
            if ((i + 1) < fnames.length) {
                i += 1;
            }
            setCurrentItem(i, true);
        } else {
            setCurrentItem(0, true);
        }
    }
    
    public void selectPrev() {
        int i = otherFontsList.getSelectedIndex();
        if (i >= 0) {
            if ((i - 1) >= 0) {
                i -= 1;
            }
            setCurrentItem(i, true);
        } else {
            setCurrentItem(0, true);
        }
    }
    
    private void setCurrentItem(int p, boolean internal) {
        if (fnames != null) {
            if (internal) {
                otherFontsList.setSelectedIndex(p);
                int spos = p * (otherFontsScrollPane.getVerticalScrollBar().getMaximum() / fnames.length);
                spos -= (otherFontsScrollPane.getSize().height/2);
                otherFontsScrollPane.getVerticalScrollBar().setValue(spos);
            }
            
            if (p >= 0)
                mw.setCurrentFont(fnames[p].toString(), currentDirectory.toString(), p);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        locationPanel = new javax.swing.JPanel();
        locationLabel = new javax.swing.JLabel();
        locationTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        otherFontsScrollPane = new javax.swing.JScrollPane();
        otherFontsList = new javax.swing.JList<>();

        setLayout(new java.awt.BorderLayout(2, 2));

        locationPanel.setLayout(new java.awt.BorderLayout(4, 0));

        locationLabel.setText("Location:");
        locationPanel.add(locationLabel, java.awt.BorderLayout.WEST);

        locationTextField.setToolTipText("Enter the location where fonts you wish to view are stored here");
        locationTextField.setPreferredSize(new java.awt.Dimension(100, 20));
        locationTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                locationTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                locationTextFieldFocusLost(evt);
            }
        });
        locationTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                locationTextFieldKeyReleased(evt);
            }
        });

        locationPanel.add(locationTextField, java.awt.BorderLayout.CENTER);

        browseButton.setText("Browse");
        browseButton.setToolTipText("Browse for font directory");
        browseButton.setPreferredSize(new java.awt.Dimension(81, 20));
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        locationPanel.add(browseButton, java.awt.BorderLayout.EAST);

        add(locationPanel, java.awt.BorderLayout.NORTH);

        otherFontsScrollPane.setBorder(null);
        otherFontsList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                otherFontsListKeyReleased(evt);
            }
        });
        otherFontsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                otherFontsListMouseClicked(evt);
            }
        });

        otherFontsScrollPane.setViewportView(otherFontsList);

        add(otherFontsScrollPane, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void locationTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_locationTextFieldFocusLost
        mw.setTyping(false);
    }//GEN-LAST:event_locationTextFieldFocusLost

    private void locationTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_locationTextFieldFocusGained
        mw.setTyping(true);
    }//GEN-LAST:event_locationTextFieldFocusGained

    private void otherFontsListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_otherFontsListKeyReleased
        setCurrentItem(otherFontsList.getSelectedIndex(), false);
    }//GEN-LAST:event_otherFontsListKeyReleased

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        // Create new file chooser
        JFileChooser fc = new JFileChooser(new File(""));
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // Show open dialog; this method does not return until the dialog is closed
        fc.showOpenDialog(this);
        if (fc.getSelectedFile() != null) {
            currentDirectory = fc.getSelectedFile();
            locationTextField.setText(currentDirectory.toString());
            updateDisplay();
        }
    }//GEN-LAST:event_browseButtonActionPerformed

    private void otherFontsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_otherFontsListMouseClicked
        setCurrentItem(otherFontsList.getSelectedIndex(), false);
    }//GEN-LAST:event_otherFontsListMouseClicked

    private void locationTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_locationTextFieldKeyReleased
        if (evt.getKeyCode() == evt.VK_ENTER) {
            mw.setTyping(false);
            File f = new File(locationTextField.getText());
            if (f.exists()) {
                if (f.isDirectory()) {
                    currentDirectory = f;
                    updateDisplay();
                } else {
                    new JOptionPane().showMessageDialog(this, "The location does not point to a directory.", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                new JOptionPane().showMessageDialog(this, "Directory does not exist.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_locationTextFieldKeyReleased
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        // Do nothing
    }//GEN-LAST:event_exitForm

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JPanel locationPanel;
    private javax.swing.JTextField locationTextField;
    private javax.swing.JList<String> otherFontsList;
    private javax.swing.JScrollPane otherFontsScrollPane;
    // End of variables declaration//GEN-END:variables
}