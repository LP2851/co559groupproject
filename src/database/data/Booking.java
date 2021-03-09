package database.data;

import database.datetime.DateTimeHandler;

import java.text.ParseException;
import java.util.*;

public class Booking {

    private AbstractPerson patient, doctor;
    private int id;
    private DateTimeHandler startDateTime;
    private DateTimeHandler endDateTime;
    private static Map<AbstractPerson, List<Booking>> personBookingsMap = new HashMap<>();

    public Booking(int id, String startDateTime, String endDateTime, int patient, int doctor) {
        this.id = id;
        try {
            this.startDateTime = new DateTimeHandler(startDateTime);
            this.endDateTime = new DateTimeHandler(endDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.patient = Patient.getPatientFromID(patient);
        this.doctor = Doctor.getDoctorFromID(doctor);
        addToMap();
    }

    private void addToMap() {
        if(personBookingsMap.containsKey(patient)) {
            personBookingsMap.get(patient).add(this);
        } else {
            ArrayList<Booking> personsBookings = new ArrayList<>();
            personsBookings.add(this);
            personBookingsMap.put(patient, personsBookings);
        }

        if(personBookingsMap.containsKey(doctor)) {
            personBookingsMap.get(doctor).add(this);
        } else {
            ArrayList<Booking> personsBookings = new ArrayList<>();
            personsBookings.add(this);
            personBookingsMap.put(patient, personsBookings);
        }
    }

    public Booking(int id, Date startDateTime, Date endDateTime, Patient patient, Doctor doctor) {
        this.id = id;
        this.startDateTime = new DateTimeHandler(startDateTime);
        this.endDateTime = new DateTimeHandler(endDateTime);
        this.patient = patient;
        this.doctor = doctor;
    }

    public AbstractPerson getPatient() {
        return patient;
    }

    public AbstractPerson getDoctor() {
        return doctor;
    }

    public int getId() {
        return id;
    }

    public DateTimeHandler getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(DateTimeHandler startDateTime) {
        this.startDateTime = startDateTime;
    }

    public DateTimeHandler getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(DateTimeHandler endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Date getStartDateTimeAsDate() {
        return startDateTime.getDate();
    }

    public Date getEndDateTimeAsDate() {
        return endDateTime.getDate();
    }

    public static AuthAnswer authenticateBooking(Booking b) {
        if(hasDoctorClash(b)) {
            return AuthAnswer.DOCTOR_CLASH;
        } else if (hasPatientClash(b)) {
            return AuthAnswer.PATIENT_CLASH;
        }
        return AuthAnswer.AUTHORISED;
    }

    private static boolean hasDoctorClash(Booking booking) {

        for (Booking b : personBookingsMap.get(booking.getDoctor())) {
            if (hasClash(booking, b))
                return true;
        }
        return false;
    }

    private static boolean hasPatientClash(Booking booking) {
        for (Booking b : personBookingsMap.get(booking.getPatient())) {
            if (hasClash(booking, b))
                return true;
        }
        return false;
    }

    private static boolean hasClash(Booking tryBooking, Booking alreadyBooking) {
        /*
            TRUE if
                Starts and ends before
                Starts and end after
            FALSE if
                Starts before a booking but ends during a booking returns
                Starts and ends during booking
                Starts before end of booking and ends after booking
         */
        Date tryDateStart = tryBooking.getStartDateTimeAsDate();
        Date tryDateEnd = tryBooking.getEndDateTimeAsDate();
        Date alreadyDateStart = alreadyBooking.getStartDateTimeAsDate();
        Date alreadyDateEnd = alreadyBooking.getEndDateTimeAsDate();
        return (
                (tryDateStart.before(alreadyDateStart) && tryDateEnd.before(alreadyDateStart)) ||
                (tryDateStart.after(alreadyDateEnd) && tryDateEnd.after(alreadyDateEnd))
                );
    }

    public enum AuthAnswer {
        AUTHORISED,
        DOCTOR_CLASH,
        PATIENT_CLASH,
    }

}
