package Model;

import java.time.LocalDate;

public interface BasketballerInterface {

    int decreaseUnit = 3;
    static final String namesFile = "src/Data/Names.txt";

    static final String surnamesFile = "src/Data/Surnames.txt";

    void aging();
}
