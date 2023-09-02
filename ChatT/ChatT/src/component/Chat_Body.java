package component;

import java.awt.Color;
import javax.swing.Icon;
import net.miginfocom.swing.MigLayout;

public class Chat_Body extends javax.swing.JPanel {

    public Chat_Body() {
        initComponents();
        init();
    }

    private void init() {
        body.setLayout(new MigLayout("fillx", "", "5[]5"));
    }

    public void addItemLeft(String text, String user, String filename, String time, Color color, Icon... image) {
        Chat_Message item = new Chat_Message();
        item.setText(text);
        item.setImage(filename, image);
        item.setTime(time);
        item.setUserProfile(user, color);
        item.filename = filename;
        body.add(item, "wrap, w 100::80%");
        body.repaint();
        body.revalidate();
        
    }
    
    public void addItemFile(String text, String user, String filename, int filesize, String time, Color color) {
        Chat_Message item = new Chat_Message();
        item.setText(text);
        item.setFile(filename, filesize);
        item.setTime(time);
        item.setUserProfile(user, color);
        body.add(item, "wrap, w 100::80%");
        body.repaint();
        body.revalidate();
    }
   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sp = new javax.swing.JScrollPane();
        body = new javax.swing.JPanel();

        sp.setBorder(null);
        sp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        body.setBackground(new java.awt.Color(30, 31, 34));

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1060, Short.MAX_VALUE)
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 940, Short.MAX_VALUE)
        );

        sp.setViewportView(body);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    public javax.swing.JScrollPane sp;
    // End of variables declaration//GEN-END:variables
}
