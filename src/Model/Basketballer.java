package Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class Basketballer extends Person implements BasketballerInterface, Comparable<Basketballer> {
    private double height; //in meters
    private LocalDate dateOfBirth;
    private int age;
    private int quantityOfWins;
    protected Map<String, Integer> skills;

    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 42;
    private static final String namesFile = "src/Data/Names.txt";
    private static final String surnamesFile = "src/Data/Surnames.txt";
    private static final String exportFile = "src/Data/export.csv";



    public Basketballer() {

    }

    public Basketballer(String name, String surname, String ID, double height, int dayOfBirth, int monthOfBirth,
                        int yearOfBirth, int quantityOfWins){
        super(name, surname, ID);
        this.height = height;
        this.quantityOfWins = quantityOfWins;

        String dateOfBirthStr = String.format("%d-%d-%d", dayOfBirth, monthOfBirth, yearOfBirth);

        try {
            dateOfBirth = LocalDate.parse(dateOfBirthStr, DateTimeFormatter.ofPattern("d-M-uuuu").
                    withResolverStyle(ResolverStyle.STRICT));

        }catch (DateTimeParseException ex){
            System.out.println("Invalid Date");
        }

        init();
    }

    public Basketballer(String name, String surname, String ID, double height, LocalDate dateOfBirth, int currentYear) {
        super(name, surname, ID);
        this.height = height;
        this.dateOfBirth = dateOfBirth;
        quantityOfWins = 0;

        LocalDate currentDate = LocalDate.parse(String.format("1-1-%d", currentYear), DateTimeFormatter.ofPattern("d-M-uuuu"));

        age = calculateAge(currentDate);

        init();
    }

    private void init(){

        skills = new HashMap<>();
        setRandomSkills();

        if(age > 35)
            aging();
    }

    private int calculateAge(LocalDate currentDate){

        Period period = Period.between(dateOfBirth, currentDate);

        return period.getYears();
    }

    private void setRandomSkills(){
        skills.put("Shoot", ThreadLocalRandom.current().nextInt(1, skillsUpperBoundary));
        skills.put("Pass", ThreadLocalRandom.current().nextInt(1, skillsUpperBoundary));
        skills.put("Block", ThreadLocalRandom.current().nextInt(1, skillsUpperBoundary));
        skills.put("Steal", ThreadLocalRandom.current().nextInt(1, skillsUpperBoundary));
        skills.put("Sprint", ThreadLocalRandom.current().nextInt(1, skillsUpperBoundary));
    }

    @Override
    public void aging() {
        decreaseSkills(decreaseUnit);
    }

    public void decreaseSkills(int units){
        for (String skill : skills.keySet()) {
            int decreasedSkillLevel = skills.get(skill) - units;

            if(decreasedSkillLevel < 1)
                decreasedSkillLevel = 1;

                skills.put(skill, decreasedSkillLevel);
        }
    }

    BasketballerFunctionalInterface in = () -> {
        return  averageSkillScores();
    };

    public double calculateAvgSkillScore(){

        return  in.skillScoreCalculator();
    }

    private double averageSkillScores(){
        double sum = 0;

        for(String skill : skills.keySet())
            sum += skills.get(skill);

        return sum / skills.keySet().size();
    }

    public static ArrayList<Basketballer> getUniqueRandomBasketballers(int quantity, int currentYear){
        ArrayList<Basketballer> result = new ArrayList<>();
        HashSet<String> usedIDs = new HashSet<>();

        //Reading data files
        ArrayList<String> names = readData(namesFile);
        ArrayList<String> surnames = readData(surnamesFile);

        for(int i = 0; i < quantity; i++){

        //Generating random values
        String randomName = names.get(ThreadLocalRandom.current().nextInt(0, names.size()));
        String randomSurname = surnames.get(ThreadLocalRandom.current().nextInt(0, surnames.size()));

        long randomDateStr = ThreadLocalRandom.current().nextLong(

                LocalDate.parse(String.format("1-1-%d", currentYear - MAX_AGE),DateTimeFormatter.ofPattern("d-M-uuuu")).toEpochDay(),
                LocalDate.parse(String.format("1-1-%d", currentYear - MIN_AGE),DateTimeFormatter.ofPattern("d-M-uuuu")).toEpochDay()
                );
        LocalDate randomDateOfBirth = LocalDate.ofEpochDay(randomDateStr);

        double randomHeight = ThreadLocalRandom.current().nextDouble(1.60, 2.40);

        String randomId = generateUniqueId(usedIDs, IDLength);

            Basketballer basketballer = new Basketballer(randomName, randomSurname, randomId, randomHeight,
                    randomDateOfBirth, currentYear);
            result.add(basketballer);
        }

        return result;
    }

    private static ArrayList<String> readData(String FilePath){
        ArrayList<String> data = new ArrayList<>();

        try {
            BufferedReader bf = new BufferedReader(new FileReader(FilePath));
            String line = bf.readLine();

            while(line != null){
                data.add(line);
                line = bf.readLine();
            }

            bf.close();

        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }

        return  data;
    }

    protected String getCSVFormattedData(){
        String part1 = String.format("%s,%s,%s,%.2f,%d,%d,%s,%d,%d," ,
                name, surname, ID, height, age,
                dateOfBirth.getDayOfMonth(), dateOfBirth.getMonth(), dateOfBirth.getYear(),
                quantityOfWins);

        String part2 = String.format("%d,%d,%d,%d,%d", skills.get("Pass"), skills.get("Steal"),
                skills.get("Block"), skills.get("Sprint"), skills.get("Shoot"));

        return part1 + part2;
    }

    public static void exportData(List<Basketballer> basketballers) {

        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(exportFile));

            for(Basketballer basketballer : basketballers){

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

    public static ArrayList<Basketballer> importData(String filePath){
        ArrayList<Basketballer> basketballers= new ArrayList<>();

        try {
            Stream<String> rows = Files.lines(Paths.get(filePath));
            rows
                    .map(x -> x.split(","))
                    .filter(x -> x.length == 14) //Fixme
                    .forEach(row -> {
                            Basketballer basketballer = new Basketballer();
                            basketballer.setName(row[0]);
                            basketballer.setSurname(row[1]);
                            basketballer.setID(row[2]);
                            basketballer.setHeight(Double.parseDouble(row[3]));
                            basketballer.setAge(Integer.parseInt(row[4]));
                            String dateOfBirth = String.format("%d-%s-%d",
                                    Integer.parseInt(row[5]), row[6].charAt(0) + row[6].substring(1, row[6].length()).toLowerCase(),
                                    Integer.parseInt(row[7]));
                            basketballer.setDateOfBirth(LocalDate.parse(dateOfBirth,
                                    DateTimeFormatter.ofPattern("d-MMMM-uuuu")));
                            basketballer.setQuantityOfWins(Integer.parseInt(row[8]));

                            basketballer.setSkills(new HashMap<String, Integer>() {{
                               put("Pass", Integer.parseInt(row[9]));
                               put("Steal", Integer.parseInt(row[10]));
                               put("Block", Integer.parseInt(row[11]));
                               put("Sprint", Integer.parseInt(row[12]));
                               put("Shoot", Integer.parseInt(row[13]));
                            }}
                            );
                            basketballers.add(basketballer);
                    });
                    rows.close();
                    return  basketballers;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getShortenedData(){
        return  String.format("%s %s ID:%s Age:%d Skills score:%.2f",
                name, surname, ID, age, averageSkillScores());
    }


    @Override
    public String toString() {
        String info = String.format("%s %s ID%s %dyrs. %d %s %d %.2fm %d wins " , name, surname, ID, age,
                dateOfBirth.getDayOfMonth(), dateOfBirth.getMonth(), dateOfBirth.getYear(),
                height, quantityOfWins);

         String skillsInfo = skills.toString() + " avg:" + calculateAvgSkillScore();

        return info + skillsInfo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Map<String, Integer> getSkills() {
        return skills;
    }

    public void setSkills(Map<String, Integer> skills) {
        this.skills = skills;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setQuantityOfWins(int quantityOfWins) {
        this.quantityOfWins = quantityOfWins;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public double getHeight() {
        return height;
    }

    public int getQuantityOfWins() {
        return quantityOfWins;
    }

    @Override
    public int compareTo(Basketballer other) {
        int compareResult = surname.compareTo(other.surname);

        if(compareResult == 0)
           return name.compareTo(other.getName());

        return compareResult;
    }
}
