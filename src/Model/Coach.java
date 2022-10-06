package Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Coach extends Person{

    private int skillLevel;

    public Coach() {
    }

    public Coach(String name, String surname, String ID, int skillLevel) {
        super(name, surname, ID);
        this.skillLevel = skillLevel;
    }

    public static ArrayList<Coach> getUniqueRandomCoachs(int quantity) {
        ArrayList<Coach> result = new ArrayList<>();
        Set<String> usedIDs = new HashSet<>();

        for (int i = 0; i < quantity; i++) {

        String name = generateRandomString(ThreadLocalRandom.current().nextInt(2,10));
        String surname = generateRandomString(ThreadLocalRandom.current().nextInt(2,10));

        String ID = generateUniqueId(usedIDs, IDLength);

        int skillLevel = ThreadLocalRandom.current().nextInt(0, skillsUpperBoundary);

        result.add(new Coach(name, surname, ID, skillLevel));
        }

        return result;
    }

    private static String generateRandomString(int length){
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        Random random = new Random();

        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            stringBuilder.append(alphabet[random.nextInt(26)]);
        }

        return  stringBuilder.toString();
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    @Override
    public String toString(){
        return String.format("%s %s %s %d", name, surname, ID, skillLevel);
    }
}
