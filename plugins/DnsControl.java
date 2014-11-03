package plugins;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.*;
import java.awt.event.*;
import java.io.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.*;
import application.os.*;
import application.config.*;
import application.*;
public class DnsControl extends Plugin
{
    private ArrayList<ServerButton> _buttons;
    private JFrame _frame;
    private TrayIcon _tray;
    @Override
    public void registerPlugin(JFrame frame) {
        this._frame = frame;
        this._registerPluginMenu(frame);
        this._registerContentPane(frame);
        this._updatePluginStatus();
        
    }
    public JFrame getFrame() {
        return this._frame;
    }
    protected void _updatePluginStatus() {
        String currentServer = OSFactory.getOSController().getDns();
        if ( this._buttons.size() > 0 ) {
            for ( ServerButton button : this._buttons ) {
                button.updateState(currentServer);
            }
        }
    }
    protected void _registerContentPane(JFrame frame) {
        JPanel content = new JPanel();
        
        _buttons = new ArrayList<ServerButton>();
        
        Server serverItem;
        Element servers = this._config.getChild("servers");
        content.setLayout(new GridLayout(servers.getChildren("server").size(),1));
        
        for ( Element item : servers.getChildren("server") ) {
            serverItem = new Server(item.getChild("name").getText());
            for (Element address : item.getChildren("address") ) {
                serverItem.addServerAddress(address.getText());
            }
            Element tray = item.getChild("tray");
            TrayConfig c;
            if (tray != null) {
                String trayName = tray.getChild("name").getText();
                String trayColor = tray.getChild("color").getText();
                String trayBg = tray.getChild("background").getText();
                c = new TrayConfig(trayName, trayColor, trayBg);
            } else {
                c = new TrayConfig("", "white", "black");
            }
            serverItem.setTrayConfig(c);
            serverItem.setDefault( item.getChild("default") != null );
            ServerButton b = new ServerButton(serverItem,this);
            b.addActionListener(b);
            b.setServersIfDefault();
            _buttons.add(b);
            content.add(b);
        }
        frame.setSize(ServerButton.SIZE_WIDTH,(ServerButton.SIZE_HEIGHT * _buttons.size() ));        
        frame.getContentPane().add(content);
    }
    public ArrayList<ServerButton> getButtons() {
        return this._buttons;
    }
    public void postRegister() {
        for (ServerButton b : this._buttons) {
            if (b.isButtonDefault()) {
                this._registerTray(this._frame, "vs43", b.getServerTrayConfig());
            }
        }

    }
    private void _registerTray(JFrame frame, String text) {
        TrayConfig c = new TrayConfig(text, "white", "black");
        this._registerTray(frame, text, c);
    }
    private void _registerTray(JFrame frame, String text, TrayConfig config) {
        if ( SystemTray.isSupported() ) {
            try {
                Image img = frame.createImage(22,22);
                Graphics g = img.getGraphics();
                g.setColor(config.getBackground());
                g.drawRect(0,0,22,22);
                g.setColor(config.getColor());
                g.setFont(g.getFont().deriveFont(Float.parseFloat("8.5")));
                g.drawString(config.getName(),1,16);
                if (this._tray == null) {
                    this._tray = new TrayIcon(img);
                    this._tray.setImageAutoSize(true);

                    ((MainFrame)this._frame).setTrayIcon(this._tray);
                } else {
                    this._tray.setImage(img);
                    this._tray.setToolTip(text);
                }
                this._tray.setToolTip(text);
            } catch (Exception err) {
                err.printStackTrace(System.err);
            }
        }
    }
    static class Server {
        private String _name;
        private ArrayList<String> _servers;
        private boolean _default;
        private TrayConfig _config;
        public String getName() { return this._name; }
        public ArrayList<String> getServers() { return this._servers; }
        
        public Server(String name, ArrayList<String> servers) {
            this._name = name;
            this._servers = servers;
        }
        
        public Server(String name, ArrayList<String> servers, boolean def) {
            this(name, servers);
            this._default = def;
        }
        public void setTrayConfig(TrayConfig config) {
            this._config = config;
        }
        public TrayConfig getTrayConfig() {
            return this._config;
        }
        public Server(String name) {
            this._name = name;
            this._servers = new ArrayList<String>();
        }
        public void addServerAddress(String address) {
            this._servers.add(address);
        }
        public boolean isDefault() {
            return this._default;
        }
        public void setDefault(boolean set) {
            this._default = set;
        }
    }
    static class TrayConfig {
        private String _name;
        private String _color;
        private String _background;

        public TrayConfig(String name, String color, String bgc) {
            super();
            this._name = name;
            this._color = color;
            this._background = bgc;
        }

        public String getName() {
            return this._name;
        }
        public Color getColor() {
            return this.getColorForString(this._color);
        }
        public Color getBackground() {
            return this.getColorForString(this._background);
        }
        private Color getColorForString(String name) {
            try {
                Color color;
                Field field = Class.forName("java.awt.Color").getField(name.toLowerCase()); // toLowerCase because the color fields are RED or red, not Red
                color = (Color)field.get(null);
                return color;
            } catch (ClassNotFoundException cnf) {
                return Color.WHITE;
            } catch (IllegalAccessException iae) {
                return Color.WHITE;
            } catch (NoSuchFieldException ex) {
                return Color.WHITE;
            }
        }
    }
    static class ServerButton extends JButton implements ActionListener {
        public static final int SIZE_WIDTH = 250;
        public static final int SIZE_HEIGHT = 30;
        
