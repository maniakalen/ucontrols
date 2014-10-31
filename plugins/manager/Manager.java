package plugins.manager;
import javax.swing.*;
import org.jdom2.*;
import org.jdom2.input.*;
import application.os.*;
import application.config.*;
import application.*;
import java.awt.Dimension;
import java.awt.Component;
public class Manager extends Plugin
{
    private JFrame _frame;
    private UserInterface _interface;
    @Override
    public void registerPlugin(JFrame frame) {
        this._frame = frame;
        this._interface = new UserInterface();
        this._registerMainPanelUi();
        this._registerPluginMenu(frame);       
    }
    
    protected void _registerMainPanelUi() {
        ((MainFrame)this._frame).addToBottomBorder((Component)this._interface.getMainFramePanel());       
        DataBaseConnector.buildConnection(this._config.getChild("database"));
        
    }
    @Override
    protected void _registerPluginMenu(JFrame frame) {
        MainFrame f = (MainFrame)frame;
        JMenu m = new JMenu(this.getName());
        PluginItemAbstract p;
        for ( String key : this._list.keySet() ) {
            JMenuItem item = new JMenuItem(key);
            p = this._getNewItemInstance(this._list.get(key));
            p.setParentFrame(frame);
            p.setParentPlugin(this);
            ((Dialog)p).setUpDialog();
            item.addActionListener(p);
            m.add(item);
        }     
        f.getFrameMenuBar().add(m); 
    }
    
    protected UserInterface getUserInterface() {
        return this._interface;
    }
    
    public void userAlert(String text) {
        JOptionPane.showMessageDialog(this._frame, text);
    }
}
