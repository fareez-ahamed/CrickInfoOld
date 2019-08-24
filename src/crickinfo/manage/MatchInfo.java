/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package crickinfo.manage;

/**
 *
 * @author Shinaz
 */
public class MatchInfo {

    int overs;
    String team1id;
    String team2id;
    String desc;

    public MatchInfo(int o,String team1id,String team2id,String desc)
    {
        overs=o;
        this.team1id=team1id;
        this.team2id=team2id;
        this.desc=desc;
    }
}
