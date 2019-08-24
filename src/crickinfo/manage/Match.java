/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package crickinfo.manage;

import java.sql.*;
import java.util.*;
import java.io.*;
/**
 *
 * @author Shinaz
 */
public class Match implements Constants, Serializable, Cloneable {

    Team team1;

    Team team2;

    Team battingteam;

    Team bowlingteam;

    Innings firstinn;

    Innings secondinn;

    Innings currentinn;

    Player striker;

    Player nonstriker;

    Player bowler;

    Over overs;

    Vector thisover;

    int ballno;

    transient UIController ui;

    String desc;

    DocFormat doc;

    String winStat;

    public Match(UIController ui)throws SQLException, IOException
    {
        this.ui = ui;
        ballno=0;
        doc = new DocFormat();
        thisover = new Vector();
    }

    public void init()
    {
        firstinn = new Innings(overs);
        currentinn = firstinn;
    }

    public void init2ndInn()throws InvalidBallException
    {
        Team temp;
        secondinn = new Innings(overs,firstinn.getScore()+1);
        temp=battingteam;
        battingteam=bowlingteam;
        bowlingteam=temp;
        ballno=0;
        currentinn = secondinn;
        processBall("start");
        thisover.removeAllElements();
    }

    private Ball encodeBall(String s)
    {
        System.out.println("Called : encodeBall(String s)");
        Ball ball = new Ball(ballno);
        StringTokenizer st;
        String temp;
        s = s.toLowerCase();
        st = new StringTokenizer(s);
        WicketInfo wktinf;
        ball.setBatsman(striker);
        ball.setBowler(bowler);
        ball.setOver(new Over(currentinn.getOver().getBalls()));
        while(st.hasMoreTokens())
        {
            temp = st.nextToken();
            if(temp.equals("no"))
                ball.setNoBall();
            else if(temp.equals("by"))
                ball.setBye();
            else if(temp.equals("wd"))
                ball.setWide();
            else if(temp.equals("lb"))
                ball.setLegBye();
            else if(temp.equals("wk"))
            {
                ball.setWicket();
                ball.setNormal();
                if(currentinn.getWickets()<10)
                {
                    wktinf=ui.getWicketInfo();
                    ball.setWicketInfo(wktinf);
                }
            }
            else if(isAllDigits(temp))
            {
                ball.setRuns(Integer.parseInt(temp));
                ball.setNormal();
            }
            else if(temp.equals("start"))
                ball.setSpecial();
            else
                return null;
        }
        return ball;
    }

