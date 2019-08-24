/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package crickinfo.manage;

/**
 *
 * @author Shinaz
 */
public class RunoutInfo {

//    String out;

    String next;

    boolean strike;

    public RunoutInfo(String next,boolean s)
    {
     //   this.out = out;
        this.next = next;
        strike = s;
    }

//    public String getOut()
//    {
//        return out;
//    }

    public String getNext()
    {
        return next;
    }

    public boolean isStrike()
    {
        return strike;
    }

    public String toString()
    {
        String s;
        s="[(RunoutInfo) "+next+" "+strike+"]";
        return s;
    }
}