        private Server _server;
        private DnsControl _control;
        
        public ServerButton(Server serv, DnsControl control) {
            super(serv.getName());
            this._server = serv;
            this._control = control;
        }
        @Override
        public void actionPerformed(ActionEvent e) { 
            this.setServers();
        }
        
        public void updateState(String ip) {
            boolean active = false;
            for ( String serv : this._server.getServers() ) {
                active = serv.equals(ip);
            }
            if ( active ) {
                this.setEnabled(false);
            } else {
                this.setEnabled(true);
            }
        }
        protected void setServers() {
            try {
                OperationSystemInterface system = OSFactory.getOSController();
                system.clearDns();
                for ( String s : this._server.getServers() ) {
                    system.addDns(s);
                }
                for ( ServerButton button : this._control.getButtons() ) {
                    button.setEnabled(true);
                }
                this.setEnabled(false);
                this._control._registerTray(this._control.getFrame(),this._server.getName(), this._server.getTrayConfig());
                
            } catch (Exception ex) {
                System.err.println("Unable to change dns server");
            }
        }
 
        public void setServersIfDefault() {
            if (this._server.isDefault()) {
                this.setServers();
            }
        }
        public boolean isButtonDefault() {
            return this._server.isDefault();
        }
        public TrayConfig getServerTrayConfig() {
            return this._server.getTrayConfig();
        }
    }
    
    
    public static class Settings extends PluginItemAbstract {
        JDialog _dialog;
        public Settings() {
            super();
            this._dialog = new JDialog(this._frame, "Settings");
            this._dialog.setSize(300,241);
            this._prepareTabs();
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            this._dialog.setVisible(true);
        }
        
        private void _prepareTabs() {
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Add server",this._addServerTab());
            tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
            this._dialog.add(tabbedPane);
        }
        
        private JComponent _addServerTab() {
            return new plugins.DnsControl.Settings.AddServersPane(this._dialog, this);
        }
        
        class AddServersPane extends JPanel {
            private JTextField _serverName;
            private List<JTextField> _ips;
            private JPanel _serversPanel;
            PluginItemAbstract _item;
            JDialog _dialog;
            public AddServersPane(JDialog container, PluginItemAbstract item) {
                this._dialog = container;
                this._item = item;
                this._serverName = new JTextField("Server name");
                this._ips = new ArrayList<JTextField>();
                this._addNewServerField();
                this.setSize(new Dimension(300,151));
                this.setLayout(new BorderLayout());
                this.add(this._serverName,BorderLayout.NORTH);
                this._serversPanel = new JPanel();
                this._serversPanel.setLayout(new BoxLayout(this._serversPanel,BoxLayout.Y_AXIS));
                for ( JTextField field : this._ips ) {
                    this._serversPanel.add(field, BorderLayout.CENTER);
                }
                this.add(this._addMoreIpsButton(),BorderLayout.EAST);
                this.add(this._addToConfigButton(),BorderLayout.SOUTH);
                this.add(this._serversPanel,BorderLayout.CENTER);
            }
            JTextField _addNewServerField() {
                JTextField field = new JTextField();
                field.setSize(new Dimension(290, 28));
                field.setMaximumSize(new Dimension(290, 28));
                this._ips.add(field);
                return field;
            }
            void _addNewServerFieldToPanel(JTextField field) {
                this._serversPanel.add(field);
            }
            private JButton _addMoreIpsButton() {
                JButton button = new JButton("+");
                button.addActionListener(new plugins.DnsControl.Settings.AddServersPane.AddMoreIps(this));
                return button;
            }
            private JButton _addToConfigButton() {
                JButton button = new JButton("Add");
                button.addActionListener(new plugins.DnsControl.Settings.AddServersPane.AddToConfigAction(this._item, this));
                return button;
            }
            public String getServerName() {
                return this._serverName.getText();
            }
            public List<JTextField> getServerIpFields() {
                return this._ips;
            }
            class AddMoreIps implements ActionListener {
                protected AddServersPane _container;
                public AddMoreIps(AddServersPane c) {
                    this._container = c;
                }
                public void actionPerformed(ActionEvent e) {
                    this._container._addNewServerFieldToPanel(this._container._addNewServerField());
                    if (this._container._ips.size() >= 5) {
                        ((JButton)e.getSource()).setEnabled(false);
                    }
                    this._container._dialog.validate();
                }
            }
            class AddToConfigAction implements ActionListener {
                private PluginItemAbstract _item;
                private AddServersPane _pane;
                public AddToConfigAction(PluginItemAbstract i, AddServersPane pane) {
                    super();
                    this._item = i;
                    this._pane = pane;
                }
                public void actionPerformed(ActionEvent e) {
                    DnsControl parent = (DnsControl)this._item.getParent();
                    Element servers = parent._config.getChild("servers");
                    Element server = new Element("server");
                    Element name = new Element("name");

                    name.setText(this._pane.getServerName());
                    server.addContent(name);
                    Element ip;
                    for (JTextField field : this._pane.getServerIpFields()) {
                        ip = new Element("address");
                        ip.setText(field.getText());
                        server.addContent(ip);
                    }
                    servers.addContent(server);
                    parent._src.save();
                    parent._registerContentPane(parent.getFrame());
                }
            }
        }
        
    }
}