    public void processBall(Ball ball)throws InvalidBallException
    {
        String s="";
        System.out.println("processBall(Ball ball)");
        if(ball==null)
            throw new InvalidBallException();
        ballno++;
        currentinn.getBallsVector().add(ball);
        if(ball.isNormal())
        {
            currentinn.addRuns(ball.getRuns());
            currentinn.addBall();
            striker.ballFaced();
            striker.addRunsTaken(ball.getRuns());
            bowler.ballBowled();
            bowler.addRunsGiven(ball.getRuns());
            if(ball.getRuns()%2==1)
                swapStrike();
            s=s+ball.getRuns();
        }
        else if(ball.isNoBall() && !ball.isBye())
        {
            currentinn.addRuns(ball.getRuns()+1);
            bowler.addRunsGiven(ball.getRuns()+1);
            striker.addRunsTaken(ball.getRuns());
            if(ball.getRuns()%2==1)
                swapStrike();
            s=s+"NB+"+ball.getRuns();
        }
        else if(ball.isNoBall() && ball.isBye())
        {
            currentinn.addRuns(ball.getRuns()+1);
            bowler.addRunsGiven(ball.getRuns());
            if(ball.getRuns()%2==1)
                swapStrike();
            s=s+"NB+BY+"+ball.getRuns();
        }
        else if(!ball.isNoBall() && ball.isBye())
        {
            currentinn.addRuns(ball.getRuns());
            currentinn.addBall();
            bowler.addRunsGiven(ball.getRuns());
            bowler.ballBowled();
            striker.ballFaced();
            if(ball.getRuns()%2==1)
                swapStrike();
            s=s+"BY+"+ball.getRuns();
        }
        else if(ball.isWide())
        {
            currentinn.addRuns(ball.getRuns()+1);
            bowler.addRunsGiven(ball.getRuns()+1);
            if(ball.getRuns()%2==1)
                swapStrike();
            s=s+"WD+"+ball.getRuns();
        }
        else if(ball.isSpecial())
        {
            String[] temparr = ui.getOpeningBatsman();
            striker = battingteam.getPlayerByName(temparr[0]);
            striker.setBatted();
            currentinn.addBatsman(striker);
            nonstriker = battingteam.getPlayerByName(temparr[1]);
            nonstriker.setBatted();
            currentinn.addBatsman(nonstriker);
            String temp = ui.getNextBowler();
            bowler = bowlingteam.getPlayerByName(temp);
            bowler.setBowled();
            //ballno--;
        }
        if(ball.isWicket() && currentinn.getWickets()<10)
        {
            s=s+"W";
            if(ball.getWicketType()==BOWLED || ball.getWicketType()==HIT)
            {
                striker.lostWicket();
                bowler.gotWicket();
                if(currentinn.getWickets()!=9)
                {
                    striker = battingteam.getPlayerByName(ui.getNextBatsman());
                    striker.setBatted();
                    currentinn.addBatsman(striker);
                }
                else
                    striker=null;
            }
            else if(ball.getWicketType()==CAUGHT)
            {
                striker.lostWicket();
                bowler.gotWicket();
                ball.getThirdMan().caughtBall();
                if(currentinn.getWickets()!=9)
                {
                    striker = battingteam.getPlayerByName(ui.getNextBatsman());
                    striker.setBatted();
                    currentinn.addBatsman(striker);
                }
                else
                    striker=null;
            }
            else if(ball.getWicketType()==STUMPED)
            {
                striker.lostWicket();
                bowler.gotWicket();
                if(currentinn.getWickets()!=9)
                {
                    striker = battingteam.getPlayerByName(ui.getNextBatsman());
                    striker.setBatted();
                    currentinn.addBatsman(striker);
                }
                else
                    striker=null;
            }
            else if(ball.getWicketType()==RUNOUT)
            {
                RunoutInfo ri=null;
                if(currentinn.getWickets()!=9)
                    ri = ui.getRunoutInfo();
                System.out.println(ri);
                if(striker.equals(ball.getLostBatsman()))
                {
                    striker.lostWicket();
                    ball.getThirdMan().tookRunout();
                    if(currentinn.getWickets()!=9)
                    {
                        if(ri.isStrike())
                        {
                            striker=battingteam.getPlayerByName(ri.getNext());
                            striker.setBatted();
                            currentinn.addBatsman(striker);
                        }
                        else
                        {
                            striker=nonstriker;
                            nonstriker=battingteam.getPlayerByName(ri.getNext());
                            nonstriker.setBatted();
                            currentinn.addBatsman(nonstriker);
                        }
                    }
                }
                else
                {
                    if(currentinn.getWickets()!=9)
                    {
                        nonstriker.lostWicket();
                        ball.getThirdMan().tookRunout();
                        if(ri.isStrike())
                        {
                            nonstriker = striker;
                            striker = battingteam.getPlayerByName(ri.getNext());
                            striker.setBatted();
                            currentinn.addBatsman(striker);
                        }
                        else
                        {
                            nonstriker = battingteam.getPlayerByName(ri.getNext());
                            nonstriker.setBatted();
                            currentinn.addBatsman(nonstriker);
                        }
                    }
                }
            }
            currentinn.addWicket();
        }
        thisover.add(s);
        System.out.println("Finish : "+currentinn.isInningsFinished());
        if(currentinn.getOver().isRound()&&!currentinn.getOver().isZero()&&!currentinn.isInningsFinished()&&!ball.isNoBall()&&!ball.isWide())
        {
            thisover.removeAllElements();
            bowler=bowlingteam.getPlayerByName(ui.getNextBowler());
            bowler.setBowled();
            swapStrike();
        }
        
        if(currentinn.isInningsFinished()&&currentinn==firstinn)
        {
            ui.endOfFirstInnings();
            init2ndInn();
        }
        if(currentinn.isInningsFinished()&&currentinn==secondinn)
        {
            if(firstinn.getScore()>secondinn.getScore())
            {
                winStat=team1.getName()+" won by "+(firstinn.getScore()-secondinn.getScore())+" runs";
            }
            else if(firstinn.getScore()<secondinn.getScore())
            {
                winStat=team2.getName()+" won by "+(10-secondinn.getWickets())+" wickets";
            }
            ui.finishMatch();
        }
    }

    public void processBall(String s)throws InvalidBallException
    {
        System.out.println("Called : processBall(String s)");
        processBall(encodeBall(s));
    }

    public void swapStrike()
    {
        Player temp;
        temp = striker;
        striker = nonstriker;
        nonstriker = temp;
    }

