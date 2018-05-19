package nodomain.knu2018.gadgetbridge.model;

public class Meal {

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


    public Meal(String date, String time, String startDate, String startTime, String endDate, String endTime, String duration, String type,
                String gokryu, String beef, String vegetable, String fat, String milk, String fruit,
                String exchange, String kcal, String satisfaction) {
        Date = date;
        Time = time;
        StartDate = startDate;
        StartTime = startTime;
        EndDate = endDate;
        EndTime = endTime;
        Duration = duration;
        Type = type;
        Gokryu = gokryu;
        Beef = beef;
        Vegetable = vegetable;
        Fat = fat;
        Milk = milk;
        Fruit = fruit;
        Exchange = exchange;
        Kcal = kcal;
        Satisfaction = satisfaction;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getGokryu() {
        return Gokryu;
    }

    public void setGokryu(String gokryu) {
        Gokryu = gokryu;
    }

    public String getBeef() {
        return Beef;
    }

    public void setBeef(String beef) {
        Beef = beef;
    }

    public String getVegetable() {
        return Vegetable;
    }

    public void setVegetable(String vegetable) {
        Vegetable = vegetable;
    }

    public String getFat() {
        return Fat;
    }

    public void setFat(String fat) {
        Fat = fat;
    }

    public String getMilk() {
        return Milk;
    }

    public void setMilk(String milk) {
        Milk = milk;
    }

    public String getFruit() {
        return Fruit;
    }

    public void setFruit(String fruit) {
        Fruit = fruit;
    }

    public String getExchange() {
        return Exchange;
    }

    public void setExchange(String exchange) {
        Exchange = exchange;
    }

    public String getKcal() {
        return Kcal;
    }

    public void setKcal(String kcal) {
        Kcal = kcal;
    }

    public String getSatisfaction() {
        return Satisfaction;
    }

    public void setSatisfaction(String satisfaction) {
        Satisfaction = satisfaction;
    }
}
