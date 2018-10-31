//Adelayde Rome
//7675884868

import java.util.ArrayList;

public class RootObject {
  private ArrayList<User> Users;

  public ArrayList<User> getUsers() { return this.Users; }

  public void addUser(User user) { Users.add(user); }
  
  public void deleteUser(int userNumber) {  Users.remove(userNumber);  }
  
  public void setUsers(ArrayList<User> Users) { this.Users = Users; }
}

//code taken from website http://json2java.azurewebsites.net/ for basic class structure