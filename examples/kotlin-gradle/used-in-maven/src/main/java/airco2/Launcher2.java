package airco2;

import airco.Launcher;
import org.jeasy.rules.api.Facts;

public class Launcher2 {

    public static void main(String[] args) {
        Facts f=new Facts();
        System.out.println(new Launcher().exec(args));
    }

}