package nodomain.knu2018.bandutils.model;

import java.util.ArrayList;

public class UserInfo {

    ArrayList<Users> response;

    public UserInfo() {
    }

    public UserInfo(ArrayList<Users> response) {
        this.response = response;
    }

    public ArrayList<Users> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<Users> response) {
        this.response = response;
    }

    public class Users{

        String userName;
        String userUUID;
        String phone;
        String gender;
        String age;
        String ageGroup;
        String birthday;

        public Users() {
        }

        public Users(String userName, String userUUID, String phone, String gender, String age, String ageGroup, String birthday) {
            this.userName = userName;
            this.userUUID = userUUID;
            this.phone = phone;
            this.gender = gender;
            this.age = age;
            this.ageGroup = ageGroup;
            this.birthday = birthday;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserUUID() {
            return userUUID;
        }

        public void setUserUUID(String userUUID) {
            this.userUUID = userUUID;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getAgeGroup() {
            return ageGroup;
        }

        public void setAgeGroup(String ageGroup) {
            this.ageGroup = ageGroup;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }
    }



}
