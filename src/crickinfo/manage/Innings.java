/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package crickinfo.manage;

import java.util.*;
import java.io.*;
/**
 *
 * @author Shinaz
 */
public class Innings implements Serializable, Cloneable {

    Vector balls;

    int extras;

    Vector battingorder;

    Over over;

    Over limit;

    String batteamid;

    String bowlteamid;
    
    int score;

    int wkts;

    boolean secinn;

    int target;

    public Innings(Over o)
    {
        limit = o;
        secinn = false;
        over = new Over();
        balls=new Vector();
        battingorder = new Vector();
        wkts=0;
        extras=0;
    }

    public Innings(Over o,int tar)
    {
        limit = o;
        secinn = true;
        target = tar;
        over = new Over();
        balls=new Vector();
        battingorder = new Vector();
        wkts=0;
        extras=0;
    }

    public Vector getBallsVector()
    {
        return balls;
    }

    public Vector getBattingOrder()
    {
        return battingorder;
    }

    public boolean isInningsFinished()
    {
        if(over.equals(limit))
            return true;
        else if(wkts==10)
            return true;
        else if(secinn)
        {
            if(score>=target)
                return true;
        }
        return false;
    }

    public void addBatsman(Player p)
    {
        battingorder.add(p);
    }

    public int getWktType(Player p)
    {
        Ball b;
        for(int i=0;i<balls.size();i++)
        {
            b = (Ball) balls.get(i);
            if(p==b.getLostBatsman())
                return b.getWicketType();
        }
        return -1;
    }

    public void addRuns(int r)
    {
        score+=r;
    }

    public void addBall()
    {
        over.increment();
    }

    public void addWicket()
    {
        wkts++;
    }

    public boolean bowlerChange()
    {
        if(over.isRound())
            return true;
        else
            return false;
    }

    public Over getOver()
    {
        return over;
    }

    public int getScore()
    {
        return score;
    }

    public int getWickets()
    {
        return wkts;
    }

    public void addExtras(int r)
    {
        extras+=r;
    }

    public int getExtras()
    {
        return extras;
    }

    public String toString()
    {
        String s="[(Innings Overs:"+limit+",now:"+over+",score:"+score+",wkts:"+wkts+";\n";
        for(int i=0;i<balls.size();i++)
        {
            s=s+balls.get(i);
        }
        s=s+"];";
        return s;
    }

    
}
