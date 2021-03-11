package database.data;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Abstract class for people objects. Extended by Doctors and Patients.
 * @author Lucas
 * @version 0.2
 */
public abstract class AbstractPerson {

    // ID for the Person
    private int id;
    // String values for names and phone numbers.
    private String fname, sname, phone;

    /**
     * Protected constructor (can be used by classes extending AbstractPerson)
     * @param id ID of the person
     * @param fname First name for the person
     * @param sname Surname for the person
     * @param phone Phone number for the person
     */
    protected AbstractPerson(int id, String fname, String sname, String phone) {
        this.id = id;
        this.fname = fname;
        this.sname = sname;
        this.phone = phone;
    }

    /**
     * Returns the full name ("FirstName Surname") of the person
     * @return The full name of the person
     */
    public String getFullName() {
        return fname + " " + sname;
    }

    /**
     * Returns the person id.
     * @return The person id.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the person id to the passed id.
     * @param id New ID of the person.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Return the first name of the person
     * @return The first name of the person
     */
    public String getFname() {
        return fname;
    }

    /**
     * Set the person's first name
     * @param fname The person's first name
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * Returns the person's surname
     * @return The person's surname
     */
    public String getSname() {
        return sname;
    }

    /**
     * Sets the surname of the person
     * @param sname Surname of the person
     */
    public void setSname(String sname) {
        this.sname = sname;
    }

    /**
     * Returns the phone number of the person
     * @return The phone number of the person
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone of the person
     * @param phone Phone number for the person
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns a string representing the person.
     * @return A string representing the person.
     */
    @Override
    public abstract String toString();

    /**
     * Converts ArrayList of AbstractPerson object to an array of strings
     * @param people ArrayList containing all of the people.
     * @return String array containing the strings representing the people from the passed people ArrayList
     */
    public static String[] toArray(ArrayList<AbstractPerson> people) {
        // Sorts alphabetically
        people.sort(Comparator.comparing(AbstractPerson::toString));
        // Converts to string array
        String[] result = new String[people.size()];
        for (int i = 0; i < people.size(); i++) {
            result[i] = people.get(i).toString();
        }
        return result;
    }

}
