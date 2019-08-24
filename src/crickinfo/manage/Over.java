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
public class Over implements Serializable, Cloneable {

    int balls;

    public Over()
    {
        balls=0;
    }

    public Over(int o)
    {
        balls=o;
    }

    public int getBalls()
    {
        return balls;
    }

    public void setBalls(int b)
    {
        balls=b;
    }

    public boolean isZero()
    {
        if(balls==0)
            return true;
        return false;
    }

    public int getRoundOver()
    {
        return (int)(balls/6);
    }
    public void increment()
    {
        balls++;
    }

    public boolean isRound()
    {
        if(balls%6==0)
            return true;
        return false;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer(5);
        sb.append((int)(balls/6));
        sb.append(".");
        sb.append(balls%6);
        return sb.toString();
    }

    public boolean equals(Over o)
    {
        if(o.balls==balls)
            return true;
        return false;
    }
}
