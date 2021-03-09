package database.data;

import java.util.HashMap;
import java.util.Map;

public class Doctor extends AbstractPerson {

    private String background;
    private static Map<Integer, Doctor> doctorIDMap = new HashMap<>();
    private static Map<String, Doctor> doctorStringMap = new HashMap<>();

    public Doctor(int id, String fname, String sname, String phone, String background) {
        super(id, fname, sname, phone);
        this.background = background;
        doctorIDMap.put(id, this);
        doctorStringMap.put(toString(), this);
    }

    public static Doctor getDoctorFromID(int id) {
        return doctorIDMap.get(id);
    }

    public static Doctor getDoctorFromString(String s) {
        return doctorStringMap.get(s);
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }


    @Override
    public String toString() {
        return getFullName() +
                ((background.isEmpty()) ? "" : " (" + background + ")");
    }

}
