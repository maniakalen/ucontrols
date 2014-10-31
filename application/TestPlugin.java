package application;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
public class TestPlugin extends Plugin
{
    @Override
    public void registerPlugin(JFrame frame) {
        this._registerPluginMenu(frame);
    }
    
    

    
    public static class ItemDos extends PluginItemAbstract { 
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Button clicked two");
        }
    }
}
