/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package crickinfo.manage;

import java.io.*;
/**
 *
 * @author Dev
 */
public class Ball implements Serializable, Cloneable {

    int ballno;

    Over over;

    int balltype;

    Player batsman;

    Player bowler;

    int runs;

    int wkttype;

    Player thirdman;

    Player wktplayer;

    public Ball(int no)
    {
        ballno=no;
        balltype=0;
    }

    public Ball(int no,Player bowl,Player bat)
    {
        ballno = no;
        bowler = bowl;
        batsman = bat;
        balltype=0;
    }

    public void addRuns(int r)
    {
        runs=runs+r;
    }

    public void setRuns(int r)
    {
        runs=r;
    }

    public int getRuns()
    {
        return runs;
    }

    public Player getBowler()
    {
        return bowler;
    }

    public Player getBatsman()
    {
        return batsman;
    }

    public Over getOver()
    {
        return over;
    }

    public int getWicketType()
    {
        return wkttype;
    }

    public Player getLostBatsman()
    {
        return wktplayer;
    }

    public void setNoBall()
    {
        balltype = balltype | Constants.NO_BALL;
    }

    public void setBye()
    {
        balltype = balltype | Constants.BYE;
    }

    public void setWide()
    {
         balltype = balltype | Constants.WIDE;
    }

    public void setLegBye()
    {
         balltype = balltype | Constants.LEGBYE;
    }

    public void setWicket()
    {
        balltype = balltype | Constants.WICKET;
    }

    public void setNormal()
    {
        balltype = balltype | Constants.NORMAL;
    }

    public void setWicketInfo(WicketInfo wi)
    {
        wkttype = wi.getWicketType();
        wktplayer = wi.getLostBatsman();
        thirdman = wi.getThirdMan();
    }

    public void setBatsman(Player p)
    {
        batsman = p;
    }

    public void setBowler(Player p)
    {
        bowler = p;
    }

    public void setOver(Over o)
    {
        over = o;
    }

    public void setSpecial()
    {
        balltype=0xF000;
    }
    
    public boolean isWicket()
    {
        if((balltype & Constants.WICKET)==Constants.WICKET)
            return true;
        return false;
    }

    public boolean isNoBall()
    {
        if((balltype & Constants.NO_BALL)==Constants.NO_BALL)
            return true;
        return false;
    }

    public boolean isWide()
    {
        if((balltype & Constants.WIDE)==Constants.WIDE)
            return true;
        return false;
    }

    public boolean isNormal()
    {
        if(balltype==Constants.NORMAL || balltype==(Constants.NORMAL|Constants.WICKET))
            return true;
        return false;
    }

    public boolean isSpecial()
    {
        if(balltype==0xF000)
            return true;
        return false;
    }

    public boolean isBye()
    {
        if((balltype & Constants.BYE)==Constants.BYE)
            return true;
        return false;
    }

    public Player getThirdMan()
    {
        return thirdman;
    }

    @Override
    public String toString()
    {
        String s="[(Ball) "+ballno+","+balltype+",Batsman:"+batsman+" ,Bowler"+bowler+","+over+","+runs;
        if(wktplayer!=null)
            s=s+", Thirdman:"+thirdman+" ,Wktplr:"+wktplayer+" ,wkt:"+wkttype;
        s=s+"]\n";
        return s;
    }
}
