package Model;

import Enums.SortingParameter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Statistics extends StatisticsAbstract {

    private static final String exportFile = "src/Data/sortedStatistics.csv";

    @Override
    public void exportSortedBasketballers(SortingParameter parameter, ArrayList<Basketballer> basketballers) {

        List<Basketballer> sortedList = basketballers.stream()
                .sorted(new Comparator<Basketballer>() {
                            @Override
                            public int compare(Basketballer o1, Basketballer o2) {
                                switch (parameter) {
                                    case WIN_RATE:
                                        return Integer.compare(o1.getQuantityOfWins(), o2.getQuantityOfWins());

                                    case SKILLS_SCORE:
                                        return Double.compare(o1.calculateAvgSkillScore(), o2.calculateAvgSkillScore());

                                    case AGE:
                                        return Integer.compare(o1.getAge(), o2.getAge());

                                    case HEIGHT:
                                        return Double.compare(o1.getHeight(), o2.getHeight());

                                    case ID:
                                        return (o1.getID().compareTo(o2.getID()));

                                    default:
                                        return o1.compareTo(o2);
                                }
                            }
                        }
                )
                .collect(Collectors.toList());

        writeData(sortedList);
    }

    private void writeData(List<Basketballer> basketballers) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(exportFile));

            LocalDate today = LocalDate.now();

            bufferedWriter.write(
                    String.format("Date: %d %s %d", today.getDayOfMonth(), today.getMonth(), today.getYear()));

            bufferedWriter.newLine();

            for (Basketballer basketballer : basketballers) {
                bufferedWriter.write(basketballer.getCSVFormattedData());
                bufferedWriter.newLine();
            }


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}