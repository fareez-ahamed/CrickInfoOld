/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

/**
 *
 * @author Shinaz
 */
class TTC {

    public void test()
    {
        try{
        this.wait(1000);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        System.out.println("Hi");
        this.notifyAll();
    }

    public void test1()
    {
        try{
        this.wait();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        System.out.println("test1");
    }
}

public class TTClass
{
    public static void main(String args[])throws Exception
    {
        final TTC t = new TTC();
        Thread th = new Thread(){

            public void run()
            {
                t.test();
            }
        };
        th.start();
        t.test1();
    }
}