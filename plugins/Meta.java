package plugins;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import org.jdom2.*;
import org.jdom2.input.*;
import application.os.*;
import application.config.*;
import application.*;

public class Meta extends Plugin
{
    private JFrame _frame;
    private String _title;
    private ImageIcon _icon;
    @Override
    public void registerPlugin(JFrame frame) {
        this._frame = frame;
        this._readConfig();
        this._setMetaTitle(frame);
        this._setMetaIcon(frame);
    }
    
    private void _setMetaTitle(JFrame frame) {
        if (this._title != null) {
            frame.setTitle(this._title);
        }
    }
    private void _setMetaIcon(JFrame frame) {
        if (this._icon != null) {
            frame.setIconImage(this._icon.getImage());
        }
    }
    
    private void _readConfig() {
        Element metas = this._config.getChild("metas");
        String attr;
        for ( Element item : metas.getChildren("meta") ) {
            attr = item.getAttribute("type").getValue();
            if (attr.equals("title")) {           
                this._title = item.getText();
            } else if (attr.equals("icon")) {
                this._icon = new ImageIcon(item.getText());
            }
        }
    }
    
    
}
