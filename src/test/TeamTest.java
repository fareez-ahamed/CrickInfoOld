/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import crickinfo.manage.*;
/**
 *
 * @author Shinaz
 */
public class TeamTest {

    public static void main(String args[])throws Exception
    {
        Team t = new Team("LG");
        for(int i=0;i<1;i++)
        System.out.println(t.getPlayersList()[i]);
    }
}
