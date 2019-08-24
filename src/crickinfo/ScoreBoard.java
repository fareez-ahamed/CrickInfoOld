/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ScoreBoard.java
 *
 * Created on Jan 2, 2010, 1:24:20 AM
 */

package crickinfo;

import crickinfo.manage.*;
import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
import java.io.*;
/**
 *
 * @author Shinaz
 */
public class ScoreBoard extends javax.swing.JFrame implements UIController{

    MatchInfo minf;
    TeamSelectorDialog ts;
    Match match;
    Match matchundo;
    Thread t;
    String temparr[],temp;
    String ibuffstr;
    WicketInfo wi;
    Debugger dbg;
    RunoutInfo ri;
    RankingFrame rf;
    int undoid=0;

    boolean wait;
    /** Creates new form ScoreBoard */
    public ScoreBoard() {
        initComponents();
        ts = new TeamSelectorDialog(this,true);
        ts.setVisible(true);
        try{
        match = new Match(this);
        initMatch();
        match.init();
        match.processBall("start");
        dbg=new Debugger(match);
        dbg.setVisible(true);
        updateView();
        dbg.update();
        this.setFocusableWindowState(true);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, e.toString());
            e.printStackTrace();
        }

    }

    //<editor-fold desc="UIConroller Actions">
    public WicketInfo getWicketInfo()
    {
        batsmanCombo3.removeAllItems();
        batsmanCombo3.addItem(match.getStriker().getName());
        batsmanCombo3.addItem(match.getNonStriker().getName());
        fielderCombo.removeAllItems();
        for(int i=0;i<match.getBowlingTeam().getPlayersList().length;i++)
            fielderCombo.addItem(match.getBowlingTeam().getPlayersList()[i]);
        wktDlg.setSize(wktDlg.getPreferredSize());
        wktDlg.setVisible(true);
        return wi;
    }

    public String getNextBatsman()
    {
        batsmanCombo4.removeAllItems();
        for(int i=0;i<match.getBattingTeam().getNonBattedList().length;i++)
        {
            batsmanCombo4.addItem(match.getBattingTeam().getNonBattedList()[i]);
        }
        nextBatDlg.setSize((int)nextBatDlg.getPreferredSize().getWidth()+50,(int)nextBatDlg.getPreferredSize().getHeight()+50);
        nextBatDlg.setVisible(true);
        return temp;
    }

    public String[] getOpeningBatsman()
    {
        temparr = new String[2];
        batsmanCombo1.removeAllItems();
        batsmanCombo2.removeAllItems();
        for(int i=0;i<match.getBattingTeam().getNonBattedList().length;i++)
        {
            batsmanCombo1.addItem(match.getBattingTeam().getNonBattedList()[i]);
            batsmanCombo2.addItem(match.getBattingTeam().getNonBattedList()[i]);
        }
        openingBatDlg.setSize((int)openingBatDlg.getPreferredSize().getWidth()+50,(int)openingBatDlg.getPreferredSize().getHeight()+50);
        openingBatDlg.setVisible(true);
        return temparr;
    }

    public RunoutInfo getRunoutInfo()
    {
        batsmanCombo5.removeAllItems();
        for(int i=0;i<match.getBattingTeam().getNonBattedList().length;i++)
        {
            batsmanCombo5.addItem(match.getBattingTeam().getNonBattedList()[i]);
        }
        runoutBatDlg.setSize(runoutBatDlg.getPreferredSize());
        runoutBatDlg.setVisible(true);
        return ri;
    }

    public String getNextBowler()
    {
        bowlerCombo1.removeAllItems();
        for(int i=0;i<match.getBowlingTeam().getPlayersList().length;i++)
        {
            bowlerCombo1.addItem(match.getBowlingTeam().getPlayersList()[i]);
        }
        nextBowlDlg.setSize((int)nextBowlDlg.getPreferredSize().getWidth()+50,(int)nextBowlDlg.getPreferredSize().getHeight()+50);
        nextBowlDlg.setVisible(true);
        return temp;
    }

    public void initMatch()
    {
        try
        {
            matchInitDlg.setSize(matchInitDlg.getPreferredSize().width+50,
                    matchInitDlg.getPreferredSize().height+50);
            oversTxt.setText(Integer.toString(oversSlider.getValue()));
            teamCombo1.addItem(ts.getTeamName(ts.getTeam1ID()));
            teamCombo1.addItem(ts.getTeamName(ts.getTeam2ID()));
            matchInitDlg.setVisible(true);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,e.toString());
            e.printStackTrace();
        }

    }

    public void finishMatch()
    {
        buffTxt.setEnabled(false);
        JOptionPane.showMessageDialog(this, match.getWinningStatement());
        try{
        match.updateDatabase();
        showDetails();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, e.toString());
            e.printStackTrace();
        }
    }

    public void endOfFirstInnings()
    {
        String s;
        s="<center><h1>"+match.getBattingTeam().getName()+" : "
                +match.getCurrentInnings().getScore()+"/"+match.getCurrentInnings().getWickets()+"</h1>";
        s=s+"<h1>"+match.getBowlingTeam().getName()+" should get "+(match.getCurrentInnings().getScore()+1)
                +" from "+match.getOvers()+"</h1></center>";
        firstInnSummary.setText(s);
        firstInnEndDlg.setSize(firstInnEndDlg.getPreferredSize());
        firstInnEndDlg.setVisible(true);
    }
    //</editor-fold>

    public void updateView()
    {
        scoreView.setText(match.getMainScoreDoc());
        batScoreView.setText(match.getBatDoc());
        bowlScoreView.setText(match.getBowlDoc());
        dbg.update();
    }

    public void showDetails()
    {
        batPane1.setText(match.getBatDoc(match.getFirstInnings()));
        bowlPane1.setText(match.getBowlDoc(match.getTeam2()));
        batPane2.setText(match.getBatDoc(match.getSecondInnings()));
        bowlPane2.setText(match.getBowlDoc(match.getTeam1()));
        matchSummaryDlg.setSize(matchSummaryDlg.getPreferredSize());
        matchSummaryDlg.setVisible(true);
    }

    public void writeToFile(String s)throws IOException
    {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(s));
        out.writeObject(match);
    }

    public void readFromFile(String s)throws IOException,ClassNotFoundException
    {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(s));
        match=(Match)in.readObject();
        updateView();
    }
    
    public void writeToTempFile()throws IOException
    {
        writeToFile("temp.txt");
    }
    
    public void readFromTempFile()throws IOException,ClassNotFoundException
    {
        readFromFile("temp.txt");
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        matchInitDlg = new javax.swing.JDialog();
        teamCombo1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        datntimeTxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        descTxt = new javax.swing.JTextField();
        matchInitOK = new javax.swing.JButton();
        matchInitCancel = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        oversSlider = new javax.swing.JSlider();
        oversTxt = new javax.swing.JTextField();
        openingBatDlg = new javax.swing.JDialog();
        batsmanCombo1 = new javax.swing.JComboBox();
        batsmanCombo2 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        dlgOK2 = new javax.swing.JButton();
        nextBatDlg = new javax.swing.JDialog();
        batsmanCombo4 = new javax.swing.JComboBox();
        dlg5OK = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        nextBowlDlg = new javax.swing.JDialog();
        bowlerCombo1 = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        dlg3OK = new javax.swing.JButton();
        wktDlg = new javax.swing.JDialog();
        jLabel9 = new javax.swing.JLabel();
        wkttypeCombo = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        batsmanCombo3 = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        fielderCombo = new javax.swing.JComboBox();
        dlg4OK = new javax.swing.JButton();
        runoutBatDlg = new javax.swing.JDialog();
        jLabel12 = new javax.swing.JLabel();
        batsmanCombo5 = new javax.swing.JComboBox();
        dlg6OK = new javax.swing.JButton();
        strkChk = new javax.swing.JCheckBox();
        firstInnEndDlg = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        firstInnSummary = new javax.swing.JEditorPane();
        matchSummaryDlg = new javax.swing.JDialog();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        batPane1 = new javax.swing.JEditorPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        bowlPane1 = new javax.swing.JEditorPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        batPane2 = new javax.swing.JEditorPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        bowlPane2 = new javax.swing.JEditorPane();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        scoreView = new javax.swing.JEditorPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        bowlScoreView = new javax.swing.JEditorPane();
        buffTxt = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        batScoreView = new javax.swing.JEditorPane();
        jLabel13 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuFileUpdate = new javax.swing.JMenuItem();
        menuFileReload = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        editSwapStrike = new javax.swing.JMenuItem();
        editUndo = new javax.swing.JMenuItem();
        editChangeRun = new javax.swing.JMenuItem();
        editBabyCutOver = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        matchShowDetails = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        tournRank = new javax.swing.JMenuItem();

        matchInitDlg.setAlwaysOnTop(true);
        matchInitDlg.setModal(true);
        matchInitDlg.setResizable(false);

        jLabel1.setText("Batting Team");

        jLabel2.setText("Date and Time");

        jLabel3.setText("Venue");

        matchInitOK.setText("OK");
        matchInitOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                matchInitOKActionPerformed(evt);
            }
        });

        matchInitCancel.setText("Cancel");
        matchInitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                matchInitCancelActionPerformed(evt);
            }
        });

        jLabel4.setText("Overs");

        oversSlider.setMajorTickSpacing(5);
        oversSlider.setMaximum(20);
        oversSlider.setMinorTickSpacing(1);
        oversSlider.setPaintLabels(true);
        oversSlider.setPaintTicks(true);
        oversSlider.setSnapToTicks(true);
        oversSlider.setValue(10);
        oversSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                oversSliderStateChanged(evt);
            }
        });

        oversTxt.setEnabled(false);

        javax.swing.GroupLayout matchInitDlgLayout = new javax.swing.GroupLayout(matchInitDlg.getContentPane());
        matchInitDlg.getContentPane().setLayout(matchInitDlgLayout);
        matchInitDlgLayout.setHorizontalGroup(
            matchInitDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(matchInitDlgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(matchInitDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, matchInitDlgLayout.createSequentialGroup()
                        .addComponent(matchInitOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(matchInitCancel)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, matchInitDlgLayout.createSequentialGroup()
                        .addGroup(matchInitDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(oversSlider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                            .addGroup(matchInitDlgLayout.createSequentialGroup()
                                .addGroup(matchInitDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addGroup(matchInitDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(oversTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, matchInitDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(descTxt)
                                        .addComponent(datntimeTxt)
                                        .addComponent(teamCombo1, 0, 233, Short.MAX_VALUE)))))
                        .addGap(18, 18, 18))))
        );
        matchInitDlgLayout.setVerticalGroup(
            matchInitDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(matchInitDlgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(matchInitDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teamCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(matchInitDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(datntimeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(matchInitDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(descTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(matchInitDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(oversTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(oversSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(matchInitDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(matchInitCancel)
                    .addComponent(matchInitOK))
                .addContainerGap())
        );

        openingBatDlg.setAlwaysOnTop(true);
        openingBatDlg.setModal(true);

        jLabel6.setText("Striker");

        jLabel7.setText("Non-striker");

        dlgOK2.setText("OK");
        dlgOK2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlgOK2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout openingBatDlgLayout = new javax.swing.GroupLayout(openingBatDlg.getContentPane());
        openingBatDlg.getContentPane().setLayout(openingBatDlgLayout);
        openingBatDlgLayout.setHorizontalGroup(
            openingBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(openingBatDlgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(openingBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(openingBatDlgLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, openingBatDlgLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(40, 40, 40)))
                .addGroup(openingBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(batsmanCombo2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(batsmanCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, openingBatDlgLayout.createSequentialGroup()
                .addContainerGap(265, Short.MAX_VALUE)
                .addComponent(dlgOK2)
                .addContainerGap())
        );
        openingBatDlgLayout.setVerticalGroup(
            openingBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(openingBatDlgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(openingBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(batsmanCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(openingBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(batsmanCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(dlgOK2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        nextBatDlg.setAlwaysOnTop(true);
        nextBatDlg.setModal(true);
        nextBatDlg.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        dlg5OK.setText("OK");
        dlg5OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlg5OKActionPerformed(evt);
            }
        });

        jLabel5.setText("Next Batsman");

        javax.swing.GroupLayout nextBatDlgLayout = new javax.swing.GroupLayout(nextBatDlg.getContentPane());
        nextBatDlg.getContentPane().setLayout(nextBatDlgLayout);
        nextBatDlgLayout.setHorizontalGroup(
            nextBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nextBatDlgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(nextBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nextBatDlgLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(batsmanCombo4, 0, 264, Short.MAX_VALUE))
                    .addComponent(dlg5OK, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        nextBatDlgLayout.setVerticalGroup(
            nextBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nextBatDlgLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(nextBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(batsmanCombo4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(dlg5OK)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        nextBowlDlg.setModal(true);

        jLabel8.setText("Next Bowler");

        dlg3OK.setText("OK");
        dlg3OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlg3OKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout nextBowlDlgLayout = new javax.swing.GroupLayout(nextBowlDlg.getContentPane());
        nextBowlDlg.getContentPane().setLayout(nextBowlDlgLayout);
        nextBowlDlgLayout.setHorizontalGroup(
            nextBowlDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nextBowlDlgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(nextBowlDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dlg3OK)
                    .addGroup(nextBowlDlgLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(bowlerCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        nextBowlDlgLayout.setVerticalGroup(
            nextBowlDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nextBowlDlgLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(nextBowlDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(bowlerCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(dlg3OK)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        wktDlg.setAlwaysOnTop(true);
        wktDlg.setModal(true);

        jLabel9.setText("Wicket Type");

        wkttypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bowled", "Caught", "Runout", "Stumped", "Hit", "Unspecified" }));

        jLabel10.setText("Batsman");

        jLabel11.setText("Fielder");

        dlg4OK.setText("OK");
        dlg4OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlg4OKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout wktDlgLayout = new javax.swing.GroupLayout(wktDlg.getContentPane());
        wktDlg.getContentPane().setLayout(wktDlgLayout);
        wktDlgLayout.setHorizontalGroup(
            wktDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wktDlgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(wktDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dlg4OK)
                    .addGroup(wktDlgLayout.createSequentialGroup()
                        .addGroup(wktDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(wktDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fielderCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(batsmanCombo3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(wkttypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        wktDlgLayout.setVerticalGroup(
            wktDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wktDlgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(wktDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(wkttypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(wktDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(batsmanCombo3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(wktDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(fielderCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(dlg4OK)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        runoutBatDlg.setModal(true);

        jLabel12.setText("Next Batsman");

        dlg6OK.setText("OK");
        dlg6OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlg6OKActionPerformed(evt);
            }
        });

        strkChk.setText("Takes the strike");

        javax.swing.GroupLayout runoutBatDlgLayout = new javax.swing.GroupLayout(runoutBatDlg.getContentPane());
        runoutBatDlg.getContentPane().setLayout(runoutBatDlgLayout);
        runoutBatDlgLayout.setHorizontalGroup(
            runoutBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(runoutBatDlgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(runoutBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(strkChk)
                    .addGroup(runoutBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(dlg6OK)
                        .addGroup(runoutBatDlgLayout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addGap(18, 18, 18)
                            .addComponent(batsmanCombo5, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(30, 30, 30))
        );
        runoutBatDlgLayout.setVerticalGroup(
            runoutBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(runoutBatDlgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(runoutBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(batsmanCombo5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(runoutBatDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(runoutBatDlgLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(strkChk)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, runoutBatDlgLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addComponent(dlg6OK)
                        .addGap(32, 32, 32))))
        );

        firstInnEndDlg.setAlwaysOnTop(true);
        firstInnEndDlg.setModal(true);

        firstInnSummary.setContentType("text/html");
        firstInnSummary.setEditable(false);
        jScrollPane4.setViewportView(firstInnSummary);

        firstInnEndDlg.getContentPane().add(jScrollPane4, java.awt.BorderLayout.CENTER);

        batPane1.setContentType("text/html");
        batPane1.setEditable(false);
        jScrollPane5.setViewportView(batPane1);

        bowlPane1.setContentType("text/html");
        bowlPane1.setEditable(false);
        jScrollPane6.setViewportView(bowlPane1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("First Innings", jPanel1);

        batPane2.setContentType("text/html");
        jScrollPane7.setViewportView(batPane2);

        bowlPane2.setContentType("text/html");
        jScrollPane8.setViewportView(bowlPane2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Second Innings", jPanel2);

        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout matchSummaryDlgLayout = new javax.swing.GroupLayout(matchSummaryDlg.getContentPane());
        matchSummaryDlg.getContentPane().setLayout(matchSummaryDlgLayout);
        matchSummaryDlgLayout.setHorizontalGroup(
            matchSummaryDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(matchSummaryDlgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(matchSummaryDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        matchSummaryDlgLayout.setVerticalGroup(
            matchSummaryDlgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(matchSummaryDlgLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        scoreView.setContentType("text/html");
        scoreView.setEditable(false);
        jScrollPane1.setViewportView(scoreView);

        bowlScoreView.setContentType("text/html");
        bowlScoreView.setEditable(false);
        jScrollPane3.setViewportView(bowlScoreView);

        buffTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                buffTxtKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                buffTxtKeyTyped(evt);
            }
        });

        batScoreView.setContentType("text/html");
        batScoreView.setEditable(false);
        jScrollPane2.setViewportView(batScoreView);

        jLabel13.setText("Ball Detail");

        jMenu1.setText("File");

        menuFileUpdate.setText("Update File");
        menuFileUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileUpdateActionPerformed(evt);
            }
        });
        jMenu1.add(menuFileUpdate);

        menuFileReload.setText("Reload from File");
        menuFileReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileReloadActionPerformed(evt);
            }
        });
        jMenu1.add(menuFileReload);

        jMenuBar1.add(jMenu1);

        editMenu.setText("Edit");
        editMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editMenuActionPerformed(evt);
            }
        });

        editSwapStrike.setText("Swap Strike");
        editSwapStrike.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSwapStrikeActionPerformed(evt);
            }
        });
        editMenu.add(editSwapStrike);

        editUndo.setText("Undo last ball");
        editUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editUndoActionPerformed(evt);
            }
        });
        editMenu.add(editUndo);

        editChangeRun.setText("Change Run");
        editChangeRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editChangeRunActionPerformed(evt);
            }
        });
        editMenu.add(editChangeRun);

        editBabyCutOver.setText("Baby Cut Over");
        editBabyCutOver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBabyCutOverActionPerformed(evt);
            }
        });
        editMenu.add(editBabyCutOver);

        jMenuItem1.setText("Reset Over");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        editMenu.add(jMenuItem1);

        jMenuBar1.add(editMenu);

        jMenu2.setText("Match");

        matchShowDetails.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        matchShowDetails.setText("Show Details");
        matchShowDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                matchShowDetailsActionPerformed(evt);
            }
        });
        jMenu2.add(matchShowDetails);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Tournament");

        tournRank.setText("Rankings");
        tournRank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tournRankActionPerformed(evt);
            }
        });
        jMenu3.add(tournRank);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 593, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addComponent(buffTxt)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buffTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void oversSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_oversSliderStateChanged
        // TODO add your handling code here:
        oversTxt.setText(Integer.toString(oversSlider.getValue()));
    }//GEN-LAST:event_oversSliderStateChanged

    private void matchInitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_matchInitCancelActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_matchInitCancelActionPerformed

    private void matchInitOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_matchInitOKActionPerformed
        // TODO add your handling code here:
        try
        {
            if(ts.getTeamName(ts.getTeam1ID()).equals(teamCombo1.getSelectedItem()))
                match.setTeams(ts.getTeam1ID(), ts.getTeam2ID());
            else
                match.setTeams(ts.getTeam2ID(), ts.getTeam1ID());
            match.setMatchOvers(oversSlider.getValue());
            match.setDesc(descTxt.getText());
            matchInitDlg.setVisible(false);
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(this,e.toString());
            e.printStackTrace();
        }
    }//GEN-LAST:event_matchInitOKActionPerformed

    private void dlgOK2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgOK2ActionPerformed
        // TODO add your handling code here:
        temparr[0]=(String)batsmanCombo1.getSelectedItem();
        temparr[1]=(String)batsmanCombo2.getSelectedItem();
        openingBatDlg.setVisible(false);
    }//GEN-LAST:event_dlgOK2ActionPerformed

    private void dlg3OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlg3OKActionPerformed
        // TODO add your handling code here:
        temp=(String)bowlerCombo1.getSelectedItem();
        nextBowlDlg.setVisible(false);
    }//GEN-LAST:event_dlg3OKActionPerformed

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formKeyTyped

    private void buffTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buffTxtKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_buffTxtKeyTyped

    private void buffTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buffTxtKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
        {
            try{
                writeToFile("undo"+undoid+".txt");
                undoid++;
                match.processBall(buffTxt.getText());
                buffTxt.setText("");
                updateView();
                writeToTempFile();
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(this, e.toString());
                e.printStackTrace();
            }
        }
        buffTxt.setFocusable(true);
    }//GEN-LAST:event_buffTxtKeyPressed

    private void dlg4OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlg4OKActionPerformed
        // TODO add your handling code here:
        wi = new WicketInfo(wkttypeCombo.getSelectedIndex());
        wi.setLostBatsman(match.getBattingTeam().getPlayerByName((String)batsmanCombo3.getSelectedItem()));
        wi.setThirdMan(match.getBowlingTeam().getPlayerByName((String)fielderCombo.getSelectedItem()));
        wktDlg.setVisible(false);
    }//GEN-LAST:event_dlg4OKActionPerformed

    private void dlg5OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlg5OKActionPerformed
        // TODO add your handling code here:
        temp=(String)batsmanCombo4.getSelectedItem();
        nextBatDlg.setVisible(false);
    }//GEN-LAST:event_dlg5OKActionPerformed

    private void dlg6OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlg6OKActionPerformed
        // TODO add your handling code here:
        ri = new RunoutInfo((String)batsmanCombo5.getSelectedItem(),strkChk.isSelected());
        runoutBatDlg.setVisible(false);
    }//GEN-LAST:event_dlg6OKActionPerformed

    private void menuFileReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileReloadActionPerformed
        // TODO add your handling code here:
        try
        {
            readFromTempFile();
            match.setUICon(this);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }//GEN-LAST:event_menuFileReloadActionPerformed

    private void menuFileUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileUpdateActionPerformed
        // TODO add your handling code here:
        try
        {
            writeToFile("update.txt");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }//GEN-LAST:event_menuFileUpdateActionPerformed

    private void editSwapStrikeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSwapStrikeActionPerformed
        // TODO add your handling code here:
        match.swapStrike();
        updateView();
    }//GEN-LAST:event_editSwapStrikeActionPerformed

    private void editUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editUndoActionPerformed
        // TODO add your handling code here:
        try
        {
            undoid--;
            if(undoid>=0)
                readFromFile("undo"+undoid+".txt");
            else
                JOptionPane.showMessageDialog(this, "Can't Undo");
            match.setUICon(this);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }//GEN-LAST:event_editUndoActionPerformed

    private void editChangeRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editChangeRunActionPerformed
        // TODO add your handling code here:
        int newr;
        String s;
        s=JOptionPane.showInputDialog(this, "Enter the new run", "New Run");
        newr=Integer.parseInt(s);
        match.getCurrentInnings().addRuns(newr);
    }//GEN-LAST:event_editChangeRunActionPerformed

    private void editBabyCutOverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBabyCutOverActionPerformed
        // TODO add your handling code here:
        String s;
        getNextBowler();
        s=temp;
        match.setBowler(s);
        updateView();
    }//GEN-LAST:event_editBabyCutOverActionPerformed

    private void matchShowDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_matchShowDetailsActionPerformed
        // TODO add your handling code here:
        showDetails();
    }//GEN-LAST:event_matchShowDetailsActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        matchSummaryDlg.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void editMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editMenuActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        int n;
        n=match.getCurrentInnings().getOver().getBalls()/6;
        match.getCurrentInnings().getOver().setBalls(n*6);
        updateView();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void tournRankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tournRankActionPerformed
        // TODO add your handling code here:
        rf = new RankingFrame();
        rf.setVisible(true);
    }//GEN-LAST:event_tournRankActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScoreBoard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane batPane1;
    private javax.swing.JEditorPane batPane2;
    private javax.swing.JEditorPane batScoreView;
    private javax.swing.JComboBox batsmanCombo1;
    private javax.swing.JComboBox batsmanCombo2;
    private javax.swing.JComboBox batsmanCombo3;
    private javax.swing.JComboBox batsmanCombo4;
    private javax.swing.JComboBox batsmanCombo5;
    private javax.swing.JEditorPane bowlPane1;
    private javax.swing.JEditorPane bowlPane2;
    private javax.swing.JEditorPane bowlScoreView;
    private javax.swing.JComboBox bowlerCombo1;
    private javax.swing.JTextField buffTxt;
    private javax.swing.JTextField datntimeTxt;
    private javax.swing.JTextField descTxt;
    private javax.swing.JButton dlg3OK;
    private javax.swing.JButton dlg4OK;
    private javax.swing.JButton dlg5OK;
    private javax.swing.JButton dlg6OK;
    private javax.swing.JButton dlgOK2;
    private javax.swing.JMenuItem editBabyCutOver;
    private javax.swing.JMenuItem editChangeRun;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem editSwapStrike;
    private javax.swing.JMenuItem editUndo;
    private javax.swing.JComboBox fielderCombo;
    private javax.swing.JDialog firstInnEndDlg;
    private javax.swing.JEditorPane firstInnSummary;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton matchInitCancel;
    private javax.swing.JDialog matchInitDlg;
    private javax.swing.JButton matchInitOK;
    private javax.swing.JMenuItem matchShowDetails;
    private javax.swing.JDialog matchSummaryDlg;
    private javax.swing.JMenuItem menuFileReload;
    private javax.swing.JMenuItem menuFileUpdate;
    private javax.swing.JDialog nextBatDlg;
    private javax.swing.JDialog nextBowlDlg;
    private javax.swing.JDialog openingBatDlg;
    private javax.swing.JSlider oversSlider;
    private javax.swing.JTextField oversTxt;
    private javax.swing.JDialog runoutBatDlg;
    private javax.swing.JEditorPane scoreView;
    private javax.swing.JCheckBox strkChk;
    private javax.swing.JComboBox teamCombo1;
    private javax.swing.JMenuItem tournRank;
    private javax.swing.JDialog wktDlg;
    private javax.swing.JComboBox wkttypeCombo;
    // End of variables declaration//GEN-END:variables

}
