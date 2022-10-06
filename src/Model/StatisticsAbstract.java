package Model;

import Enums.SortingParameter;

import java.util.ArrayList;

public abstract class StatisticsAbstract {
    public abstract void exportSortedBasketballers(SortingParameter parameter, ArrayList<Basketballer> basketballers);
}
