/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TeamSelector.java
 *
 * Created on Dec 30, 2009, 5:48:45 PM
 */

package crickinfo;

import java.sql.*;
import javax.swing.*;

/**
 *
 * @author Dev
 */
public class TeamSelector extends javax.swing.JFrame {

    Connection con;
    Statement st;
    ResultSet rs;
    String sql;
    String team1id,team2id;
    DefaultListModel lm1,lm2,lm3,lm4;
    boolean comboflag=false;
    boolean valid=false;

    /** Creates new form TeamSelector */
    public TeamSelector() {
        initComponents();

        lm1 = new DefaultListModel();
        lm2 = new DefaultListModel();
        lm3 = new DefaultListModel();
        lm4 = new DefaultListModel();
        playerList1.setModel(lm1);
        playerList2.setModel(lm2);
        subList1.setModel(lm3);
        subList2.setModel(lm4);
        try
        {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            con = DriverManager.getConnection("jdbc:odbc:blazers");
            st = con.createStatement();
            
            loadTeamList();
            updateTeamID();
            loadPlayerList1();
            loadPlayerList2();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, e.toString());
        }
        comboflag=true;
    }

    public String getTeam1ID()
    {
        return team1id;
    }

    public String getTeam2ID()
    {
        return team2id;
    }

    public String getTeamName(String teamid)throws SQLException
    {
        String temp;
        sql="select id from teams where name='"+teamid+"'";
        rs=st.executeQuery(sql);
        //rs.next();
        temp=rs.getString(1);
        rs.close();
        return temp;
    }

    public void loadTeamList()throws Exception
    {
        String temp;
        sql="select * from teams";
        rs = st.executeQuery(sql);
        while(rs.next())
        {
            temp=rs.getString("name");
            teamCombo1.addItem(temp);
            teamCombo2.addItem(temp);
        }
        rs.close();
    }
    
    public void updateTeamID()throws Exception
    {
        sql="select * from teams where name='"+teamCombo1.getSelectedItem()+"'";
        System.out.println(sql);
        rs = st.executeQuery(sql);
        rs.next();
        team1id=rs.getString(1);
        System.out.println(team1id);
        rs.close();
        sql="select * from teams where name='"+teamCombo2.getSelectedItem()+"'";
        rs = st.executeQuery(sql);
        rs.next();
        team2id=rs.getString(1);
        System.out.println(team2id);
        rs.close();
    }

    public void loadPlayerList1()throws Exception
    {
        lm1.clear();
        lm3.clear();
        sql="select players.name from players,teams where teams.id=players.team and " +
                "teams.name = '"+teamCombo1.getSelectedItem()+"' and " +
                "players.status = 'P'";
        rs = st.executeQuery(sql);
        while(rs.next())
        {
            lm1.addElement(rs.getString(1));
        }
        sql="select players.name from players,teams where teams.id=players.team and " +
                "teams.name = '"+teamCombo1.getSelectedItem()+"' and " +
                "players.status = 'DNP'";
        rs = st.executeQuery(sql);
        while(rs.next())
        {
            lm3.addElement(rs.getString(1));
        }
    }

    public void loadPlayerList2()throws Exception
    {
        lm2.clear();
        lm4.clear();
        sql="select players.name from players,teams where teams.id=players.team and " +
                "teams.name = '"+teamCombo2.getSelectedItem()+"' and " +
                "players.status = 'P'";
        rs = st.executeQuery(sql);
        while(rs.next())
        {
            lm2.addElement(rs.getString(1));
        }
        sql="select players.name from players,teams where teams.id=players.team and " +
                "teams.name = '"+teamCombo2.getSelectedItem()+"' and " +
                "players.status = 'DNP'";
        rs = st.executeQuery(sql);
        while(rs.next())
        {
            lm4.addElement(rs.getString(1));
        }
        rs.close();
    }

    public void swap1()throws Exception
    {
        if(playerList1.getSelectedIndex()<0 || subList1.getSelectedIndex()<0)
        {
            JOptionPane.showMessageDialog(this, "Select player to swap");
            return;
        }
        sql="update players set status='DNP' where name='"+playerList1.getSelectedValue()+"'";
        st.executeUpdate(sql);
        sql="update players set status='P' where name='"+subList1.getSelectedValue()+"'";
        st.executeUpdate(sql);
        loadPlayerList1();
    }

    public void swap2()throws Exception
    {
        if(playerList2.getSelectedIndex()<0 || subList2.getSelectedIndex()<0)
        {
            JOptionPane.showMessageDialog(this, "Select player to swap");
            return;
        }
        sql="update players set status='DNP' where name='"+playerList2.getSelectedValue()+"'";
        st.executeUpdate(sql);
        sql="update players set status='P' where name='"+subList2.getSelectedValue()+"'";
        st.executeUpdate(sql);
        loadPlayerList2();
    }

    public boolean isDataValid()
    {
        return valid;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        teamCombo1 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        playerList1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        subList1 = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        teamCombo2 = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        playerList2 = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        subList2 = new javax.swing.JList();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        jLabel1.setText("Team 1");

        teamCombo1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                teamCombo1MouseClicked(evt);
            }
        });
        teamCombo1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                teamCombo1ItemStateChanged(evt);
            }
        });

        jScrollPane1.setViewportView(playerList1);

        jButton1.setText("Swap");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(subList1);

        jLabel2.setText("VS");

        jLabel3.setText("Team 2");

        teamCombo2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                teamCombo2ItemStateChanged(evt);
            }
        });

        jScrollPane3.setViewportView(playerList2);

        jButton2.setText("Swap");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jScrollPane4.setViewportView(subList2);

        jButton3.setText("OK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Cancel");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(teamCombo1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(30, 30, 30)
                        .addComponent(jLabel2)
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(teamCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE))))
                .addGap(36, 36, 36))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton3, jButton4});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2, jScrollPane1, jScrollPane2, jScrollPane3, jScrollPane4});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(teamCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(teamCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, 0, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(jLabel2)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4)
                    .addComponent(jButton3))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void teamCombo1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_teamCombo1ItemStateChanged
        // TODO add your handling code here:
        try
        {
            if(comboflag)
            {
                loadPlayerList1();
                updateTeamID();
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }//GEN-LAST:event_teamCombo1ItemStateChanged

    private void teamCombo2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_teamCombo2ItemStateChanged
        // TODO add your handling code here:
        try
        {
            if(comboflag)
            {
                loadPlayerList2();
                updateTeamID();
            }
            
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }//GEN-LAST:event_teamCombo2ItemStateChanged

    private void teamCombo1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_teamCombo1MouseClicked
        // TODO add your handling code here:
        try
        {
            loadPlayerList1();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }//GEN-LAST:event_teamCombo1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try{
            swap1();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try
        {
            swap2();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        valid=true;
        this.setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TeamSelector().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList playerList1;
    private javax.swing.JList playerList2;
    private javax.swing.JList subList1;
    private javax.swing.JList subList2;
    private javax.swing.JComboBox teamCombo1;
    private javax.swing.JComboBox teamCombo2;
    // End of variables declaration//GEN-END:variables

}
