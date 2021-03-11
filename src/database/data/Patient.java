package database.data;

import database.AccessSQLite;

import java.util.HashMap;
import java.util.Map;

/**
 * Class of the Patient. Extends AbstractPerson.
 * @author Lucas
 * @version 0.2
 */
public class Patient extends AbstractPerson {

    // Id of the doctor that the patient usually has
    private int doctorID;
    // Doctor object for the doctor that the patient usually has
    private AbstractPerson doctor;
    // NHS Number
    private String nhsNumber;
    // Static maps for ids and string to patients
    private static Map<Integer, Patient> patientIDMap = new HashMap<>();
    private static Map<String, Patient> patientNHSNumberMap = new HashMap<>();

    /**
     * Constructor for Patient objects
     * @param id The id of the patient
     * @param nhsNumber The NHS number of the patient
     * @param fname The first name of the patient
     * @param sname The surname of the patient
     * @param phone The phone number of the patient
     * @param doctorID The ID of the patients usual doctor
     */
    public Patient(int id, String nhsNumber, String fname, String sname, String phone, int doctorID) {
        super(id, fname, sname, phone);
        this.nhsNumber = nhsNumber;
        this.doctorID = doctorID;
        // If the doctor id is null, then it wasnt set in the database.
        this.doctor = (doctorID != 0) ? Doctor.getDoctorFromID(doctorID) : null;
        // Adding patient ids to maps
        patientIDMap.put(id, this);
        patientNHSNumberMap.put(nhsNumber, this);
    }

    /**
     * Refreshes static maps for patients
     */
    public static void resetMap() {
        patientIDMap.clear(); patientNHSNumberMap.clear();
        AccessSQLite accessSQLite = new AccessSQLite();
        accessSQLite.getAllPatients();
    }

    /**
     * Returns a patient based on the passed id.
     * @param id Id of a patient
     * @return Patient object relating to the id or null if there isn't a corresponding patient.
     */
    public static Patient getPatientFromID(int id) {
        return patientIDMap.get(id);
    }

    /**
     * Returns a patient based on the passed NHS number.
     * @param nhs NHS number of the patient
     * @return Patient object relating to the NHS number or null if there isn't a corresponding patient.
     */
    public static Patient getPatientFromNHSNumber(String nhs) {
        return patientNHSNumberMap.get(nhs);
    }

    /**
     * Returns the patients doctor id
     * @return The patients doctor id
     */
    public int getDoctorID() {
        return doctorID;
    }

    /**
     * Sets the patient's doctor id.
     * @param doctorID The new doctor id for the patient
     */
    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    /**
     * Returns the NHS number
     * @return The NHS number
     */
    public String getNhsNumber() {
        return nhsNumber;
    }

    /**
     * Sets the NHS number of the patient
     * @param nhsNumber The new NHS number of the patient
     */
    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    /**
     * Returns the string representation of the patient
     * @return The string representation of the patient
     */
    @Override
    public String toString() {
        return getFullName() + " (" + nhsNumber + ")";
    }

    /**
     * Returns the doctor for the patient
     * @return The doctor for the patient (can be null if not set).
     */
    public AbstractPerson getDoctor() {
        return doctor;
    }

    /**
     * Sets the doctor of the patient
     * @param doctor New doctor for the patient
     */
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    /**
     * Returns all of the patient details in the string.
     * @return All of the patient details in the string.
     */
    public String getAllDetailsString() {
        return  "NHS Number: " + nhsNumber + "\n" +
                "Patient Name: " + getFullName() + "\n" +
                "Patient Phone Number: " + getPhone() + "\n" +
                "Usual Doctor: " + doctor;
    }

}
