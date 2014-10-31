package plugins.tracker;

import application.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.Dimension;

public class UserInterface extends PluginItemAbstract
{
    private JDialog _dialog;
    public UserInterface() {
        super();
        this._dialog = new JDialog(this._frame, "Dialog", true);
        Dimension d = new Dimension(1800,1220);
        this._dialog.setSize(d);
    }
    public void prepareInterface() {
        JTabbedPane tabbedPane = new JTabbedPane();
        List<TabWindow> wnd = ((Starter)this.getParent()).getWindowsList();
        for (TabWindow w : wnd) {
            tabbedPane.addTab(w.getName(),w);
        }
        
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this._dialog.add(tabbedPane);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        this._dialog.setVisible(true);
    } 
}
