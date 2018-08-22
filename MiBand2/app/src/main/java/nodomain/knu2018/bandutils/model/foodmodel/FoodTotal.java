package nodomain.knu2018.bandutils.model.foodmodel;

import java.util.ArrayList;

public class FoodTotal {
    private String intakeType;

    ArrayList<FoodCard> foodCardArrayList;

    String Date;
    String Time;
    String StartDate;
    String StartTime;
    String EndDate;
    String EndTime;
    String Duration;
    String Type;

    String Gokryu;
    String Beef;
    String Vegetable;
    String Fat;
    String Milk;
    String Fruit;
    String Exchange;
    String Kcal;

    String Satisfaction;

    public FoodTotal() {
    }

    public FoodTotal(String intakeType, ArrayList<FoodCard> foodCardArrayList) {
        this.intakeType = intakeType;
        this.foodCardArrayList = foodCardArrayList;
    }

    public String getIntakeType() {
        return intakeType;
    }

    public void setIntakeType(String intakeType) {
        this.intakeType = intakeType;
    }

    public ArrayList<FoodCard> getFoodCardArrayList() {
        return foodCardArrayList;
    }

    public void setFoodCardArrayList(ArrayList<FoodCard> foodCardArrayList) {
        this.foodCardArrayList = foodCardArrayList;
    }
}
