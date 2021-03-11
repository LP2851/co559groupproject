package database.data;

import database.AccessSQLite;
import database.datetime.DateTimeHandler;

import java.text.ParseException;
import java.util.*;

public class Booking {

    private AbstractPerson patient, doctor;
    private int id;
    private DateTimeHandler startDateTime;
    private DateTimeHandler endDateTime;
    private static Map<String, List<Booking>> personBookingsMap = new HashMap<>();

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



    public Booking(int id, Date startDateTime, Date endDateTime, Patient patient, Doctor doctor) {
        this.id = id;
        this.startDateTime = new DateTimeHandler(startDateTime);
        this.endDateTime = new DateTimeHandler(endDateTime);
        this.patient = patient;
        this.doctor = doctor;
    }

    public Booking(DateTimeHandler startDateTime, DateTimeHandler endDateTime, Patient patient, Doctor doctor) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.patient = patient;
        this.doctor = doctor;
    }

    private void addToMap() {
        if(personBookingsMap.containsKey("p" + patient.getId())) {
            personBookingsMap.get("p" + patient.getId()).add(this);
        } else {
            ArrayList<Booking> personsBookings = new ArrayList<>();
            personsBookings.add(this);
            personBookingsMap.put("p" + patient.getId(), personsBookings);
        }

        if(personBookingsMap.containsKey("d" + doctor.getId())) {
            personBookingsMap.get("d" + doctor.getId()).add(this);
        } else {
            ArrayList<Booking> personsBookings = new ArrayList<>();
            personsBookings.add(this);
            personBookingsMap.put("d" + doctor.getId(), personsBookings);
        }
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

    @Override
    public String toString() {
        return  "Patient: " + getPatient() + "\n" +
                "Doctor: " + getDoctor() + "\n" +
                "Start: " + getStartDateTime().toString() + "\n" +
                "End: " + getEndDateTime().toString();
    }

    public static AuthAnswer authenticateBooking(Booking b) {
        if(hasDoctorClash(b)) {
            //personBookingsMap.get(b.getPatient()).remove(b);
            return AuthAnswer.DOCTOR_CLASH;
        } else if (hasPatientClash(b)) {
            //personBookingsMap.get(b.getPatient()).remove(b);
            return AuthAnswer.PATIENT_CLASH;
        }
        //personBookingsMap.get(b.getPatient()).remove(b);
        return AuthAnswer.AUTHORISED;
    }

    private static boolean hasDoctorClash(Booking booking) {
        if (personBookingsMap.get("d" + booking.getDoctor().getId()) == null) {
            return false;
        }
        for (Booking b : personBookingsMap.get("d" + booking.getDoctor().getId())) {
            if (hasClash(booking, b))
                return true;
        }
        return false;
    }

    private static boolean hasPatientClash(Booking booking) {
        if (personBookingsMap.get("p" + booking.getPatient().getId()) == null) {
            return false;
        }
        for (Booking b : personBookingsMap.get("p" + booking.getPatient().getId())) {
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
                ((tryDateStart.before(alreadyDateStart) && tryDateEnd.before(alreadyDateStart)) &&
                !(tryDateStart.after(alreadyDateEnd) && tryDateEnd.after(alreadyDateEnd))) ||

                !((tryDateStart.before(alreadyDateStart) && tryDateEnd.before(alreadyDateStart)) &&
                (tryDateStart.after(alreadyDateEnd) && tryDateEnd.after(alreadyDateEnd)))
                );
    }

    public static void resetMap() {
        personBookingsMap.clear();
        AccessSQLite accessSQLite = new AccessSQLite();
        accessSQLite.getAllBookings();
    }

    public enum AuthAnswer {
        AUTHORISED,
        DOCTOR_CLASH,
        PATIENT_CLASH,
    }

}
