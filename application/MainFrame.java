package application;
import javax.swing.*;
import java.awt.*;
public class MainFrame extends JFrame
{
    private JMenuBar _menu = null;
    private TrayIcon _tray;

    public MainFrame() {
        super();
        this.getContentPane().setLayout(new BorderLayout());
    }
    public void addToTopBorder(Component item) {
        this.getContentPane().add(item,BorderLayout.NORTH);
    }
    public void addToBottomBorder(Component item) {
        this.getContentPane().add(item,BorderLayout.SOUTH);
    }
    public void addToRightBorder(Component item) {
        this.getContentPane().add(item,BorderLayout.EAST);
    }
    public void addToLeftBorder(Component item) {
        this.getContentPane().add(item,BorderLayout.WEST);
    }
    
    public JMenuBar getFrameMenuBar() {
        if ( this._menu == null ) {
            this.setMenuBar(new JMenuBar());
        }
        return this._menu;
    }
    
    public void setMenuBar(JMenuBar bar) {
        this._menu = bar;
        this.setJMenuBar(this._menu);
    }

    public void setTrayIcon(TrayIcon icon) {
        try {
            if (this._tray != null) {
                SystemTray.getSystemTray().remove(this._tray);
            }
            this._tray = icon;
            SystemTray.getSystemTray().add(this._tray);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
    public TrayIcon getTrayIcon() {
        return this._tray;
    }
}
