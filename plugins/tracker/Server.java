package plugins.tracker;


/**
 * Write a description of class Server here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Server implements ServerInterface
{
    private String host;
    private String user;
    private String pass;
    private String name;
    
    public Server(String host, String user, String pass) {
        this.host = host;
        this.user = user;
        this.pass = pass;
    }

    public Server(String host, String user, String pass, String name) {
        this(host, user, pass);
        this.name = name;
    }
    public String getHost() {
        return this.host;
    }
    public String getUsername() {
        return this.user;
    }
    public String getPassword() {
        return this.pass;
    }
    public String getName() {
        return this.name;
    }
}
