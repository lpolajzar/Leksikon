
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class LeksikonGUI extends javax.swing.JFrame {
    
    final JFileChooser fc = new JFileChooser();
    private Baza baza;
    private ArrayList<Character> znaki = new ArrayList<Character>(Arrays.asList('A', 'a', 'B', 'b', 'C', 'c', 'Č', 'č', 'Ć', 'ć', 'D', 'd','Đ', 'đ', 'E', 'e', 'F', 'f', 'G', 'g', 'H', 'h', 'I', 'i', 'J', 'j', 'K', 'k', 'L', 'l', 'M', 'm', 'N', 'n','O', 'o', 'P', 'p', 'Q', 'q', 'R', 'r', 'S', 's', 'Š', 'š','T', 't', 'U', 'u', 'V', 'v', 'W', 'w', 'X', 'x','Y', 'y', 'Z', 'z', 'Ž', 'ž'));

    public LeksikonGUI() {
        initComponents();
        
        try{
            
            FileInputStream fin = new FileInputStream("baza.baz");
            ObjectInputStream ois = new ObjectInputStream(fin);
            baza = (Baza) ois.readObject();
            ois.close();

        }catch(Exception ex){
            baza = new Baza();
        }
        
        izrisiVnose();
        
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList1.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if(jList1.getSelectedValue() == null) {
                    jTextArea1.setText(null);
                    jButton2.setEnabled(false);
                    jButton3.setEnabled(false);
                } else {
                    jTextArea1.setText(baza.getVnosi().get(jList1.getSelectedIndex()).getOpis());
                    jButton2.setEnabled(true);
                    jButton3.setEnabled(true);
                }
            }
        });
        
        final LeksikonGUI thisGUI = this; 
        
        jButton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        new VnosGUI(thisGUI, null).setVisible(true);
                    }
                });
            }
        });
        
        jButton2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        new VnosGUI(thisGUI, baza.getVnosi().get(jList1.getSelectedIndex())).setVisible(true);
                    }
                });
            }
        });
        
        jButton2.setEnabled(false);
        
        jButton3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        brisi();
                    }
                });
            }
        });
        
        jButton3.setEnabled(false);
        
        jTextField1.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
               izrisiVnose();
            }
        });
        
        jButton4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        int returnVal = fc.showOpenDialog(thisGUI);

                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            beri(file);
                        } else {
                        }
                    }
                });
            }
        });
    }
    
    private void beri(File file) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Napaka pri odpiranju datoteke!");
            return;
        }
        String line;
        try {
            if(!"Ime,Opis".equals(br.readLine())) {
                JOptionPane.showMessageDialog(this, "Datoteka napačnega formata!");
            }
            line = br.readLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Napaka pri branju datoteke!");
            return;
        }
        
        int duplicates = 0;
        
        while(line != null) {
            String[] parts = line.split(",");
            Vnos vnos = new Vnos(parts[0], parts[1]);
            if(!addVnos(vnos)) {
                duplicates++;
            }
            
            try { 
                line = br.readLine();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Napaka pri branju datoteke!");
                return;
            }
        }
        
        if(duplicates > 0) {
            JOptionPane.showMessageDialog(this, "Vnos končan, število podvojenih vnosov: " + duplicates);
        } else {
            JOptionPane.showMessageDialog(this, "Vnos uspešen!");
        }
        
        izrisiVnose();
    }
    
    public void izrisiVnose() {
        jList1.removeAll();
        
        List<String> ujemajoca = filter();
        
        if(baza != null) {
            final String[] opisi = ujemajoca.toArray(new String[ujemajoca.size()]);
            
            jList1.setModel(new javax.swing.AbstractListModel() {
                String[] strings = opisi;
                public int getSize() { return strings.length; }
                public Object getElementAt(int i) { return strings[i]; }
            });
        }
    }
    
    private List<String> filter() {
        List<String> ujemajoca = new ArrayList<String>();
        String beseda = jTextField1.getText();
        
        for(String ime : baza.getImena()) {
            if(ime.length() >=  beseda.length() && ime.substring(0, beseda.length()).equalsIgnoreCase(beseda)) {
                ujemajoca.add(ime);
            }
        }
        
        return ujemajoca;
    }
    
    public void editVnos(Vnos vnos) {
        
        try{
 
            FileOutputStream fout = new FileOutputStream("baza.baz");
            ObjectOutputStream oos = new ObjectOutputStream(fout);   
            oos.writeObject(baza);
            oos.close();
            System.out.println("Done");

        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        izrisiVnose();
    }
    
    public void brisi() {
        baza.getImena().remove(jList1.getSelectedIndex());
        baza.getVnosi().remove(jList1.getSelectedIndex());
        
        try{
 
            FileOutputStream fout = new FileOutputStream("baza.baz");
            ObjectOutputStream oos = new ObjectOutputStream(fout);   
            oos.writeObject(baza);
            oos.close();
            System.out.println("Done");

        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        izrisiVnose();
    }
    
    public boolean addVnos(Vnos vnos) {
        if(baza.getImena().contains(vnos.getIme())) {
            return false;
        }
        boolean added = false;
        for(int i = 0; i < baza.getImena().size(); i++) {
            if(manjsaPoAbecedi(vnos.getIme(), baza.getImena().get(i))) {
                baza.getVnosi().add(i, vnos);
                baza.getImena().add(i, vnos.getIme());
                added = true;
                break;
            }
        }
        if(!added) {
            baza.getVnosi().add(vnos);
            baza.getImena().add(vnos.getIme());
        }
        
        try{
 
            FileOutputStream fout = new FileOutputStream("baza.baz");
            ObjectOutputStream oos = new ObjectOutputStream(fout);   
            oos.writeObject(baza);
            oos.close();
            System.out.println("Done");

        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        izrisiVnose();
        
        return true;
    }
    
    public boolean manjsaPoAbecedi(String prva, String druga) {
        for(int i = 0; i < prva.length(); i++) {
            if(i > druga.length() - 1) {
                return false;
            }
            if(index(prva.charAt(i))> index(druga.charAt(i))) {
                return false;
            } else if(index(prva.charAt(i)) < index(druga.charAt(i))) {
                return true;
            }
        }
        return true;
    }
    
    private int index(char c) {
        return znaki.indexOf(c) / 2;
    }
    
    public boolean niSamoIzCrk(String beseda) {
        for(int i = 0; i < beseda.length(); i++) {
            if(!znaki.contains(beseda.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Leksikon");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });

        jScrollPane3.setViewportView(jList1);

        jButton1.setText("Nov vnos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Uredi vnos");

        jButton3.setText("Izbriši vnos");

        jButton4.setText("Uvozi CSV datoteko");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                            .addComponent(jTextField1))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addGap(0, 23, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)))
        );

        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LeksikonGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LeksikonGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LeksikonGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LeksikonGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LeksikonGUI().setVisible(true);
            }
        });
    }
    
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
}
