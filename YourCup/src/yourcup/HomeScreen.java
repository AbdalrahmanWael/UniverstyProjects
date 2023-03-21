package yourcup;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;

public class HomeScreen extends javax.swing.JFrame {

    public static DefaultTableModel playlistModel;

    ImageIcon WCUPICON = new ImageIcon("Data\\WCicon.png");
    ImageIcon APPLOGO = new ImageIcon("Data\\appicon.png");

    int currentVideoIndex = 0;
    
    public HomeScreen() {
        initComponents();
        
        wcIcon.setIcon(WCUPICON);
        wcIcon.setOpaque(false);
        appIcon.setIcon(APPLOGO);
        appIcon.setOpaque(false);
        
        this.setTitle("Your Cup");
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(APPLOGO.getImage());
        
        Controller.draw(Controller.columns, playlistModel, VideoTable, Controller.dvll.ReturnAsStringAL());
        
    }
    public static void draw(){
        Controller.draw(Controller.columns, playlistModel, VideoTable, Controller.dvll.ReturnAsStringAL());
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        VideoTable = new javax.swing.JTable();
        startBtn = new javax.swing.JButton();
        search = new javax.swing.JButton();
        modify = new javax.swing.JButton();
        appIcon = new javax.swing.JLabel();
        wcIcon = new javax.swing.JLabel();
        UpBtn = new javax.swing.JButton();
        DownBtn = new javax.swing.JButton();
        MoveLabel = new javax.swing.JLabel();
        prevBtn = new javax.swing.JButton();
        nextBtn = new javax.swing.JButton();
        SaveBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.SW_RESIZE_CURSOR));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(30, 30, 30));
        jPanel1.setToolTipText("");
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));

        jPanel2.setBackground(new java.awt.Color(102, 0, 0));

        VideoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(VideoTable);

        startBtn.setBackground(new java.awt.Color(255, 255, 255));
        startBtn.setText("Start");
        startBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startBtnActionPerformed(evt);
            }
        });

        search.setBackground(new java.awt.Color(255, 255, 255));
        search.setText("Search");
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        modify.setBackground(new java.awt.Color(255, 255, 255));
        modify.setText("Add/Modify");
        modify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyActionPerformed(evt);
            }
        });

        UpBtn.setBackground(new java.awt.Color(255, 255, 255));
        UpBtn.setText("UP");
        UpBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpBtnActionPerformed(evt);
            }
        });

        DownBtn.setBackground(new java.awt.Color(255, 255, 255));
        DownBtn.setText("Down");
        DownBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DownBtnActionPerformed(evt);
            }
        });

        MoveLabel.setForeground(new java.awt.Color(255, 255, 255));
        MoveLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MoveLabel.setText("Move");

        prevBtn.setBackground(new java.awt.Color(255, 255, 255));
        prevBtn.setText("Prev");
        prevBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevBtnActionPerformed(evt);
            }
        });

        nextBtn.setBackground(new java.awt.Color(255, 255, 255));
        nextBtn.setText("Next");
        nextBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextBtnActionPerformed(evt);
            }
        });

        SaveBtn.setBackground(new java.awt.Color(255, 255, 255));
        SaveBtn.setText("Save");
        SaveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(218, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(202, 202, 202))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(prevBtn)
                        .addGap(18, 18, 18)
                        .addComponent(startBtn)
                        .addGap(18, 18, 18)
                        .addComponent(nextBtn)
                        .addGap(344, 344, 344))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(392, 392, 392)
                .addComponent(appIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(wcIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(220, 220, 220)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(modify, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(MoveLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(UpBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(DownBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(SaveBtn))
                .addGap(63, 63, 63))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(appIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prevBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modify, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(254, 254, 254)
                        .addComponent(MoveLabel)
                        .addGap(18, 18, 18)
                        .addComponent(UpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(DownBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SaveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69)))
                .addComponent(wcIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startBtnActionPerformed
        // TODO add your handling code here:
        currentVideoIndex = 0;
        String url = Controller.dvll.searchByIndex(currentVideoIndex).url;
        // *************PLAY VIDEO USING URL**********
        Controller.EXCUTECMDCOMMAND(url);
    }//GEN-LAST:event_startBtnActionPerformed

    private void modifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyActionPerformed
        // TODO add your handling code here:
        Controller.changeScreen(Controller.Screens.Modify);
    }//GEN-LAST:event_modifyActionPerformed

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        // TODO add your handling code here:
        Controller.changeScreen(Controller.Screens.SEARCH);
    }//GEN-LAST:event_searchActionPerformed

    private void UpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpBtnActionPerformed
        // TODO add your handling code here       
        
        int selectedIndex =  VideoTable.getSelectedRow();
        int prevIndex = selectedIndex - 1;
        if (selectedIndex <= 0)
        {
            JOptionPane.showMessageDialog(this, "You are already at the start");
            return;
        }
        
        String B = Controller.dvll.searchByIndex(selectedIndex).url;       
        String A = Controller.dvll.searchByIndex(prevIndex).url;
        Controller.dvll.swap(A,B);
        
        draw();
    }//GEN-LAST:event_UpBtnActionPerformed

    private void DownBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DownBtnActionPerformed
        // TODO add your handling code here:
        int selectedIndex =  VideoTable.getSelectedRow();
        int nextIndex = selectedIndex + 1;
        if (selectedIndex >= Controller.dvll.getLength() - 1)
        {
            JOptionPane.showMessageDialog(this, "You are already at the End");
            return;
        }
        
        String B = Controller.dvll.searchByIndex(selectedIndex).url;       
        String A = Controller.dvll.searchByIndex(nextIndex).url;
        Controller.dvll.swap(A,B);
        
        draw();
    }//GEN-LAST:event_DownBtnActionPerformed

    private void prevBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevBtnActionPerformed
        // TODO add your handling code here:
        if (currentVideoIndex <= 0)
        {
            JOptionPane.showMessageDialog(this, "You are already at the start");
            return;
        }
        currentVideoIndex = currentVideoIndex - 1;
        String url = Controller.dvll.searchByIndex(currentVideoIndex).url;
        // *************PLAY VIDEO USING URL**********
        Controller.EXCUTECMDCOMMAND(url);
    }//GEN-LAST:event_prevBtnActionPerformed

    private void nextBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextBtnActionPerformed
        // TODO add your handling code here:

        if (currentVideoIndex >= Controller.dvll.getLength() - 1)
        {
            JOptionPane.showMessageDialog(this, "You are already at the End");
            return;
        }
        currentVideoIndex = currentVideoIndex + 1;
        String url = Controller.dvll.searchByIndex(currentVideoIndex).url;
        
         //*************PLAY VIDEO USING URL**********
        Controller.EXCUTECMDCOMMAND(url);
    }//GEN-LAST:event_nextBtnActionPerformed

    private void SaveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveBtnActionPerformed
        // TODO add your handling code here:
        Serialization.PutData(Controller.dvll.ReturnAsStringAL());
    }//GEN-LAST:event_SaveBtnActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new HomeScreen().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DownBtn;
    private javax.swing.JLabel MoveLabel;
    private javax.swing.JButton SaveBtn;
    private javax.swing.JButton UpBtn;
    private static javax.swing.JTable VideoTable;
    private javax.swing.JLabel appIcon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton modify;
    private javax.swing.JButton nextBtn;
    private javax.swing.JButton prevBtn;
    private javax.swing.JButton search;
    private javax.swing.JButton startBtn;
    private javax.swing.JLabel wcIcon;
    // End of variables declaration//GEN-END:variables
}