package database.data;

import database.AccessSQLite;

import java.util.HashMap;
import java.util.Map;

public class Patient extends AbstractPerson {

    private int doctorID;
    private AbstractPerson doctor;
    private String nhsNumber;
    private static Map<Integer, Patient> patientIDMap = new HashMap<>();
    private static Map<String, Patient> patientNHSNumberMap = new HashMap<>();

    public Patient(int id, String nhsNumber, String fname, String sname, String phone, int doctorID) {
        super(id, fname, sname, phone);
        this.nhsNumber = nhsNumber;
        this.doctorID = doctorID;
        this.doctor = (doctorID != 0) ? Doctor.getDoctorFromID(doctorID) : null;
        patientIDMap.put(id, this);
        patientNHSNumberMap.put(nhsNumber, this);
    }

    public static void resetMap() {
        patientIDMap.clear(); patientNHSNumberMap.clear();
        AccessSQLite accessSQLite = new AccessSQLite();
        accessSQLite.getAllPatients();
    }

    public static Patient getPatientFromID(int id) {
        return patientIDMap.get(id);
    }

    public static Patient getPatientFromNHSNumber(String nhs) {
        return patientNHSNumberMap.get(nhs);
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    @Override
    public String toString() {
        return getFullName() + " (" + nhsNumber + ")";
    }

    public AbstractPerson getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getAllDetailsString() {
        return  "NHS Number: " + nhsNumber + "\n" +
                "Patient Name: " + getFullName() + "\n" +
                "Patient Phone Number: " + getPhone() + "\n" +
                "Usual Doctor: " + doctor;
    }

}
