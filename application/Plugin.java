package application;
import javax.swing.*;
import java.util.HashMap;
import java.awt.event.*;
import java.awt.*;
import org.jdom2.*;
import org.jdom2.input.*;
import application.config.*;
public class Plugin implements PluginInterface
{
    protected String _name;
    protected HashMap<String, String> _list;
    protected Element _config;
    protected Config.ConfigSourceInstance _src;
    public void registerPlugin(JFrame frame) {}
    
    public void setName(String name) {
        this._name = name;
    }
    
    public String getName() {
        return this._name;
    }
    
    public void itemsConfig(HashMap<String, String> list) {
        this._list = list;   
    }
    public void setConfig(Element config,Config.ConfigSourceInstance source) {
        this._config = config;
        this._src = source;
    }
    
    protected PluginItemAbstract _getNewItemInstance(String cls) {
        PluginItemAbstract p;
        try {
            Class<?> c = Class.forName(cls);
            p = (PluginItemAbstract)c.newInstance();
        } catch (Exception ex) {
            p = new PluginItemAbstract() { 
                @Override
                public void actionPerformed(ActionEvent e) {}
            };
            ex.printStackTrace(System.err);
        }
        return p;
    }
    protected void _registerPluginMenu(JFrame frame) {
        MainFrame f = (MainFrame)frame;
        JMenu m = new JMenu(this.getName());
        PluginItemAbstract p;
        for ( String key : this._list.keySet() ) {
            JMenuItem item = new JMenuItem(key);
            p = this._getNewItemInstance(this._list.get(key));
            p.setParentFrame(frame);
            p.setParentPlugin(this);
            
            item.addActionListener(p);
            m.add(item);
        }     
        f.getFrameMenuBar().add(m); 
    }
    public void postRegister() {}
    public void shutdown() {}
}
