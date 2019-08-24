/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package crickinfo.manage;

import java.io.*;
/**
 *
 * @author Shinaz
 */
public interface UIController extends Serializable {

    public WicketInfo getWicketInfo();

    public String getNextBatsman();
    
    public String[] getOpeningBatsman();

    public RunoutInfo getRunoutInfo();

    public String getNextBowler();

    public void initMatch();

    public void finishMatch();

    public void endOfFirstInnings();
}
