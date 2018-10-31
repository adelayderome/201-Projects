//Adelayde Rome
//7675884868
import java.util.ArrayList;

public class User {
  private Name Name;
  private ArrayList<Event> Events;

  public Name getName() { return this.Name; }
  public void setName(Name Name) { this.Name = Name; }

  public ArrayList<Event> getEvents() { return this.Events; }
  public void setEvents(ArrayList<Event> Events) { this.Events = Events; }
  
  public void addEvent(Event newEvent) { this.Events.add(newEvent); }
  
  public void removeEvent(Event deleteEvent) { this.Events.remove(deleteEvent); }
  
}