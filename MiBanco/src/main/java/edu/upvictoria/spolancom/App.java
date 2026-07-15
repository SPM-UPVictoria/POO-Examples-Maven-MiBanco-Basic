package edu.upvictoria.spolancom;


import edu.upvictoria.spolancom.utils.Leer;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Leer leer = new Leer();

        int a = leer.leerInt();

        System.out.println(a);
    }
}
