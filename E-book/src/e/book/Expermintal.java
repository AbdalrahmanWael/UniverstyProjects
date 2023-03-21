package e.book;

import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Expermintal extends JFrame {
//     public void connectToSql(String username, String password) {
//        Authorsmodel = new DefaultTableModel();
//
//        for (String Column : AuthorColumns) {
//            Authorsmodel.addColumn(Column);
//        }
//
//        AuthorsTable.setModel(Authorsmodel);
//
//        try {
//            System.out.println("Connected");
//
//            stmt = SceneManager.con.createStatement();
//            rs = stmt.executeQuery("select * from Author");//Todo: Order by Asc
//
//            while (rs.next()) {
//                Authorsmodel.addRow(new Object[]{
//                    rs.getInt("Author_ID"), rs.getString("First_Name"), rs.getString("Last_Name")// Todo: A way to Generalize?
//                });
//                System.out.println("hmm");
//            }
//
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, e.getMessage());
//            System.out.println("Error CONNECTION2");
//        }
//    }
    Expermintal()
    {
        ImageIcon image = new ImageIcon("H:\\Unicurriculum\\Year2\\Sem1\\DataBase\\Project\\New folder\\E-book\\src\\e\\book\\E-Book.png");
        JLabel label = new JLabel();     
        
        label.setText("Do you even code bruh");
        
        //label.setIcon(image);
//        label.setHorizontalTextPosition(JLabel.CENTER);
//        label.setForeground(Color.red);
//        label.setFont(new Font("MV Boli", Font.PLAIN,20));
//        label.setIconTextGap(10);
//        label.setBackground(Color.red);
//        label.setOpaque(true);
        
        this.setSize(400, 400);
        this.setTitle("E-Books");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        //ImageIcon image = new ImageIcon("logo.png");
        //this.setIconImage(image.getImage());
        
        //this.getContentPane().setBackground(new Color(0,0,0));
    }
//    public static void main(String[] args) {
//        HomePage frame1 = new HomePage();
//    }

}
