package database.data;

import java.util.HashMap;
import java.util.Map;

public class Patient extends AbstractPerson {

    private int doctorID;
    private AbstractPerson doctor;
    private String nhsNumber;
    public static Map<Integer, Patient> patientIDMap = new HashMap<>();

    public Patient(int id, String nhsNumber, String fname, String sname, String phone, int doctorID) {
        super(id, fname, sname, phone);
        this.nhsNumber = nhsNumber;
        this.doctorID = doctorID;
        patientIDMap.put(id, this);
        this.doctor = Doctor.getDoctorFromID(doctorID);
    }

    public static Patient getPatientFromID(int id) {
        return patientIDMap.get(id);
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
}
