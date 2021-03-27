package database.data;

import database.AccessSQLite;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for the doctor objects
 * @author Lucas
 * @version 0.2
 */
public class Doctor extends AbstractPerson {

    // Background string for the doctor
    private String background;
    // Maps for the doctors to the strings and ID.
    private static Map<Integer, Doctor> doctorIDMap = new HashMap<>();
    private static Map<String, Doctor> doctorStringMap = new HashMap<>();

    /**
     * Constructor for the Doctor class
     * @param id The id of the doctor
     * @param fname The first name of the doctor
     * @param sname The surname of the doctor
     * @param phone The phone number of the doctor
     * @param background The background of the doctor
     */
    public Doctor(int id, String fname, String sname, String phone, String background) {
        super(id, fname, sname, phone);
        this.background = background;
        // Adding to maps
        doctorIDMap.put(id, this);
        doctorStringMap.put(toString(), this);
    }

    /**
     * Refreshes the static doctors maps
     */
    public static void resetMap() {
        doctorStringMap.clear(); doctorIDMap.clear();
        AccessSQLite accessSQLite = new AccessSQLite();
        accessSQLite.getAllDoctors();
    }

    /**
     * Gets the doctor object from an id.
     * @param id ID of the doctor
     * @return Doctor object if the id is valid, otherwise will return null
     */
    public static Doctor getDoctorFromID(int id) {
        return doctorIDMap.get(id);
    }

    /**
     * Gets the doctor object from a string
     * @param s String of the doctor
     * @return Doctor object if the id is valid, otherwise will return null
     */
    public static Doctor getDoctorFromString(String s) {
        return doctorStringMap.get(s);
    }

    /**
     * Returns the background for the doctor
     * @return The background for the doctor
     */
    public String getBackground() {
        return background;
    }

    /**
     * Sets the background value for the doctor
     * @param background New background for doctor
     */
    public void setBackground(String background) {
        this.background = background;
    }

    /**
     * Returns a string representation of the doctor
     * @return A string representation of the doctor
     */
    @Override
    public String toString() {
        return getFullName() +
                ((background.isEmpty()) ? "" : " (" + background + ")");
    }

    public static Doctor[] getAllDoctors() {
        return doctorIDMap.values().toArray(new Doctor[0]);
    }
}
