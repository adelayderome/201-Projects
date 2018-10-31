//Adelayde Rome
//7675884868

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


public class Assignment1 {
	private static String ifile;
	static boolean changesMade = false;
	private static String garbage;
	
	public static RootObject getFile(Scanner scan) {
		boolean found = false;
		//Scanner scan = new Scanner(System.in);
		RootObject Calendar = null;
		
		while(!found) {
			System.out.print("What is the name of the input file? ");
			ifile = scan.nextLine();
			
			try {
				FileReader fr = new FileReader(ifile);
				Calendar = parse(fr);
				
				found = true;
				
			} catch(FileNotFoundException fnfe) {
				System.out.println("\nThat file could not be found.\n"); ///WHAT
			} catch(JsonSyntaxException jse) {
				System.out.println("\nThat file is not a well-formed JSON file.\n");
			}
		}
		return Calendar;
	}
	
	//parses the info in the json file into classes
	public static RootObject parse(FileReader fileread) {
		BufferedReader br = new BufferedReader(fileread);
		Gson gson = new Gson();
		
		//put all info from json file into classes
		RootObject Calendar = gson.fromJson(br, RootObject.class);
		
		return Calendar;
	}
	
	//list all users
	public static void listUsers(RootObject calendar) {
		ArrayList<User> users = calendar.getUsers();
		System.out.println();
		for(int i = 0; i < users.size(); i++) {
			System.out.println("\t" + (i+1) + ") "+ users.get(i).getName().getLname() + 
					", " + users.get(i).getName().getFname());
		}
		System.out.print('\n');
	}
	
	public static String monthToString(int month) {
		String one = "January", two = "February", three = "March", four = "April", five = "May",
				six = "June", seven = "July", eight = "August", nine = "September", ten = "October",
				eleven = "November", twelve = "December";
		if(month == 1) return one;
		else if(month == 2) return two;
		else if(month == 3) return three;
		else if(month == 4) return four;
		else if(month == 5) return five;
		else if(month == 6) return six;
		else if(month == 7) return seven;
		else if(month == 8) return eight;
		else if(month == 9) return nine;
		else if(month == 10) return ten;
		else if(month == 11) return eleven;
		else return twelve;	
	}
	
	//order the events of one user
	public static void orderEvents(User user) {
		//call comparator 
		Collections.sort(user.getEvents(), new EventDateComparator());
	}
	
	//option 1: display users calendar
	public static void opt1(RootObject calendar) {
		ArrayList<User> users = null;
		boolean usersExist = false;
		try {
			users = calendar.getUsers();
			usersExist = true;
		} catch(NullPointerException npe) {
			System.out.println('\n' + "No users exist. ");
		}
		if(usersExist) {
			int userNum = 1;
			//loop through users to print 
			System.out.print('\n');
			for(int i = 0; i < users.size(); i++) {
				System.out.println("\t" + userNum + ") "+ users.get(i).getName().getLname() + 
						", " + users.get(i).getName().getFname());
				//loop through events to print
				ArrayList<Event> userEvents = users.get(i).getEvents();
				char letter = 'a'; 
				if(userEvents.size() > 0) {
					//order events for each user before printing
					orderEvents(users.get(i));
					for(int j = 0; j < userEvents.size(); j++) {
						Event event = userEvents.get(j);
						Date date = event.getDate();
						System.out.println("\t" + "\t" + Character.toString(letter) + ". " + event.getTitle() + ", " + 
								event.getTime() + ", " + date.getMonth() + " " + date.getDay() 
								+ ", " + date.getYear());
						letter++;
					}
				}
				userNum++;
			}
		}
	}
	//option 2: add user to calendar
	public static void opt2(RootObject calendar, Scanner scan) {
		boolean correctInput = false;
		String fn = null, ln = null;

		garbage = scan.nextLine();
		while(!correctInput) {
			System.out.println('\n'  + "What is the user's name? ");
			
			fn = null;
			ln = null;
			
			String name = scan.nextLine();
			String[] names = name.split(" "); 
			try {
				fn = names[0];
				ln = names[1];
				correctInput = true;
			} catch(ArrayIndexOutOfBoundsException aioobe) {
				System.out.println("Invalid, must have first and last name." );
			}

		}
		Name newName = new Name();
		newName.setFname(fn);
		newName.setLname(ln);
		User addition = new User();
		addition.setName(newName);
		ArrayList<Event> events = new ArrayList<Event>();
		addition.setEvents(events);
		calendar.addUser(addition);
		
		changesMade = true;
	}
	
	//option 3: remove user from calendar
	public static void opt3(RootObject calendar, Scanner scan) {
		if(calendar.getUsers().size() == 0) {
			System.out.println("no users exist.");
			return;
		}
		listUsers(calendar);
		System.out.println("Which user would you like to remove?");

		boolean found = false;
		int userNumber = 0;
		
		while(!found) {
			try {
				userNumber = scan.nextInt();
			} catch(InputMismatchException ime) {
				System.out.print('\n' + "Invalid input. Please enter a valid user number. ");
			}
			if(userNumber < 1 || userNumber > calendar.getUsers().size()) {
				System.out.println('\n' + "Invalid input. Please enter a valid user number. ");
			}
			else {
				found = true;
			}
		}
		//user to remove found, now remove from calendar
		Name userName = calendar.getUsers().get(userNumber-1).getName();
		calendar.deleteUser(userNumber-1);
		System.out.println('\n' + userName.getFname() + " "  + userName.getLname() + " has been deleted. ");
		changesMade = true;
	}
	
