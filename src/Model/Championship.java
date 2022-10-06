package Model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Championship {

   private static final int numberOfTeams = 14;
   private static final int transferUpperLimit = 3;
   private static final int transferLowerLimit = 1;
   private static  final int numberOfRounds = 3;
   private static final String exportFile = "src/Data/ChampionshipResults.txt";

    public void startChampionship(int currentYear, int seasons) {

        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(exportFile));

            for (int i = currentYear; i < currentYear + seasons; i++) {
                startSeason(i, bufferedWriter);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void startSeason(int currentYear, BufferedWriter bufferedWriter){
        ArrayList<Team> teams = Team.generateRandomTeams(numberOfTeams, currentYear);
        makeTransfers(teams);
        HashMap<Team, HashSet<Team>> alreadyPlayed = new HashMap<>();

        startMathches(teams, alreadyPlayed);
        writeData(teams, currentYear, bufferedWriter);
    }

        private void startMathches(ArrayList<Team> teams, HashMap<Team, HashSet<Team>> alreadyPlayed){
        Random random = new Random();

        for (Team currentTeam : teams){
            for (int i = 0; i < numberOfRounds; i++) {
                int oppositeTeamIndex = random.nextInt(teams.size());
                Team oppositeTeam = teams.get(oppositeTeamIndex);

                while(oppositeTeam == currentTeam
                        || (alreadyPlayed.containsKey(currentTeam) && alreadyPlayed.get(currentTeam).contains(oppositeTeam))
                        || (alreadyPlayed.containsKey(oppositeTeam) && alreadyPlayed.get(oppositeTeam).contains(currentTeam))){

                    oppositeTeam = teams.get(random.nextInt(teams.size()));
                }

                match(currentTeam, oppositeTeam);
                updateMatchHistory(alreadyPlayed, currentTeam, oppositeTeam);
            }
        }
    }

    private void writeData(ArrayList<Team> teams, int currentYear, BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.append("Year:" + currentYear + "\n");

            bufferedWriter.append("Teams:" + "\n");

            for (Team team : teams) {
                bufferedWriter.append(String.format("%s %s\n%s\nCoach:%s",
                        team.getCountry(), team.getTeamName(), team.getMotivationalPhrase(), team.getCoach().toString()));

                bufferedWriter.newLine();
                bufferedWriter.newLine();

                for (Basketballer basketballer : team.getBasketballers()){
                    bufferedWriter.append(basketballer.getShortenedData());
                    bufferedWriter.newLine();
                }
                bufferedWriter.newLine();
            }

            bufferedWriter.newLine();
            bufferedWriter.write("Results" + "\n");

            List<Team> sorted = teams.stream()
                    .sorted(Comparator.comparingInt(Team::getQuantityOfWins).reversed())
                    .collect(Collectors.toList());

            for (Team team : sorted) {
                bufferedWriter.append(String.format("%s %d wins\n",
                         team.getTeamName(), team.getQuantityOfWins()));
            }
            bufferedWriter.newLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateMatchHistory(HashMap<Team, HashSet<Team>> alreadyPlayed, Team team1, Team team2){
        if(alreadyPlayed.containsKey(team1)){
            alreadyPlayed.get(team1).add(team2);

        }else {
            HashSet set = new HashSet<>();
            set.add(team2);
            alreadyPlayed.put(team1, set);
        }
    }

    private void match(Team team1, Team team2) {
        double team1Score = getTeamScore(team1);
        double team2Score = getTeamScore(team2);

        if(team1Score >= team2Score)
            team1.win();
        else
            team2.win();
    }

    private double getTeamScore(Team team){
        double score = 0;

        for(Basketballer b : team.getBasketballers())
            score += b.calculateAvgSkillScore();

        score /= team.getBasketballers().size();
        score += team.getCoach().getSkillLevel();

        return score;
    }

    private void makeTransfers(ArrayList<Team> teams) {
        List<Team> part1 = teams.subList(0, numberOfTeams / 2);
        List<Team> part2 = teams.subList(numberOfTeams / 2, teams.size());

        for(int i = 0; i < Math.min(part1.size(), part2.size()); i++){
            changePlayers(part1.get(i), part2.get(i));
        }
    }

    private void changePlayers(Team firstTeam, Team secondTeam) {
        int transfersNumber = ThreadLocalRandom.current().nextInt(transferLowerLimit, transferUpperLimit);

        for (int i = 0; i < transfersNumber; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(0,
                    Math.min(firstTeam.getBasketballers().size(), secondTeam.getBasketballers().size()));

            Basketballer temp = firstTeam.getBasketballers().get(randomIndex);

            firstTeam.getBasketballers().set(randomIndex, secondTeam.getBasketballers().get(randomIndex));

            secondTeam.getBasketballers().set(randomIndex, temp);

        }
    }
}