    public void setTeams(String s1,String s2)throws SQLException
    {
        team1 = new Team(s1);
        team2 = new Team(s2);
        battingteam = team1;
        bowlingteam = team2;
    }

    public Team getTeam1()
    {
        return team1;
    }

    public Team getTeam2()
    {
        return team2;
    }

    public void setMatchOvers(int o)
    {
        overs=new Over(o*6);
    }

    public void setDesc(String s)
    {
        desc=s;
    }

    public Team getBattingTeam()
    {
        return battingteam;
    }

    public Innings getCurrentInnings()
    {
        return currentinn;
    }

    public Team getBowlingTeam()
    {
        return bowlingteam;
    }

    public String getWinningStatement()
    {
        return winStat;
    }


    private static boolean isAllDigits(String s)
    {
        int i;
        for(i=0;i<s.length();i++)
        {
            if(!Character.isDigit(s.charAt(i)))
                return false;
        }
        return true;
    }

    public Player getStriker()
    {
        return striker;
    }

    public Player getBowler()
    {
        return bowler;
    }

    public void setBowler(String s)
    {
        bowler = bowlingteam.getPlayerByName(s);
        bowler.setBowled();
    }


    public Player getNonStriker()
    {
        return nonstriker;
    }

    public Over getOvers()
    {
        return overs;
    }

    public String getMainScoreDoc()
    {
        String s="";
        double rr;
        double reqr;
        int brem,rrem;
        int exscore;
        try
        {
        rr=((double)currentinn.getScore()/(double)currentinn.getOver().getBalls())*6;
        exscore=(int)(rr*currentinn.limit.getRoundOver());
        s = new String(doc.getMainDoc());
        s=s.replaceFirst("score", currentinn.getScore()+"/"+currentinn.getWickets());
        s=s.replaceFirst("over", currentinn.getOver().toString());
        s=s.replaceFirst("runrate", String.valueOf(rr));
        s=s.replaceFirst("thisover", thisover.toString().substring(1, thisover.toString().length()-1));
        
        if(currentinn==firstinn)
        {
           s=s.replaceFirst("status","Projected Score : "+String.valueOf(exscore));
           s=s.replaceFirst("remballs", "");
        }
        else if(currentinn==secondinn)
        {
           brem = (currentinn.limit.getBalls()-currentinn.over.getBalls());
           rrem = (firstinn.getScore()+1-currentinn.getScore());
           reqr = (rrem/(double)brem)*6;
           s=s.replaceFirst("status", "Target : "+(firstinn.getScore()+1)+" Required Rate : "+reqr);
           s=s.replaceFirst("remballs", "To get "+(firstinn.getScore()-secondinn.getScore()+1)+" from "+(secondinn.limit.balls-secondinn.over.balls)+" balls");
        }
        s=s.replaceFirst("stkrate", String.valueOf(striker.getStrikeRate()));
        s=s.replaceFirst("ecorate", String.valueOf(bowler.getEconomyRate()));
        s=s.replaceFirst("bowltobat", String.valueOf(bowler.getName()+" to "+striker.getName()));
        System.out.println(s);
        }
        catch(ArithmeticException e)
        {

        }
        return s;
    }

    public String getBatDoc(Innings inn)
    {
        String s="<table width=\"100%\">";
        Player p;
        if(inn==null)
            return "";
        for(int i=0;i<inn.getBattingOrder().size();i++)
        {
            s=s+"<tr";
            p=(Player)inn.getBattingOrder().get(i);
            if(p.hasBatted()==true&&p.isOut()==false)
                s=s+" bgcolor=\"#abcdef\" ";
            if(p==striker)
                s=s+"><td><b>"+p.getName()+"</b></td>";
            else
                s=s+"><td>"+p.getName()+"</td>";
            s=s+"<td><b>"+p.getRunsTaken()+"</b>"+" ("+p.getBallsFaced()+")</td>";
            /*if(p.isOut())
            {
                s=s+"<td>"
            }*/
            s=s+"</tr>";
        }
        s=s+"</table>";
        return s;
    }
    
    public String getBatDoc()
    {
        return getBatDoc(currentinn);
    }