	//option 4: add event to a users calendar
	public static void opt4(RootObject cal, Scanner scan) {
		listUsers(cal);
		int userNum = -1;
		String time = null, title = null;
		int month = 0, day = 0, year = 0;
		boolean adding = false, valid = false, valid2 = false;
		
		System.out.println("To which user would you like to add an event? ");
		while(!valid) {
			try{ 
				userNum = scan.nextInt();
				valid = true;
			}
			catch(InputMismatchException ime) { 
				System.out.println("Invalid input. Please enter a user number. "); 
				garbage = scan.nextLine();
				valid = false;
			}
			if((userNum > cal.getUsers().size() || userNum < 0) && valid) {
				System.out.println("Invalid User Number.");
				valid = false;
			}
		}
		User user = cal.getUsers().get(userNum-1);
		
		while(!valid2) {
			try {
				boolean valid1 = false;
				System.out.println('\n' + "What is the title of the event? ");
				garbage = scan.nextLine();
					
				title = scan.nextLine();
				
				System.out.println('\n' + "What time is the event? ");
				time = scan.nextLine();
				while(!valid1) {
					System.out.println('\n' + "What month? ");
					month = scan.nextInt();
					valid1 = true;
					if(month < 1 || month > 12) {
						System.out.println("Invalid input. Please enter a valid month number. ");
						valid1 = false;
					}
				}
				System.out.println('\n' + "What day? ");
				day = scan.nextInt();
				System.out.println('\n' + "What year? ");
				year = scan.nextInt();
				adding = true;
				valid2 = true;
			} catch(InputMismatchException ime) { 
					System.out.println("Invalid input. Please enter a number. "); 
			}
		}
		
		Event newEvent = new Event();
		newEvent.setTitle(title);
		newEvent.setTime(time);
		Date newDate = new Date();
		
		String monthWord = monthToString(month);
		newDate.setMonth(monthWord);
		newDate.setDay(day);
		newDate.setYear(year);
		
		newEvent.setDate(newDate);
		user.addEvent(newEvent);
		if(adding) {
			System.out.println('\n' + "Added: " + title + ", " + time + ", " + monthWord + " " + day + ", "
				+ year + " to " + user.getName().getFname() + " " + user.getName().getLname() + "'s calendar. "); 
			changesMade = true;
		}
	}
	
	//option 5: remove event from users calendar
	public static void opt5(RootObject calendar, Scanner scan) {
		listUsers(calendar);
		System.out.println("From which user would you like to delte an event? ");
		int validNums = calendar.getUsers().size();
		int userNum = 0;
		boolean valid = false;
		
		while(!valid) {
			try {
				userNum = scan.nextInt();
				valid = true;
			} catch(InputMismatchException ime) { 
					System.out.println("Invalid input. Please enter a number. "); 
			}
			if(userNum < 1 || userNum > validNums) {
				System.out.print("Invalid number. Please enter a user number. ");
				valid = false;
			}
		}
		User user = calendar.getUsers().get(userNum-1);
		int numEvents = user.getEvents().size();
		if(numEvents < 1) {
			System.out.println("Calendar is empty. ");
		}
		
		else {
			orderEvents(user);
			for(int i = 0; i < user.getEvents().size(); i++) {
				Event event = user.getEvents().get(i);
				Date date = event.getDate();
				System.out.println("\t" +  (i+1) + ") " + event.getTitle() + ", " + 
						event.getTime() + ", " + date.getMonth() + " " + date.getDay() 
						+ ", " + date.getYear());
			}
			System.out.println('\n' + "Which event would you like the delete? ");
			valid = false;
			int eventToDelete = 0;
			
			while(!valid) {
				try {
					eventToDelete = scan.nextInt();
				} catch(InputMismatchException ime) {
					System.out.println("Invalid input. Please enter an event number. ");
				}
				if(eventToDelete > numEvents || eventToDelete < 1) {
					System.out.print("Invalid input. Please enter a correct event number. ");
				}
				else {
					valid = true;
					Event deleteEvent = user.getEvents().get(eventToDelete-1);
					String title = deleteEvent.getTitle();
					user.removeEvent(deleteEvent);
					System.out.println(title + " has been deleted. ");
					changesMade = true;
				}
			}
		}
	}
	
