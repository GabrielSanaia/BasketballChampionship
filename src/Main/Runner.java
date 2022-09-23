package Main;

import Model.Basketballer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Runner {
    public static void main(String[] args) {
//        Basketballer ja = new Basketballer("Ja", "Morant", 123, 1.91, 10, 8,
//                1999, 56);
//
//        System.out.println(ja.getName());
//        System.out.println(ja.getAge());
//        System.out.println(ja.getSkills());
//        System.out.println(ja.calculateAvgSkillScore());
//        System.out.println(ja.getID());

        ArrayList<Basketballer> basketballers = Basketballer.getUniqueRandomBasketballers(1000);

        Collections.sort(basketballers);
        for(Basketballer b : basketballers){
            System.out.println(b);
        }


    }
}
