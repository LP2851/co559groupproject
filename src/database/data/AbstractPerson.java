package database.data;

import java.util.ArrayList;
import java.util.Comparator;

public abstract class AbstractPerson {

    private int id;
    private String fname, sname, phone;

    protected AbstractPerson(int id, String fname, String sname, String phone) {
        this.id = id;
        this.fname = fname;
        this.sname = sname;
        this.phone = phone;
    }

    public String getFullName() {
        return fname + " " + sname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public abstract String toString();

    public static String[] toArray(ArrayList<AbstractPerson> people) {
        people.sort(Comparator.comparing(AbstractPerson::toString));
        String[] result = new String[people.size()];

        for (int i = 0; i < people.size(); i++) {
            result[i] = people.get(i).toString();
        }
        return result;
    }

}
