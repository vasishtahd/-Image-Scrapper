package appengine.parser.objects;

/**
 * Created by anand.kurapati on 26/06/17.
 */
public class UserCredentials {
    public String userName;
    public String password;

    public UserCredentials(String userName,String password){
        this.userName=userName;
        this.password=password;
    }
}
