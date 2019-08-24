/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package crickinfo.manage;

import java.io.*;
/**
 *
 * @author darkdevs
 */
public class DocFormat implements Serializable, Cloneable {

    byte buff[];
    String mainDoc;
    String batDoc;
    String bowlDoc;

    public DocFormat()throws IOException
    {
        loadMainDoc();
    }

    private void loadMainDoc()throws IOException
    {
        FileInputStream fin;
        File f=new File("C:\\docs\\mainscore.html");
        fin = new FileInputStream(f);
        buff = new byte[(int)f.length()];
        fin.read(buff);
        mainDoc = new String(buff);
    }
    
    public String getMainDoc()
    {
        return mainDoc;
    }
}
