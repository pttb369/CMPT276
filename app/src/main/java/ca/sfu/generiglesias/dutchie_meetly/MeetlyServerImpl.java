package ca.sfu.generiglesias.dutchie_meetly;

import android.util.Log;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MeetlyServerImpl implements MeetlyServer {

    // This is horrible. Don't do this. Seriously.
    // For our purposes, it's just a time saver because we don't care too much
    // about robustness. Using a RESTful API is preferred in practice.
    private static final String url  =
            "jdbc:mysql://csil-messaging1.cs.surrey.sfu.ca/db1";
    private static final String user =
            "cmptuser";
    private static final String pass =
            "sUp3rS3cretp@ssw0rd";

    private java.sql.Connection getConnection()
            throws ClassNotFoundException, java.sql.SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(url, user, pass);
    }

    private final String login =
            "CALL db1.sp_LOGIN(?, ?, ?)";

    @Override
    public int login(String username, String password)
            throws FailedLoginException {
        try {
            Connection con = getConnection();
            CallableStatement loginStmt = con.prepareCall(login);
            loginStmt.setString(1, username);
            loginStmt.setString(2, password);
            loginStmt.registerOutParameter(3, Types.INTEGER);
            loginStmt.executeQuery();
            if (loginStmt.getInt(3) == 0) {
                throw new FailedLoginException("Error logging in");
            }
            return loginStmt.getInt(3);
        } catch (ClassNotFoundException cne) {
            throw new FailedLoginException(cne);
        } catch (java.sql.SQLException sqle) {
            throw new FailedLoginException(sqle);
        }
    }

    private final String create =
            "CALL db1.sp_CREATE_EVENT(?, ?, ?, ?, ?, ?, ?)";

    @Override
    public int publishEvent(String username, int userToken, String title,
                            Calendar startTime, Calendar endTime,
                            double latitude, double longitude)
            throws FailedPublicationException {
        try {
            Connection con = getConnection();
            CallableStatement createStmt = con.prepareCall(create);
            createStmt.setInt(1, userToken);
            createStmt.setString(2, title);
            createStmt.setInt(3, (int) startTime.getTimeInMillis());
            createStmt.setInt(4, (int)endTime.getTimeInMillis());
            createStmt.setDouble(5, latitude);
            createStmt.setDouble(6, longitude);
            createStmt.registerOutParameter(7, Types.INTEGER);
            createStmt.executeQuery();
            if (createStmt.getInt(7) == 0) {
                throw new FailedPublicationException("Error creating event");
            }
            return createStmt.getInt(7);
        } catch (ClassNotFoundException cne) {
            throw new FailedPublicationException(cne);
        } catch (java.sql.SQLException sqle) {
            throw new FailedPublicationException(sqle);
        }
    }

    private final String modify =
            "CALL db1.sp_MODIFY_EVENT(?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public void modifyEvent(int eventID, int userToken, String title,
                            Calendar startTime, Calendar endTime,
                            double latitude, double longitude)
            throws FailedPublicationException {
        try {
            Connection con = getConnection();
            CallableStatement modifyStmt = con.prepareCall(modify);
            modifyStmt.setInt(1, eventID);
            modifyStmt.setInt(2, userToken);
            modifyStmt.setString(3, title);
            modifyStmt.setInt(4, (int) startTime.getTimeInMillis());
            modifyStmt.setInt(5, (int) endTime.getTimeInMillis());
            modifyStmt.setDouble(6, latitude);
            modifyStmt.setDouble(7, longitude);
            modifyStmt.registerOutParameter(8, Types.INTEGER);
            modifyStmt.executeQuery();
            if (modifyStmt.getInt(8) != 1) {
                throw new FailedPublicationException("Error updating event");
            }
        } catch (ClassNotFoundException cne) {
            throw new FailedPublicationException(cne);
        } catch (java.sql.SQLException sqle) {
            throw new FailedPublicationException(sqle);
        }
    }

    private final String fetch =
            "CALL db1.sp_FETCH_EVENTS(?)";

    @Override
    public List<Event> fetchEventsAfter(int lastTick)
            throws FailedFetchException {
        try {
            Connection con = getConnection();
            CallableStatement fetchStmt = con.prepareCall(fetch);
            fetchStmt.setInt(1, lastTick);
            ResultSet results = fetchStmt.executeQuery();

            ArrayList<Event> events = new ArrayList<Event>();
            ResultSetMetaData meta = results.getMetaData();
            int colCount = meta.getColumnCount();
            while (results.next()) {

                MeetlyEvent event = new MeetlyEvent();
                event.eventID    = results.getInt(1);
                event.lastUpdate = results.getInt(2);
                event.title      = results.getString(4);

                event.startTime  = Calendar.getInstance();
                event.startTime.setTimeInMillis(results.getInt(5));
                event.endTime    = Calendar.getInstance();
                event.endTime.setTimeInMillis(results.getInt(6));

                event.latitude   = results.getDouble(7);
                event.longitude  = results.getDouble(8);
                events.add(new Event(event.eventID, event.lastUpdate, event.title, event.startTime,
                        event.endTime, event.latitude, event.longitude));

                /*long eventId    = results.getInt(1);
                int lastUpdate = results.getInt(2);
                String title      = results.getString(4);

                Calendar startTime  = Calendar.getInstance();
                startTime.setTimeInMillis(results.getInt(5));
                Calendar endTime    = Calendar.getInstance();
                endTime.setTimeInMillis(results.getInt(6));

                double latitude   = results.getDouble(7);
                double longitude  = results.getDouble(8);

                events.add(new Event(eventId, lastUpdate, title, startTime, endTime, latitude,
                        longitude));*/
            }
            return events;
        } catch (ClassNotFoundException cne) {
            throw new FailedFetchException(cne);
        } catch (java.sql.SQLException sqle) {
            throw new FailedFetchException(sqle);
        }
    }
}
