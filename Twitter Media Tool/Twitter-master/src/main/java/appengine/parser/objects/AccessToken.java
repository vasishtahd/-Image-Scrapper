package appengine.parser.objects;

/**
 * Created by anand.kurapati on 28/06/17.
 */
public class AccessToken {

    public String access_token;
    public ID_TYPE id_type;
    public String id;

    public AccessToken(String access_token, ID_TYPE id_type, String id) {
        this.access_token = access_token;
        this.id_type = id_type;
        this.id = id;
    }

    public enum ID_TYPE {
        USER,
        PAGE
    }
}
