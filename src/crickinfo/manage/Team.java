/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package crickinfo.manage;


import java.util.*;
import java.sql.*;
import java.io.*;
/**
 *
 * @author Dev
 */
public class Team implements Serializable, Cloneable {

    String id;

    String name;

    Vector players;

    public Team(String id)throws SQLException
    {
        this.id=id;
        players = new Vector(11);
        loadTeam();
        print();
    }

    private void loadTeam()throws SQLException
    {
        Connection con = DriverManager.getConnection("jdbc:odbc:blazers");
        Statement st = con.createStatement();
        ResultSet rs;
        String sql="select * from teams where id='"+id+"'";
        rs=st.executeQuery(sql);
        rs.next();
        name=rs.getString(2);
        sql="select id,name from players where status='P' and team='"+id+"'";
        rs=st.executeQuery(sql);
        while(rs.next())
        {
            players.add(new Player(rs.getString("id"),rs.getString("name")));
        }
    }

    public String[] getPlayersList()
    {
        String[] arr = new String[players.size()];
        Player temp;
        for(int i=0;i<players.size();i++)
        {
            temp = (Player)players.get(i);
            arr[i] = temp.getName();
        }
        return arr;
    }

    public Vector getPlayers()
    {
        return players;
    }

    public Player getPlayerByName(String name)
    {
        Player temp;
        for(int i=0;i<players.size();i++)
        {
            temp=(Player)players.get(i);
            if(name.equals(temp.getName()))
                return temp;
        }
        return new Player();
    }

    public String[] getNonBattedList()
    {
        Vector v = new Vector();
        String s[];
        Player p;
        for(int i=0;i<players.size();i++)
        {
            p=(Player)players.get(i);
            if(!p.hasBatted())
                v.add(p.getName());
        }
        s = new String[v.size()];
        for(int i=0;i<v.size();i++)
            s[i]=(String)v.get(i);
        return s;
    }

    public String getName()
    {
        return name;
    }

    public void print()
    {
        for(int i=0;i<players.size();i++)
            System.out.println(((Player)(players.get(i))).getName());
    }

    public String toString()
    {
        String s="[(Team) name:"+name+"\n";
        for(int i=0;i<players.size();i++)
        {
            s=s+players.get(i);
        }
        s=s+"];\n";
        return s;
    }
}
