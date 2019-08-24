/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package crickinfo.manage;

import crickinfo.manage.*;
import java.io.*;
import java.sql.*;

/**
 *
 * @author darkdevs
 */
public class DataUpdater {

    public static void main(String args[])throws Exception
    {
        Match m;
        Player p;
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("C:\\docs\\update.txt"));
        Connection con = DriverManager.getConnection("jdbc:odbc:blazers");
        Statement st = con.createStatement();
        m = (Match)in.readObject();
        String matchid = m.team1.id+m.team2.id;
        Ball b;
        String s="";
        String sql = "insert into matches values('"+matchid+"','"+(new java.util.Date()).toString()+"','"+m.team1.id+"','"+m.team2.id
                +"','',"+m.firstinn.getScore()+","+m.secondinn.getScore()+","+m.firstinn.getWickets()+","+m.secondinn.getWickets()
                +","+m.firstinn.getOver().getBalls()+","+m.secondinn.getOver().getBalls()
                +",'"+m.winStat+"')";
        st.execute(sql);
//        for(int i=0;i<m.team1.getPlayers().size();i++)
//        {
//            p = (Player)m.team1.getPlayers().get(i);
//            st.execute("insert into playerdetails values ('"+matchid+"','"+p.id+"',"+p.batted+","+p.bowled
//                    +","+p.ballsfaced+","+p.ballsbowled+","+p.runstaken+","+p.runsgiven+","+p.out+","+p.wkts
//                    +","+p.catches+","+p.otherwkts+",0)");
//        }
//        for(int i=0;i<m.team2.getPlayers().size();i++)
//        {
//            p = (Player)m.team2.getPlayers().get(i);
//            st.execute("insert into playerdetails values ('"+matchid+"','"+p.id+"',"+p.batted+","+p.bowled
//                    +","+p.ballsfaced+","+p.ballsbowled+","+p.runstaken+","+p.runsgiven+","+p.out+","+p.wkts
//                    +","+p.catches+","+p.otherwkts+",0)");
//        }
//        for(int i=1;i<m.firstinn.balls.size();i++)
//        {
//            s="";
//            b=(Ball)m.firstinn.balls.get(i);
//            s=s+"insert into balls values ('"+matchid+"','"+m.team2.id+"','"+m.team1.id+"',"+b.ballno
//                    +","+b.over.balls+","+b.balltype+",'"+b.batsman.id+"','"+b.bowler.id
//                    +"',"+b.runs+","+b.wkttype+",";
//            if(b.thirdman!=null)
//                s=s+"'"+b.thirdman.id+"',";
//            else
//                s=s+"'-',";
//            if(b.wktplayer!=null)
//                s=s+"'"+b.wktplayer.id+"')";
//            else
//                s=s+"'-')";
//            System.out.println(s);
//            st.execute(s);
//        }
//        for(int i=1;i<m.secondinn.balls.size();i++)
//        {
//            s="";
//            b=(Ball)m.secondinn.balls.get(i);
//            s=s+"insert into balls values ('"+matchid+"','"+m.team2.id+"','"+m.team1.id+"',"+b.ballno
//                    +","+b.over.balls+","+b.balltype+",'"+b.batsman.id+"','"+b.bowler.id
//                    +"',"+b.runs+","+b.wkttype+",";
//            if(b.thirdman!=null)
//                s=s+"'"+b.thirdman.id+"',";
//            else
//                s=s+"'-',";
//            if(b.wktplayer!=null)
//                s=s+"'"+b.wktplayer.id+"')";
//            else
//                s=s+"'-')";
//            System.out.println(s);
//            st.execute(s);
//        }
        st.close();
        con.close();
        System.out.println(m);
    }
}
