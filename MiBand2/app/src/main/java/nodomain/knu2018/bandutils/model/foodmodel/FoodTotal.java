package nodomain.knu2018.bandutils.model.foodmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class FoodTotal implements Parcelable {
    private String intakeType;
    private ArrayList<FoodCard> foodCardArrayList;

    public FoodTotal() {
    }

    public FoodTotal(ArrayList<FoodCard> foodCardArrayList) {
        this.foodCardArrayList = foodCardArrayList;
    }

    public FoodTotal(String intakeType, ArrayList<FoodCard> foodCardArrayList) {
        this.intakeType = intakeType;
        this.foodCardArrayList = foodCardArrayList;
    }


    protected FoodTotal(Parcel in) {
        intakeType = in.readString();
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

    public static final Creator<FoodTotal> CREATOR = new Creator<FoodTotal>() {
        @Override
        public FoodTotal createFromParcel(Parcel in) {
            return new FoodTotal(in);
        }

        @Override
        public FoodTotal[] newArray(int size) {
            return new FoodTotal[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(intakeType);
    }
}
