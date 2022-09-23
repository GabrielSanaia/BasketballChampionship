package Model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Person {
    protected String name;
    protected String surname;
    protected long ID;

    public Person() {}

    public Person(String name, String surname, long ID) {
        this.name = name;
        this.surname = surname;
        this.ID = ID;
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

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

}
