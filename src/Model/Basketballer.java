package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Basketballer extends Person implements BasketballerInterface, Comparable<Basketballer> {
    private double height; //in meters
    private LocalDate dateOfBirth;
    private int age;
    private int quantityOfWins;
    private Map<String, Integer> skills;



    public Basketballer(String name, String surname, long ID, double height, int dayOfBirth, int monthOfBirth,
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

    public Basketballer(String name, String surname, long ID, double height, LocalDate dateOfBirth, int quantityOfWins) {
        super(name, surname, ID);
        this.height = height;
        this.dateOfBirth = dateOfBirth;
        this.quantityOfWins = quantityOfWins;

        init();
    }

    private void init(){
        age = calculateAge();

        skills = new HashMap<>();
        setRandomSkills();

        if(age > 35)
            aging();
    }

    private int calculateAge(){

        LocalDate today = LocalDate.now();

        Period period = Period.between(dateOfBirth, today);

        return period.getYears();
    }

    private void setRandomSkills(){
        skills.put("Shoot", ThreadLocalRandom.current().nextInt(1, 11));
        skills.put("Pass", ThreadLocalRandom.current().nextInt(1, 11));
        skills.put("Block", ThreadLocalRandom.current().nextInt(1, 11));
        skills.put("Steal", ThreadLocalRandom.current().nextInt(1, 11));
        skills.put("Sprint", ThreadLocalRandom.current().nextInt(1, 11));
    }

    @Override
    public void aging() {
        decreaseSkills();
    }

    private void decreaseSkills(){
        for (String skill : skills.keySet()) {
            int decreasedSkillLevel = skills.get(skill) - decreaseUnit;

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

    public static ArrayList<Basketballer> getUniqueRandomBasketballers(int quantity){
        ArrayList<Basketballer> result = new ArrayList<>();
        HashSet<Long> usedIDs = new HashSet<>();

        //Reading data files
        ArrayList<String> names = readData(namesFile);
        ArrayList<String> surnames = readData(surnamesFile);

        for(int i = 0; i < quantity; i++){

        //Generating random values
        String randomName = names.get(ThreadLocalRandom.current().nextInt(0, names.size()));
        String randomSurname = surnames.get(ThreadLocalRandom.current().nextInt(0, surnames.size()));

        long randomDateStr = ThreadLocalRandom.current().nextLong(
                LocalDate.parse("1-1-1980",DateTimeFormatter.ofPattern("d-M-uuuu")).toEpochDay(),
                LocalDate.parse("1-1-2004",DateTimeFormatter.ofPattern("d-M-uuuu")).toEpochDay()
                );
        LocalDate randomDateOfBirth = LocalDate.ofEpochDay(randomDateStr);

        int randomQuantityOfWins = ThreadLocalRandom.current().nextInt(0,82);
        double randomHeight = ThreadLocalRandom.current().nextDouble(1.60, 2.40);

        long randomId = generateUniqueId(usedIDs);

            Basketballer basketballer = new Basketballer(randomName, randomSurname, randomId, randomHeight,
                    randomDateOfBirth, randomQuantityOfWins);
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

    private static long generateUniqueId(Set<Long> usedIds){
        long randomId = ThreadLocalRandom.current().nextLong(1, 10000);

        while (usedIds.contains(randomId))
            randomId = ThreadLocalRandom.current().nextLong(1, 10000);

        usedIds.add(randomId);
        return randomId;
    }

    @Override
    public String toString() {
        String info = String.format("%s %s ID%d %dyrs. %d %s %d %.2fm %d wins " , name, surname, ID, age,
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

    @Override
    public int compareTo(Basketballer other) {
        int compareResult = surname.compareTo(other.surname);

        if(compareResult == 0)
           return name.compareTo(other.getName());

        return compareResult;
    }
}
