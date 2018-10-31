//Adelayde Rome 
//7675884868
public class Date
{
  private String Month;
  private int Day;
  private int Year;

  public String getMonth() { return this.Month; }

  public void setMonth(String Month) { this.Month = Month; }
  
  public int getMonthNumber(String month) {
	int one = 1, two = 2, three = 3, four = 4, five = 5, six = 6, seven = 7, 
			eight = 8, nine = 9, ten = 10, eleven = 11, twelve = 12;
	if(month == "January") return one;
	else if(month == "February") return two;
	else if(month == "March") return three;
	else if(month == "April") return four;
	else if(month == "May") return five;
	else if(month == "June") return six;
	else if(month == "July") return seven;
	else if(month == "August") return eight;
	else if(month == "September") return nine;
	else if(month == "October") return ten;
	else if(month == "November") return eleven;
	else return twelve;	
  }

  public int getDay() { return this.Day; }

  public void setDay(int Day) { this.Day = Day; }

  public int getYear() { return this.Year; }

  public void setYear(int Year) { this.Year = Year; }
}