    public String getBowlDoc(Team team)
    {
        String s="<table width=\"100%\"><th>Bowler</th><th>O</th><th>W</th><th>R</th><th>ER</th>";
        Player p;
        Over o;
        if(team==null)
            return "";
        for(int i=0;i<team.getPlayers().size();i++)
        {
            p=(Player)team.getPlayers().get(i);
            if(p.hasBowled())
            {
                s=s+"<tr";

                if(p==bowler)
                    s=s+" bgcolor=\"#abcdef\" ";
                s=s+"><td>"+p.getName()+"</td><td>";
                o=new Over(p.getBallsBowled());
                s=s+o+"</td>";
                s=s+"<td><b>"+p.getWickets()+"</b></td>";
                s=s+"<td>"+p.getRunsGiven()+"</td>";
                s=s+"<td>"+p.getEconomyRate()+"</td></tr>";
            }
        }
        s=s+"</table>";
        return s;
    }

    public String getBowlDoc()
    {
        return getBowlDoc(bowlingteam);
    }
    
    @Override
    public String toString()
    {
        String s = "[(Match) ballno:"+ballno+"\n";
        s=s+"Striker : "+striker;
        s=s+"Non Striker : "+nonstriker;
        s=s+"Bowler : "+bowler;
        s=s+"Batting team : "+battingteam;
        s=s+"Bowling team : "+bowlingteam;
        s=s+"Current Inning : "+currentinn;
        return s;
    }

    public void updateDatabase()throws SQLException
    {
        Connection con = DriverManager.getConnection("jdbc:odbc:blazers");
        Statement st = con.createStatement();
        String sql;
        Player p;
        Ball b;
        String matchid=team1.id+team2.id;
        java.util.Date d = new java.util.Date();
        sql = "insert into matches values('"+matchid+"','"+d.toString()+"','"+team1.id+"','"+team2.id
                +"','',"+firstinn.getScore()+","+secondinn.getScore()+","+firstinn.getWickets()+","+secondinn.getWickets()
                +","+firstinn.getOver().getBalls()+","+secondinn.getOver().getBalls()
                +",'"+winStat+"')";
        st.execute(sql);
        String s="";
        for(int i=0;i<team1.getPlayers().size();i++)
        {
            p = (Player)team1.getPlayers().get(i);
            st.execute("insert into playerdetails values ('"+matchid+"','"+p.id+"',"+p.batted+","+p.bowled
                    +","+p.ballsfaced+","+p.ballsbowled+","+p.runstaken+","+p.runsgiven+","+p.out+","+p.wkts
                    +","+p.catches+","+p.otherwkts+",0)");
        }
        for(int i=0;i<team2.getPlayers().size();i++)
        {
            p = (Player)team2.getPlayers().get(i);
            st.execute("insert into playerdetails values ('"+matchid+"','"+p.id+"',"+p.batted+","+p.bowled
                    +","+p.ballsfaced+","+p.ballsbowled+","+p.runstaken+","+p.runsgiven+","+p.out+","+p.wkts
                    +","+p.catches+","+p.otherwkts+",0)");
        }
        for(int i=1;i<firstinn.balls.size();i++)
        {
            s="";
            b=(Ball)firstinn.balls.get(i);
            s=s+"insert into balls values ('"+matchid+"','"+team2.id+"','"+team1.id+"',"+b.ballno
                    +","+b.over.balls+","+b.balltype+",'"+b.batsman.id+"','"+b.bowler.id
                    +"',"+b.runs+","+b.wkttype+",";
            if(b.thirdman!=null)
                s=s+"'"+b.thirdman.id+"',";
            else
                s=s+"'-',";
            if(b.wktplayer!=null)
                s=s+"'"+b.wktplayer.id+"')";
            else
                s=s+"'-')";
            System.out.println(s);
            st.execute(s);
        }
        for(int i=1;i<secondinn.balls.size();i++)
        {
            s="";
            b=(Ball)secondinn.balls.get(i);
            s=s+"insert into balls values ('"+matchid+"','"+team2.id+"','"+team1.id+"',"+b.ballno
                    +","+b.over.balls+","+b.balltype+",'"+b.batsman.id+"','"+b.bowler.id
                    +"',"+b.runs+","+b.wkttype+",";
            if(b.thirdman!=null)
                s=s+"'"+b.thirdman.id+"',";
            else
                s=s+"'-',";
            if(b.wktplayer!=null)
                s=s+"'"+b.wktplayer.id+"')";
            else
                s=s+"'-')";
            System.out.println(s);
            st.execute(s);
        }
        st.close();
        con.close();
    }

    public Innings getFirstInnings()
    {
        return firstinn;
    }

    public Innings getSecondInnings()
    {
        return secondinn;
    }

    public void setUICon(UIController ui)
    {
        this.ui = ui;
    }
}
