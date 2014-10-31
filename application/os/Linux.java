package application.os;
import java.io.*;
public class Linux implements OperationSystemInterface
{
    static final String DNS_FILE_NAME = "/etc/resolv.conf";
    
    public void clearDns() throws IOException {
        this._fileWrite("",Linux.DNS_FILE_NAME,false);
    }
    public void addDns(String address) throws IOException {
        this._fileWrite("nameserver " + address + "\n",Linux.DNS_FILE_NAME,true);
    }
    public String getDns() {
        return this._getFirstDns(); 
    }
    private String _getFirstDns() {
        String result = "";
        try {
            BufferedReader inputStream = new BufferedReader(new FileReader(Linux.DNS_FILE_NAME));
            result = inputStream.readLine().replace("nameserver ","");
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return result; 
    }
    private void _fileWrite(String message, String filePath,boolean append ) throws IOException {            
        BufferedWriter outputStream = new BufferedWriter(new FileWriter(filePath, append));
        outputStream.write(message);
        outputStream.close();
    }
}
