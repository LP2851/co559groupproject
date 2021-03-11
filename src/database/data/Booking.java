package database.data;

import database.AccessSQLite;
import database.datetime.DateTimeHandler;

import java.text.ParseException;
import java.util.*;

/**
 * Class for handling booking objects.
 * @author Lucas
 * @version 0.2
 */
public class Booking {

    // The patient and doctor objects for the booking
    private AbstractPerson patient, doctor;
    // The ID of the booking
    private int id;
    // Start and end dates of the bookings stored in handlers
    private DateTimeHandler startDateTime;
    private DateTimeHandler endDateTime;
    // Static map linking doctors and patients to their bookings.
    private static Map<String, List<Booking>> personBookingsMap = new HashMap<>();

    /**
     * Constructor for a Booking
     * @param id The id of the booking
     * @param startDateTime String containing the start date and time for the booking
     * @param endDateTime String containing the end date and time for the booking
     * @param patient The id of the patient
     * @param doctor The id of the doctor
     */
    public Booking(int id, String startDateTime, String endDateTime, int patient, int doctor) {
        this.id = id;
        // Converts strings for dates into DateTimeHandlers
        try {
            this.startDateTime = new DateTimeHandler(startDateTime);
            this.endDateTime = new DateTimeHandler(endDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Getting the patient/doctor from the ids.
        this.patient = Patient.getPatientFromID(patient);
        this.doctor = Doctor.getDoctorFromID(doctor);
        // Adding booking to static map
        addToMap();
    }

    /**
     * Constructor for a Booking
     * @param id The id of the booking
     * @param startDateTime Date object for the starting date and time of the booking
     * @param endDateTime Date object for the ending date and time of the booking
     * @param patient Patient object for the patient for booking
     * @param doctor Doctor object for the doctor for booking
     */
    public Booking(int id, Date startDateTime, Date endDateTime, Patient patient, Doctor doctor) {
        this.id = id;
        // Creating DateTimeHandler from Date objects.
        this.startDateTime = new DateTimeHandler(startDateTime);
        this.endDateTime = new DateTimeHandler(endDateTime);
        this.patient = patient;
        this.doctor = doctor;
    }

    /**
     * Constructor for a Booking
     * @param startDateTime DateTimeHandler for the starting date and time of the booking
     * @param endDateTime DateTimeHandler for the ending date and time of the booking
     * @param patient Patient object for the patient for booking
     * @param doctor Doctor object for the doctor for booking
     */
    public Booking(DateTimeHandler startDateTime, DateTimeHandler endDateTime, Patient patient, Doctor doctor) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.patient = patient;
        this.doctor = doctor;
    }

    /**
     * Adding the booking to the booking map for both the patient and the doctor.
     */
    private void addToMap() {
        // Adds to the patient's map entry
        if(personBookingsMap.containsKey("p" + patient.getId())) {
            personBookingsMap.get("p" + patient.getId()).add(this);
        } else {
            ArrayList<Booking> personsBookings = new ArrayList<>();
            personsBookings.add(this);
            personBookingsMap.put("p" + patient.getId(), personsBookings);
        }
        // Adds to the doctor's map entry
        if(personBookingsMap.containsKey("d" + doctor.getId())) {
            personBookingsMap.get("d" + doctor.getId()).add(this);
        } else {
            ArrayList<Booking> personsBookings = new ArrayList<>();
            personsBookings.add(this);
            personBookingsMap.put("d" + doctor.getId(), personsBookings);
        }
    }

    /**
     * Returns the patient object for the booking
     * @return The patient object for the booking
     */
    public AbstractPerson getPatient() {
        return patient;
    }

    /**
     * Returns the doctor object for the booking
     * @return The doctor object for the booking
     */
    public AbstractPerson getDoctor() {
        return doctor;
    }

    /**
     * Returns the id of the booking
     * @return The id of the booking
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the DateTimeHandler for the booking's starting date/time
     * @return The DateTimeHandler for the booking's starting date/time
     */
    public DateTimeHandler getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets the start date/time DateTimeHandler
     * @param startDateTime Sets the start date/time DateTimeHandler
     */
    public void setStartDateTime(DateTimeHandler startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Returns the DateTimeHandler for the booking's ending date/time
     * @return The DateTimeHandler for the booking's ending date/time
     */
    public DateTimeHandler getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets the end date/time DateTimeHandler
     * @param endDateTime Sets the end date/time DateTimeHandler
     */
    public void setEndDateTime(DateTimeHandler endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Returns the starting date/time as a Date.
     * @return The starting date/time as a Date.
     */
    public Date getStartDateTimeAsDate() {
        return startDateTime.getDate();
    }

    /**
     * Returns the ending date/time as a Date
     * @return The ending date/time as a Date
     */
    public Date getEndDateTimeAsDate() {
        return endDateTime.getDate();
    }

    /**
     * Returns a string representation of the booking
     * @return A string representation of the booking
     */
    @Override
    public String toString() {
        return  "Patient: " + getPatient() + "\n" +
                "Doctor: " + getDoctor() + "\n" +
                "Start: " + getStartDateTime().toString() + "\n" +
                "End: " + getEndDateTime().toString();
    }

    /**
     * Returns AUTHORISED if the booking has no clashes
     * If there was a clash with the doctor's other bookings then returns DOCTOR_CLASH
     * If there was a clash with the patient's other bookings then return PATIENT_CLASH
     * @param b The booking to check other bookings against
     * @return Either AUTHORISED, DOCTOR_CLASH, PATIENT_CLASH
     */
    public static AuthAnswer authenticateBooking(Booking b) {
        // Is there a clash with the doctor's current bookings
        if(hasDoctorClash(b)) {
            return AuthAnswer.DOCTOR_CLASH;
        // Is there a clash with the patient's current bookings
        } else if (hasPatientClash(b)) {
            return AuthAnswer.PATIENT_CLASH;
        }
        // If AUTHORISED
        return AuthAnswer.AUTHORISED;
    }

    /**
     * Returns true if there is a booking clash between the new booking and the current doctor's bookings.
     * Otherwise returns false if there is no clash.
     * @param booking Possible new booking
     * @return Returns if there is a booking clash between the new booking and the current doctor's bookings.
     */
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

    /**
     * Returns true if there is a booking clash between the new booking and the current patient's bookings.
     * Otherwise returns false if there is no clash.
     * @param booking Possible new booking
     * @return Returns if there is a booking clash between the new booking and the current patient's bookings.
     */
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

    /**
     * Returns true if the booking clashes with the other booking
     * @param tryBooking The new booking
     * @param alreadyBooking The booking that is already in the system
     * @return
     */
    private static boolean hasClash(Booking tryBooking, Booking alreadyBooking) {
        /*
            TRUE if
                Starts and ends before OR
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

    /**
     * Refreshes the static bookings map
     */
    public static void resetMap() {
        personBookingsMap.clear();
        AccessSQLite accessSQLite = new AccessSQLite();
        accessSQLite.getAllBookings();
    }

    /**
     * Enum for the authorisation answers.
     */
    public enum AuthAnswer {
        AUTHORISED,
        DOCTOR_CLASH,
        PATIENT_CLASH,
    }

}
