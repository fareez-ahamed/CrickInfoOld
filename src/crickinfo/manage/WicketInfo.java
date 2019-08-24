/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package crickinfo.manage;

/**
 *
 * @author Dev
 */
public class WicketInfo {

    int wkttype;

    Player bowler;

    Player lost;

    Player thirdman;

    public WicketInfo(int t)
    {
        wkttype=t;
    }

    public int getWicketType()
    {
        return wkttype;
    }

    public Player getBowler()
    {
        return bowler;
    }

    public Player getLostBatsman()
    {
        return lost;
    }

    public Player getThirdMan()
    {
        return thirdman;
    }

    public void setBowler(Player p)
    {
        bowler = p;
    }

    public void setLostBatsman(Player p)
    {
        lost = p;
    }

    public void setThirdMan(Player p)
    {
        thirdman = p;
    }
}
