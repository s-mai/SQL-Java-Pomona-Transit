import java.sql.*;
import java.util.Scanner;

public class PomonaTransit {

	public static void main(String[] args) {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		}
		catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace(System.err);
		}
		Connection con = null;
        Statement stmt = null;
        
        PreparedStatement pstmt;
        PreparedStatement pstmt2;
        
        int choice = 0;

        try {
            con = DriverManager.getConnection("jdbc:odbc:Profile");
            stmt = con.createStatement();
    		pstmt = con.prepareStatement("INSERT INTO TripOffering(tripnumber,date,scheduledstarttime,scheduledarrivaltime,drivername, busid)"
   				 					   + " VALUE (?,?,?,?,?)");
    		pstmt2 = con.prepareStatement("INSERT INTO Bus(busid,model,year) VALUE (?,?,?)");
            createTable(stmt);
            while (choice != 9) {
                System.out.println("Enter a number: ");
                System.out.println("1. Display the schedule of all trips for a given StartLocationName and Destination Name, and Date");
                System.out.println("2. Edit the schedule");
                System.out.println("3. Display the stops of a trip");
                System.out.println("4. Display the weekly schedule of a driver");
                System.out.println("5. Add a driver");
                System.out.println("6. Add a bus");
                System.out.println("7. Delete a bus");
                System.out.println("8. Record the actual data of a trip");
                System.out.println("9. Quit");
                Scanner kb = new Scanner(System.in);
                choice = kb.nextInt();
                if (choice == 1) {
                	showRecords(stmt);
                }
                else if (choice == 2) {
                	updateRecord(stmt, pstmt);
                }
                else if (choice == 3) {
                	showStops(stmt);
                }
                else if (choice == 4) {
                	displaySchedule(stmt);
                }
                else if (choice == 5) {
                	addDrive(stmt);
                }
                else if (choice == 6) {
                	addBus(stmt, pstmt2);
                }
                else if (choice == 7) {
                	deleteBus(stmt);
                }
                else if (choice == 8) {
                	recordData(stmt);
                }
            }
            pstmt.close();
            pstmt2.close();
            stmt.close();
            con.close();
        }
        catch(SQLException sqle){
            sqle.printStackTrace(System.err);
        }
	}

	public static void createTable(Statement stmt) throws SQLException {
	        String createTrip  = "CREATE TABLE Trip " +
	                             "( tripnumber VARCHAR(25) NOT NULL,  " +
	                             "  startlocationname VARCHAR(50) NOT NULL,  " +
	                             "  destinationname VARCHAR(50) NOT NULL " +
	                             ")";
	        String createTripOffering = "CREATE TABLE TripOffering " + 
	                             		"( tripnumber VARCHAR(25) NOT NULL,  " +
	                             		"  date DATE NOT NULL,  " +
	                             		"  scheduledstarttime VARCHAR(5) NOT NULL, " +
	                             		"  scheduledarrivaltime VARCHAR(5) NOT NULL, " + 
	                             		"  drivername VARCHAR(25) NOT NULL, " + 
	                             		"  busid VARCHAR(10) NOT NULL " +
	                             		")";
	        String createBus =  "CREATE TABLE Bus " + 
             					"( busid VARCHAR(25) NOT NULL,  " +
             					"  model VARCHAR(25) NOT NULL,  " +
             					"  year CHAR(4) NOT NULL " +
             					")";
	        String createDriver =   "CREATE TABLE Driver " + 
 									"( drivername VARCHAR(25) NOT NULL,  " +
 									"  drivertelephonenumber VARCHAR(10) NOT NULL  " +
 									")";
	        String createStop = "CREATE TABLE Stop " + 
								"( stopnumber VARCHAR(25) NOT NULL,  " +
								"  stopaddress VARCHAR(25) NOT NULL  " +
								")";
	        String createActualTripStopInfo =   "CREATE TABLE ActualTripStopInfo " + 
             									"( tripnumber VARCHAR(25) NOT NULL,  " +
             									"  date DATE NOT NULL,  " +
             									"  scheduledstarttime VARCHAR(5) NOT NULL, " +
             									"  stopnumber VARCHAR(25) NOT NULL, " + 
             									"  scheduledarrivaltime VARCHAR(5) NOT NULL, " + 
             									"  actualstarttime VARCHAR(5) NOT NULL, " + 
             									"  actualarrivaltime VARCHAR(5) NOT NULL, " +
             									"  numberofpassengerin VARCHAR(3) NOT NULL, " + 
             									"  numberofpassengerout VARCHAR(3) NOT NULL " +
             									")";
	        String createTripStopInfo = "CREATE TABLE TripStopInfo " +
                    					"( tripnumber VARCHAR(25) NOT NULL,  " +
                    					"  stopnumber VARCHAR(25) NOT NULL,  " +
                    					"  sequencenumber VARCHAR(25) NOT NULL, " +
                    					"  drivingtime VARCHAR(20) NOT NULL " +
                    					")";

	            
	       stmt.execute(createTrip);
	       stmt.execute(createTripOffering);
	       stmt.execute(createBus);
	       stmt.execute(createDriver);
	       stmt.execute(createStop);
	       stmt.execute(createActualTripStopInfo);
	       stmt.execute(createTripStopInfo);

	       stmt.execute("INSERT INTO Trip VALUES "+
	                    "('123', 'Rosemead', 'Pomona')");
	       stmt.execute("INSERT INTO Trip VALUES "+
                   		"('456', 'Pomona', 'Rosemead')");
	       stmt.execute("INSERT INTO Trip VALUES "+
                   		"('789', 'Fullerton', 'Brea')");
	       stmt.execute("INSERT INTO Trip VALUES "+
                   		"('111', 'Brea', 'Fullerton')");
	       
	       stmt.execute("INSERT INTO TripOffering VALUES "+
                   		"('123', '06/2/17', '0200', '0300', 'Steven Mai', '001')");
	       stmt.execute("INSERT INTO TripOffering VALUES "+
	    		   		"('456', '06/2/17', '0300', '0400', 'Sam Li', '002')");
	       stmt.execute("INSERT INTO TripOffering VALUES "+
	    		   		"('789', '06/4/17', '0500', '0600', 'Rando Person 1', '003')");
	       stmt.execute("INSERT INTO TripOffering VALUES "+
	    		   		"('111', '06/4/17', '0600', '0700', 'Rando Person 2', '004')");
	       
	       stmt.execute("INSERT INTO Bus VALUES "+
                   		"('001', 'Mercedes', '2016')");
	       stmt.execute("INSERT INTO Bus VALUES "+
              			"('002', 'Mercedes', '2016')");
	       stmt.execute("INSERT INTO Bus VALUES "+
              			"('003', 'Solares', '2017')");
	       stmt.execute("INSERT INTO Bus VALUES "+
              			"('004', 'Solares', '2017')");
	       
	       stmt.execute("INSERT INTO Driver VALUES "+
              			"('Kevin Nguyen', '6260101234')");
	       stmt.execute("INSERT INTO Driver VALUES "+
         				"('Josh Mcguiness', '6260151235')");
	       stmt.execute("INSERT INTO Driver VALUES "+
         				"('Dwayne Rock', '4323451234')");
	       stmt.execute("INSERT INTO Driver VALUES "+
         				"('A-Aron Junior', '1234567896')");
	       
	       stmt.execute("INSERT INTO Stop VALUES "+
         				"('01', 'Rosemead')");
	       stmt.execute("INSERT INTO Stop VALUES "+
    					"('02', 'Pomona')");
	       stmt.execute("INSERT INTO Stop VALUES "+
    					"('03', 'Fullerton')");
	       stmt.execute("INSERT INTO Stop VALUES "+
    					"('04', 'Brea')");
	       
	       stmt.execute("INSERT INTO ActualTripStopInfo VALUES " + 
                                                "('123', '06/2/17', '0200', '01', '0300', '0205', '0300', '10', '10')");
	       stmt.execute("INSERT INTO ActualTripStopInfo VALUES " + 
   		   				"('456', '06/2/17', '0300', '02', '0400', '0305', '0400', '20', '20')");
	       stmt.execute("INSERT INTO ActualTripStopInfo VALUES " + 
   		   				"('789', '06/4/17', '0500', '03', '0600', '0505', '0600', '10', '10')");
	       stmt.execute("INSERT INTO ActualTripStopInfo VALUES " + 
   		   				"('111', '06/4/17', '0600', '04', '0700', '0605', '0700', '20', '20')");
	       
	       stmt.execute("INSERT INTO TripStopInfo VALUES " + 
                                                "('123', '01', '010', '1 hour')");
	       stmt.execute("INSERT INTO TripStopInfo VALUES " + 
   		   				"('456', '02', '011', '1 hour')");
	       stmt.execute("INSERT INTO TripStopInfo VALUES " + 
   		   				"('789', '03', '100', '1 hour')");
	       stmt.execute("INSERT INTO TripStopInfo VALUES " + 
   		   				"('111', '04', '101', '1 hour')");
	}
	
	public static void showRecords(Statement stmt) throws SQLException {
		String startLocationName;
		String destinationName;
		String date;
		String varSQL;
		Scanner kb = new Scanner(System.in);
		
		System.out.println("Enter a starting location: ");
		startLocationName = kb.nextLine();
		System.out.println("Enter a destination name: ");
		destinationName = kb.nextLine();
		System.out.println("Enter a date: ");
		date = kb.nextLine();
		
		varSQL = "SELECT T.StartLocationName, T.DestinationName, TR.Date, TR.StartTime, TR.ScheduledArrivalTime, TR.DriverName, TR.BusID " +
				 "FROM Trip T, TripOffering TR " +
				 "WHERE T.StartLocationName = '" + startLocationName + "' AND " +
				 	   "T.DestinationName = '" + destinationName + "' AND " +
				 	   "TR.Date = '" + date + "' AND " + 
				 	   "T.TripNumber = TR.TripNumber";
		
		ResultSet rs = stmt.executeQuery(varSQL);
		ResultSetMetaData rsMeta = rs.getMetaData();
		int varColCount = rsMeta.getColumnCount();
		for (int col = 1; col <= varColCount; col++) {
			System.out.println(rsMeta.getColumnName(col) + "\t");
		}
		while (rs.next()) {
			for (int col = 1; col <= varColCount; col++) {
				System.out.println(rs.getString(col) + "\t");
			}
			System.out.println();
		}
	    rs.close();
	}

	public static void updateRecord(Statement stmt, PreparedStatement pstmt) throws SQLException {
            	Scanner kb = new Scanner(System.in);
		int option;
		String tripNumber;
		String date;
		String scheduledStartTime;
		String scheduledArrivalTime;
		String name;
		String id;
                
		System.out.println("Choose an option:");
		System.out.println("1. Delete a trip");
		System.out.println("2. Add a trip");
		System.out.println("3. Change the driver for a trip");
		System.out.println("4. Change the bus for a trip");
		option = kb.nextInt();
		
		if (option == 1) {                
			System.out.println("Enter a trip number: ");
			tripNumber = kb.nextLine();
			System.out.println("Enter a date: ");
			date = kb.nextLine();
			System.out.println("Enter a start time: ");
			scheduledStartTime = kb.nextLine();
			stmt.executeUpdate("DELETE TripOffering WHERE tripnumber = " + tripNumber + " AND " +
							   " date = " + date + " AND " + 
							   " scheduledstarttime = " + scheduledStartTime);
		}
		else if (option == 2) {
			String choice = "Y";
			while (choice == "Y") {
				System.out.println("Enter a trip number: ");
				tripNumber = kb.nextLine();
				System.out.println("Enter a date: ");
				date = kb.nextLine();
				System.out.println("Enter a scheduled start time: ");
				scheduledStartTime = kb.nextLine();
				System.out.println("Enter a scheduled arrival time:");
				scheduledArrivalTime = kb.nextLine();
				System.out.println("Enter a driver name:");
				name = kb.nextLine();
				System.out.println("Enter a bus ID:");
				id = kb.nextLine();
                                
				pstmt.setString(1, tripNumber);
				pstmt.setString(2, date);
				pstmt.setString(3, scheduledStartTime);
				pstmt.setString(4, scheduledArrivalTime);
				pstmt.setString(5, name);
				pstmt.setString(6, id);
				pstmt.executeUpdate();
                                
				System.out.println("Continue adding? (Y/N)");
				choice = kb.next();
			}
		}
		else if (option == 3) {
			System.out.println("Enter a trip number: ");
			tripNumber = kb.nextLine();
			System.out.println("Enter a date: ");
			date = kb.nextLine();
			System.out.println("Enter a start time: ");
			scheduledStartTime = kb.nextLine();
			System.out.println("Enter a new driver name" );
			name = kb.nextLine();
			stmt.executeUpdate("UPDATE TripOffering " +
							   "SET drivername = " + name + 
							   "WHERE tripnumber = " + tripNumber + " AND " +
							   "date = " + date + " AND " + 
							   "starttime = " + scheduledStartTime);
		}
		else if (option == 4) {
			System.out.println("Enter a trip number: ");
			tripNumber = kb.nextLine();
			System.out.println("Enter a date: ");
			date = kb.nextLine();
			System.out.println("Enter a start time: ");
			scheduledStartTime = kb.nextLine();
			System.out.println("Enter a new bus ID: " );
			id = kb.nextLine();
			stmt.executeUpdate("UPDATE TripOffering " +
					   		   "SET busid = " + id + 
					   		   "WHERE tripnumber = " + tripNumber + " AND " +
					   		   "date = " + date + " AND " + 
					   		   "starttime = " + scheduledStartTime);
		}
		else {
			System.out.println("Enter a valid option!");
			updateRecord(stmt, pstmt);
		}
	}

	public static void showStops(Statement stmt) throws SQLException {
        Scanner kb = new Scanner(System.in);
		String tripNumber;
		String varSQL;

                
		System.out.println("Enter a trip number to display the trip stops:");
		tripNumber = kb.nextLine();
		varSQL = "SELECT T.TripNumber, T.StopNumber, T.SequenceNumber, T.DrivingTime " +
				 "FROM TripStopInfo T " +
				 "WHERE T.TripNumber = '" + tripNumber + "'";
                
		ResultSet rs = stmt.executeQuery(varSQL);
		ResultSetMetaData rsMeta = rs.getMetaData();
		int varColCount = rsMeta.getColumnCount();
                
		for (int col = 1; col <= varColCount; col++) {
			System.out.println(rsMeta.getColumnName(col) + "\t");
		}
		while (rs.next()) {
			for (int col = 1; col <= varColCount; col++) {
				System.out.println(rs.getString(col) + "\t");
			}
			System.out.println();
		}
	    rs.close();
	}

	public static void displaySchedule(Statement stmt) {
            	Scanner kb = new Scanner(System.in);
		String driver;
		String date;
		String varSQL;
                
		System.out.println("Enter a driver name: ");
		driver = kb.nextLine();
		System.out.println("Enter a date: " );
		date = kb.nextLine();
		System.out.println("Can't display schedule.");
	}
	
	public static void addDrive(Statement stmt) throws SQLException {
    		Scanner kb = new Scanner(System.in);        
		String name;
		String telephone;
                
		System.out.println("Enter a driver's name: ");
		name = kb.nextLine();
		System.out.println("Enter the driver's phone number: ");
		telephone = kb.nextLine();
		stmt.execute("INSERT INTO Driver VALUES " + 
					 name + ", " + telephone + ")");
	}
	
	public static void addBus(Statement stmt, PreparedStatement pstmt2) throws SQLException {
		Scanner kb = new Scanner(System.in);            
		String busID;
		String model;
		String year;

		System.out.println("Enter a bus ID: ");
		busID = kb.nextLine();
		System.out.println("Enter a model: ");
		model = kb.nextLine();
		System.out.println("Enter a year: ");
		year = kb.nextLine();
		pstmt2.setString(1, busID);
		pstmt2.setString(2, model);
		pstmt2.setString(3, year);
	}
	
	public static void deleteBus(Statement stmt) throws SQLException {
            	Scanner kb = new Scanner(System.in);
		String busID;

		System.out.println("Enter the ID of the bus you want to delete: ");
		busID = kb.nextLine();
		stmt.executeUpdate("DELETE Bus WHERE busid = '" + busID + "'");
	}
	
	public static void recordData(Statement stmt) throws SQLException {
		System.out.println("not available");
	}
}
