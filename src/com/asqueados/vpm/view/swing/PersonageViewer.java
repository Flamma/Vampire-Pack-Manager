/*
 * PersonageViewer.java
 *
 * Created on 23 de octubre de 2011, 17:10
 */

package com.asqueados.vpm.view.swing;

import com.asqueados.vpm.app.Application;
import com.asqueados.vpm.entities.Personage;
import com.asqueados.vpm.entities.PersonageFactory;
import com.asqueados.vpm.exceptions.UnableToCreatePersonageException;
import com.asqueados.vpm.xml.PersonageXmlReader;
import com.asqueados.vpm.xml.XmlReaderException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  Pablo J. Urbano Santos <flamma at member.fsf.org>
 */
public class PersonageViewer extends javax.swing.JFrame {
    private Personage character;
    
    /** Creates new form PersonageViewer */
    public PersonageViewer() {
        initComponents();
    }
    
    public PersonageViewer(Personage character) {
        this.character = character;
        initComponents();
        
        loadPersonage(character);
    }
  
    private void loadPersonage(Personage character) {
        getContentPane().remove(personagePanel);        
        personagePanel = new PersonagePanel(character);
        getContentPane().add(personagePanel, java.awt.BorderLayout.CENTER);
        this.pack();
        
    }
    
    private void loadRandomPersonage() {
        try {
            Personage newCharacter = PersonageFactory.createPersonage(null, 3, 10);
            loadPersonage(newCharacter);
        } catch (UnableToCreatePersonageException ex) {
            Logger.getLogger(PersonageViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        personagePanel = new com.asqueados.vpm.view.swing.PersonagePanel(character);
        jPanel1 = new javax.swing.JPanel();
        randomButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(personagePanel, java.awt.BorderLayout.CENTER);

        randomButton.setText(Application.getTranslator().translate("randomCharacter")); // NOI18N
        randomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(randomButton)
                .addContainerGap(281, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(randomButton)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void randomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomButtonActionPerformed
        loadRandomPersonage();
    }//GEN-LAST:event_randomButtonActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Application.init();


        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    PersonageXmlReader reader = new PersonageXmlReader("data/chars/sample.xml");
                    Personage character = reader.readCharacter();
                    new PersonageViewer(character).setVisible(true);
                } catch (XmlReaderException ex) {
                    Logger.getLogger(PersonageViewer.class.getName()).log(Level.SEVERE, null, ex);
                    new PersonageViewer().setVisible(true);
                }
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private com.asqueados.vpm.view.swing.PersonagePanel personagePanel;
    private javax.swing.JButton randomButton;
    // End of variables declaration//GEN-END:variables
    
}
