package component;

import java.awt.Color;
import javax.swing.Icon;

public class Chat_Message extends javax.swing.JLayeredPane {

    String filename;
    
    public Chat_Message() {
        initComponents();
        txt.setBackground(new Color(43, 45, 49));
    }

    public void setUserProfile(String user, Color color) {
        txt.setUserProfile(user, color);
    }
    
    public void setText(String text) {
        if (text.equals("")) {
            txt.hideText();
        } else {
            txt.setText(text);
        }
    }

    public void setImage(String filename, Icon... image) {
        txt.setImage(filename,false, image);
    }
    
    public void setFile(String filename, int filesize){
        txt.setFile(filename, filesize);
    }

    public void setTime(String time) {
        txt.setTime(time);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txt = new component.Chat_Item();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        add(txt);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private component.Chat_Item txt;
    // End of variables declaration//GEN-END:variables
}
