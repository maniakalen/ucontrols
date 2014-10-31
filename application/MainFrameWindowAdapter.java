package application;
import java.awt.event.*;
public class MainFrameWindowAdapter extends WindowAdapter
{
    PluginManager _manager;
    public MainFrameWindowAdapter(PluginManager manager) {
        super();
        this._manager = manager;
    }
    @Override
    public void windowClosing(WindowEvent e) {
        try {
            this._manager.shutdownPlugins();
        } catch (Exception ex) {
            System.err.println("Error handled");
            ex.printStackTrace(System.err);
        }
        System.exit(0);
    }
}
