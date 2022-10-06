package Model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Person {
    protected String name;
    protected String surname;
    protected String ID;

    protected static int skillsUpperBoundary = 10;
    protected static int IDLength = 11; // FIXME: IDLength

    public Person() {}

    public Person(String name, String surname, String ID) {
        this.name = name;
        this.surname = surname;
        this.ID = ID;
    }

    protected static String generateUniqueId(Set<String> usedIds, int IDLength){
        int[] digits = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        StringBuilder IDBuilder = new StringBuilder(IDLength);
        Random random = new Random();

        for (int i = 0; i < IDLength; i++) {
            IDBuilder.append(digits[random.nextInt(10)]);
        }

        while(usedIds.contains(IDBuilder.toString())) {
            //IDBuilder.setLength(0);
            for (int i = 0; i < IDLength; i++) {

                IDBuilder.append(digits[random.nextInt(10)]);
            }
        }
        String ID = IDBuilder.toString();
        usedIds.add(ID);

        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

}
