package application;
import application.config.Config;
import java.util.List;
import java.util.ArrayList;
public class PluginManager
{
    private Root _root;
    private List<Plugin> _plugins;
    public PluginManager(Root r) {
        super();
        this._plugins = new ArrayList<Plugin>();
        this._root = r;
    }
    public void registerPlugins() {
        for ( Config.ConfigPlugin item : this._root.getConfig().getPluginConfigList() ) {
            Plugin p = this._getNewPluginInstance(item.getName(), item.getClassName());
            p.itemsConfig(item.getPluginItems());
            p.setConfig(item.getConfig(),item.getSrc());
            p.registerPlugin(this._root.getFrame());
            this._plugins.add(p);
        }
    }
    public void postRegisterPlugins() {
        for ( Plugin p : this._plugins ) {
            p.postRegister();
        }
    }
    public void shutdownPlugins() {
        for ( Plugin p : this._plugins ) {
            p.shutdown();
        }
    }
    private Plugin _getNewPluginInstance(String name, String cls) {
        Plugin p;
        try {
            Class c = Class.forName(cls);
            p = (Plugin)c.newInstance();
            p.setName(name);
            
        } catch (Exception c) {
            System.err.println(c.getMessage());
            p = new Plugin();
        }
        return p;
    }
}
