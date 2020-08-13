package appengine.parser.instagram.objects;

public class UserNameAndID {
    public int id;
    public String userName;
    public String userID;

    public UserNameAndID(int id, String userID, String userName) {
        this.id = id;
        this.userID = userID;
        this.userName = userName;
    }

}
