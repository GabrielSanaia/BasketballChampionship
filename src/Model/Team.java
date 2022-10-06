package Model;

import Enums.Country;
import Enums.TeamName;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Team implements TeamInterface{
    private TeamName teamName;
    private Country country;
    private Coach coach;
    private List<Basketballer> basketballers;
    private int quantityOfWins;
    private String motivationalPhrase;

    private static final int numberofBasketballers = 20;

    public Team() {
    }

    public Team(TeamName teamName, Country country, Coach coach, List<Basketballer> basketballers, String motivationalPhrase) {
        this.teamName = teamName;
        this.country = country;
        this.coach = coach;
        this.basketballers = basketballers;
        this.motivationalPhrase = motivationalPhrase;
        quantityOfWins = 0;
    }

    @Override
    public String generateMotivationalPhrase(){
        String[] words = {"Go", "Come on", "Win", "Defence", "Clap", "Shoot"};
        Random random = new Random();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(teamName);
        stringBuilder.append(" ");
        stringBuilder.append(country);
        stringBuilder.append(" ");
        stringBuilder.append(words[random.nextInt(words.length)]);
        stringBuilder.append(" ");
        stringBuilder.append(words[random.nextInt(words.length)]);

        return stringBuilder.toString();
    }
    
    public static ArrayList<Team> generateRandomTeams(int numberOfTeams, int currentYear) {
        ArrayList<Team> result = new ArrayList<>();

        TeamName teamNames[] = TeamName.values();
        Country countries[] = Country.values();

        ArrayList<Basketballer> basketballersList = Basketballer.getUniqueRandomBasketballers(
                numberofBasketballers * numberOfTeams, currentYear);

        ArrayList<Coach> coachesList = Coach.getUniqueRandomCoachs(numberOfTeams);

        for (int teamIndex = 0; teamIndex < numberOfTeams; teamIndex++) {
            Team team = new Team();

            if (teamIndex >= teamNames.length) {
                System.out.println("Not enough team names provided!");
                return result;
            }
                team.setTeamName(teamNames[teamIndex]);

            team.setCountry(countries[ThreadLocalRandom.current().nextInt(countries.length)]);
            team.setMotivationalPhrase(team.generateMotivationalPhrase());

            if(teamIndex >= coachesList.size()) {
                System.out.println("Not enough coaches provided!");
                return result;
            }
                team.setCoach(coachesList.get(teamIndex));

            team.setBasketballers(basketballersList.subList(teamIndex * numberofBasketballers,
                    teamIndex * numberofBasketballers + numberofBasketballers));

            result.add(team);
        }

        return result;
    }

    public void win(){
        quantityOfWins++;
        for (Basketballer basketballer : basketballers) {
            basketballer.setQuantityOfWins(basketballer.getQuantityOfWins() + 1);
        }
    }

    public void setTeamName(TeamName teamName) {
        this.teamName = teamName;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public void setBasketballers(List<Basketballer> basketballers) {
        this.basketballers = basketballers;
    }

    public void setQuantityOfWins(int quantityOfWins) {
        this.quantityOfWins = quantityOfWins;
    }

    public void setMotivationalPhrase(String motivationalPhrase) {
        this.motivationalPhrase = motivationalPhrase;
    }

    public TeamName getTeamName() {
        return teamName;
    }

    public Country getCountry() {
        return country;
    }

    public String getMotivationalPhrase() {
        return motivationalPhrase;
    }

    public List<Basketballer> getBasketballers() {
        return basketballers;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getQuantityOfWins() {
        return quantityOfWins;
    }

    @Override
    public String toString() {
        return String.format("%s %s\nCoach: %s %s\n%s\n%s",
                teamName, country, coach.getName(), coach.getSurname(), motivationalPhrase, basketballers.toString());
    }
}
