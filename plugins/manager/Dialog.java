package plugins.manager;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;

import application.os.*;
import application.config.*;
import application.*;

public class Dialog extends PluginItemAbstract
{
    private JDialog _dialog;
    public Dialog() {
        super();
        this._dialog = new JDialog(this._frame, "Dialog");
        Dimension d = new Dimension(400,220);
        this._dialog.setSize(d);
    }
    
    public void setUpDialog() {
        this._dialog.add(((Manager)this.getParent()).getUserInterface().buildDialogUI());
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Manager m = (Manager)this.getParent();
        if (m.getUserInterface().isClockRunning()) {
            m.userAlert("Some wild alert");
        }
        this._dialog.setVisible(true);
    }
}
