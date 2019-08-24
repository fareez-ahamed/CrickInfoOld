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
public class Player implements Serializable, Cloneable {

    String id;

    String name;

    boolean batted;

    boolean bowled;

    int ballsfaced;

    int ballsbowled;

    int runstaken;

    int runsgiven;

    boolean out;

    int wkts;

    int catches;

    int otherwkts;

    int extras;

    public Player()
    {
        out=false;
        batted=false;
        bowled=false;
        wkts=0;
    }

    public Player(String id,String name)
    {
        this.id = id;
        this.name = name;
        out=false;
        batted=false;
        bowled=false;
        wkts=0;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String s)
    {
        name=s;
    }

    public int getRunsTaken()
    {
        return runstaken;
    }

    public int getRunsGiven()
    {
        return runsgiven;
    }

    public void addRunsTaken(int r)
    {
        runstaken+=r;
    }

    public void addRunsGiven(int r)
    {
        runsgiven+=r;
    }

    public void ballFaced()
    {
        ballsfaced++;
    }

    public void ballBowled()
    {
        ballsbowled++;
    }

    public void lostWicket()
    {
        out=true;
    }

    public void gotWicket()
    {
        wkts++;
    }

    public void caughtBall()
    {
        catches++;
    }

    public void tookRunout()
    {
        otherwkts++;
    }

    public double getStrikeRate()
    {
        if(ballsfaced==0)
            return 0;
        return (runstaken/(double)ballsfaced)*100;
    }

    public double getEconomyRate()
    {
        if(ballsbowled==0)
            return 0;
        return (runsgiven/(double)ballsbowled)*6;
    }

    public int getBallsFaced()
    {
        return ballsfaced;
    }

    public int getBallsBowled()
    {
        return ballsbowled;
    }

    public int getWickets()
    {
        return wkts;
    }

    public boolean isOut()
    {
        return out;
    }

    public boolean equals(Player p)
    {
        if(p.id.equals(this.id))
            return true;
        return false;
    }

    public boolean hasBatted()
    {
        return batted;
    }

    public boolean hasBowled()
    {
        return bowled;
    }

    public void setBatted()
    {
        batted=true;
    }

    public void setBowled()
    {
        bowled=true;
    }

    public void addExtra(int r)
    {
        extras+=r;
    }

    @Override
    public String toString()
    {
        String s="[(Player) Name:"+name+"Balls faced:"+ballsfaced+","+"Runs:"+runstaken+","+"Bowld:"+ballsbowled+","+"BRuns:"+runsgiven+
                ",Batted:"+batted+",Bowled:"+bowled+",Wkts:"+wkts+",out:"+out+"]\n";
        return s;
    }
}
