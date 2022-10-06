package Main;

import Enums.SortingParameter;
import Model.*;


import java.util.ArrayList;

public class Runner {
    public static void main(String[] args) {
//        Basketballer ja = new Basketballer("Ja", "Morant", "12345678910", 1.91, 10, 8,
//                1999, 56);

//        System.out.println(ja.getName());
//        System.out.println(ja.getAge());
//        System.out.println(ja.getSkills());
//        System.out.println(ja.calculateAvgSkillScore());
//        System.out.println(ja.getID());
//
        ArrayList<Basketballer> basketballers = Basketballer.getUniqueRandomBasketballers(1000, 2000);
        Basketballer.exportData(basketballers);
//
        ArrayList<Basketballer> basketballersImport = Basketballer.importData("src/Data/export.csv");
//
        for (Basketballer basketballer : basketballersImport){
            System.out.println(basketballer);
        }
//
//
//        Statistics s = new Statistics();
//        s.exportSortedBasketballers(SortingParameter.AGE, basketballers);

//        ArrayList<Coach> coachs = Coach.getUniqueRandomCoachs(14);
//
//        for (Coach coach : coachs){
//            System.out.println(coach);
//        }

//        ArrayList<Team> teams = Team.generateRandomTeams(14, 2022);
//
//        for (Team t : teams) {
//            System.out.println(t);
//            System.out.println();
//        }

        Championship championship = new Championship();

        championship.startChampionship(2020, 10);




    }
}
