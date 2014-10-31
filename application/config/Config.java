package application.config;

import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.input.*;
import java.util.*;
import java.io.*;

public class Config
{
    private ConfigSourceInstance _config;
    
    public Config(String file) {
        this._config = new ConfigXml(file);
    }
    
    public List<Config.ConfigPlugin> getPluginConfigList() {
        Element plugins = this._config.getNode("plugins");
        List<Config.ConfigPlugin> listing = new ArrayList<Config.ConfigPlugin>();
        for ( Element item : plugins.getChildren("plugin") ) {
            String name = item.getAttribute("name").getValue();
            String cls = item.getAttribute("class").getValue();
            Element itemsContainer = item.getChild("items");
            List<Element> items = itemsContainer.getChildren();
            listing.add(new Config.ConfigPlugin(name, cls, items, item.getChild("config"),this._config)); 
        }
        return listing;
    }
    
    public static interface ConfigSourceInstance {
        public Element getNode(String name);
        public void save();
    }
    public class ConfigXml implements ConfigSourceInstance {
        private Document _data;
        
        public ConfigXml(String file) {
            try {

                SAXBuilder saxBuilder = new SAXBuilder();
                this._data = saxBuilder.build(new File("config.xml"));
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        public Document getData() {
            return this._data;
        }
        public void setData(Document data) {
            this._data = data;
        }
        public Element getNode(String name) {
            return this._data.getRootElement().getChild(name);
        }
        public void save() {
            XMLOutputter output = new XMLOutputter();
            try {
                output.output(this._data, new FileOutputStream("config.xml"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public class ConfigPlugin {
        private String _name;
        private String _class;
        private List<Element> _items;
        private Element _config;
        private ConfigSourceInstance _source;
        public ConfigPlugin(String name, String cls, List<Element> nodes, Element config,ConfigSourceInstance src) {
            this._name = name;
            this._class = cls;
            this._items = nodes;
            this._config = config;
            this._source = src;
        }
        
        public String getName() { return this._name; }
        public String getClassName() { return this._class; }
        public HashMap<String, String> getPluginItems() {
            HashMap<String, String> itemsList = new HashMap<String, String>();
            for ( Element item : this._items ) {
                if ( item.getName() == "item" ) {
                    itemsList.put(item.getAttribute("name").getValue(), item.getAttribute("class").getValue());
                }
            }
            return itemsList;
        }
        public Element getConfig() {
            return this._config;
        }
        public ConfigSourceInstance getSrc() {
            return this._source;
        }
    }
}
