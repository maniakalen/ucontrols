package application.os;

public class OSFactory
{
    public static OperationSystemInterface getOSController() {
        OperationSystemInterface os;
        String oss = System.getProperty("os.name");
        if ( oss.equals(OperationSystemInterface.LINUX) ) {
            os = new Linux();
        } else {
            os = new OperationSystemInterface() {
                public void clearDns() {}
                public void addDns(String address) {}
                public String getDns() { return ""; }
            };
        }
        return os;
    }
}
