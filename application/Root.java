package application;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import application.config.Config;
public class Root
{
    private MainFrame _mainFrame;
    private PluginManager _manager;
    private application.config.Config _config;
    public Root() {
        String file = System.getProperty("vmConfig");
        this._config = new application.config.Config(file);
        this.setFrame( this.createNewFrame() );
        this.assignPluginManager();
        this._mainFrame.addWindowListener(new MainFrameWindowAdapter(this._manager));
    }
    public application.config.Config getConfig() {
        return this._config;
    }
    public MainFrame getFrame() {
        return this._mainFrame;
    }
    public void setFrame(MainFrame frame) {
        this._mainFrame = frame;
    }
    
    public MainFrame createNewFrame() {
        MainFrame frame = new MainFrame();
        return frame;
    }
    public void assignPluginManager() {
        this._manager = new PluginManager(this);
        this._manager.registerPlugins();
    }
    public void postCreate() {
        this._manager.postRegisterPlugins();
    }
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Root r = new Root();
                r.getFrame().setVisible(true);
                r.postCreate();
            }
        });
    }
}
