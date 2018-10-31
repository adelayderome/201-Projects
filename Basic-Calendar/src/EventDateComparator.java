//Adelayde Rome
//7675884868

import java.util.Comparator;

public class EventDateComparator implements Comparator<Event>{

	public int compare(Event e1, Event e2) {
		Date d1 = e1.getDate(), d2 = e2.getDate();
		if(d1.getYear() > d2.getYear()) { return 1; }
		else if(d1.getYear() < d2.getYear()) { return -1; }
		else { //same year
			if(d1.getMonthNumber(d1.getMonth()) > d2.getMonthNumber(d2.getMonth())) {return 1; }
			else if(d1.getMonthNumber(d1.getMonth()) < d2.getMonthNumber(d2.getMonth())) {return -1; }
			else { //same month
				if(d1.getDay() > d2.getDay()) {return 1; }
				else if(d2.getDay() < d2.getDay()) {return -1; }
			}
		}
		//else if same exact date
		return 0;
	}

}
