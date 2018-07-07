package nodomain.knu2018.bandutils.model.selectdrug;

import android.os.Parcel;
import android.os.Parcelable;

public class SelectDrug implements Parcelable{

    public String name;
    public String age;
    public int photoId;
    public boolean selected;

    public SelectDrug(String name, String age, int photoId, boolean selected) {
        this.name = name;
        this.age = age;
        this.photoId = photoId;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.name);
        dest.writeString(this.age);
        dest.writeInt(this.photoId);
        dest.writeByte(selected ? (byte) 1 : (byte) 0);
    }

    public SelectDrug(Parcel in) {
        this.name = in.readString();
        this.age = in.readString();
        this.photoId = in.readInt();
        this.selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SelectDrug> CREATOR = new Parcelable.Creator<SelectDrug>() {
        public SelectDrug createFromParcel(Parcel source) {
            return new SelectDrug(source);
        }

        public SelectDrug[] newArray(int size) {
            return new SelectDrug[size];
        }
    };
}
