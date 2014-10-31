package application.os;
import java.io.*;
public interface OperationSystemInterface
{
    static final String LINUX = "Linux";
    public void clearDns() throws IOException;
    public void addDns(String address) throws IOException;
    public String getDns();
}