	//option 6: sorts the users alphabetically ascending or descending by last name
	public static void opt6(RootObject calendar, Scanner scan) {
		System.out.println('\n' + "How would you like to sort? ");
		System.out.println("\t" + "1) Ascending (A-Z)" + '\n' + "\t" + "2) Descending (Z-A) ");
		int number = 0;
		ArrayList<User> newUsersList = new ArrayList<User>();
		
		boolean valid = false;
		while(!valid) {
			try {
				number = scan.nextInt();
			} catch(InputMismatchException ime) {
				System.out.print("Invalid input. Please enter 1 for ascending or 2 for descending.");
			}
			if(number == 1 || number == 2) {
				valid = true;
			}
			else System.out.print("Invalid input. Please enter 1 for ascending or 2 for descending.");
		}
		
		String[] Lnames = new String[calendar.getUsers().size()];
		for(int i = 0; i < calendar.getUsers().size(); i++) {
			Lnames[i] = calendar.getUsers().get(i).getName().getLname();
		}
		//sort in ascending order
		if(number == 1) {
			Arrays.sort(Lnames);
			for(int i = 0; i < Lnames.length; i++) {
				// goal to find the match to Lnames[i] from users 
				for(int j = 0; j < calendar.getUsers().size(); j++) {
					if(Lnames[i] == calendar.getUsers().get(j).getName().getLname()) {
						newUsersList.add(i, calendar.getUsers().get(j));
					}
				}
			}
			calendar.setUsers(newUsersList);
			changesMade = true;
			listUsers(calendar);
		}
		else if(number == 2) {
			Arrays.sort(Lnames, Collections.reverseOrder());
			for(int i = 0; i < Lnames.length; i++) {
				// goal to find the match to Lnames[i] from users 
				for(int j = 0; j < calendar.getUsers().size(); j++) {
					if(Lnames[i] == calendar.getUsers().get(j).getName().getLname()) {
						newUsersList.add(i, calendar.getUsers().get(j));
					}
				}
			}
			calendar.setUsers(newUsersList);
			changesMade = true;
			listUsers(calendar);
		}
	}
	
	//option 7: write calendar to file 
	public static void opt7(RootObject calendar, Scanner scan) {
		Gson gson = new Gson();
		FileWriter writer = null;
		try {
			writer = new FileWriter(ifile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.write(gson.toJson(calendar));
			System.out.println('\n' + "File has been saved. ");
			writer.close();
		} catch (IOException e) {
			//do nothing?
		}
		
	}
	
	//option 8: exit
	public static void opt8(RootObject calendar, Scanner scan) {
		if(changesMade) {
			int choice = 0;
			boolean valid = false;
			System.out.println('\n' + "Changes have been made since the file was saved.");
			System.out.println("\t" + "1) Yes" + "\t" + "2) No");
			System.out.println("Would you like to save the file before exiting? ");
			while(!valid) {
				try {
					choice = scan.nextInt();
					if(choice < 1 || choice > 2) {
						System.out.println("Please enter a valid number. ");
					}
					else valid = true;
				} catch(InputMismatchException ime) {
					System.out.println("Please enter a valid number. ");
				}
			}
			//if yes to saving, call file write again
			if(choice == 1) {
				opt7(calendar, scan);
			}
			else {
				System.out.println('\n' + "File was not saved.");
			}
		}
		//exit message
		System.out.println('\n' + "Thank you for using my program! ");
	}
	
	//prompt list of options to user
	public static void prompt(RootObject cal, Scanner scan) {
		int optNumber = 0;
		boolean valid = false;
		while(optNumber != 8) {
			System.out.println('\n' + "\t" + "1) Display User's Calendar");
			System.out.println("\t" + "2) Add User");
			System.out.println("\t" + "3) Remove User");
			System.out.println("\t" + "4) Add Event");
			System.out.println("\t" + "5) Delete Event");
			System.out.println("\t" + "6) Sort Users"); 
			System.out.println("\t" + "7) Write File");
			System.out.println("\t" + "8) Exit");
			
			System.out.print('\n' + "What would you like to do? ");
			
			
			try {
				optNumber = scan.nextInt(); //no
				valid = true;
			} catch(InputMismatchException ime) { 
				System.out.println("Invalid input. Please enter a number. "); 
				valid = false;
				garbage = scan.nextLine();
			}
				

			if(valid == true) {
				if(optNumber > 8 || optNumber < 1) {
					System.out.println('\n' + "That is not a valid option.");
				}
				
				//display users calendar
				else if(optNumber == 1) {
					opt1(cal);
				}
				
				//add user
				else if(optNumber == 2) {
					opt2(cal, scan);
				}
				
				//remove user
				else if(optNumber == 3) {
					opt3(cal, scan);
				}
				
				//add event
				else if(optNumber == 4) {
					opt4(cal, scan);
				}
				
				//delete event
				else if(optNumber == 5) {
					opt5(cal, scan);
				}
				
				//sort users
				else if(optNumber == 6) {
					opt6(cal, scan);
				}
				
				//write file
				else if(optNumber == 7) {
					opt7(cal, scan);
					
				}
			}
		}
		//when option number is 8 to exit
		opt8(cal, scan);
		
	}
	
	public static void main(String [] args) {
		Scanner scan = new Scanner(System.in);
		
		RootObject Calendar = getFile(scan);
		
		prompt(Calendar, scan);
		
		scan.close();
	}
}

//need to add a lot of error checking?? how much to expect different inputs (little things off)
