package plugins.tracker;

import application.os.*;
import application.config.*;
import application.*;
import javax.swing.*;
import org.jdom2.*;
import org.jdom2.input.*;
import java.lang.Thread;
import java.util.*;
import java.awt.event.*;
public class Starter extends Plugin
{
    private JFrame _frame;
    private List<TabWindow> windows;
    @Override
    public void registerPlugin(JFrame frame) {
        try {
        this._frame = frame;
        this.windows = new ArrayList<TabWindow>();

        this._prepareThreads();
        this._registerPluginMenu(frame);
        Starter.Closure closer = this.new Closure();
        closer.setThreadsList(this.windows);
        Runtime.getRuntime().addShutdownHook(closer);
    } catch (Exception e) {
        e.printStackTrace(System.err);
    }
    }
    public List<TabWindow> getWindowsList() {
        return this.windows;
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
            ((UserInterface)p).prepareInterface();
            item.addActionListener(p);
            m.add(item);
        }     
        f.getFrameMenuBar().add(m); 
    }
    class Closure extends Thread {
        private List<TabWindow> wnd;
        public void setThreadsList(List<TabWindow> t) {
            this.wnd = t;
        }
        @Override
        public void run()
        {
            try {
                for (TabWindow w : this.wnd) {
                    w.stopExistingWorker();
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    };
    private void _prepareThreads() {
        try {
            Element servers = this._config.getChild("servers");
            TabWindow t;
            for (Element el : servers.getChildren("server")) {
                String name = el.getChild("name").getText();
                Server s = new Server(
                    el.getChild("host").getText(),
                    el.getChild("user").getText(),
                    el.getChild("pass").getText(),
                    name
                );

                List<String> str = new ArrayList<String>();
                try {
                    Element h = el.getChild("highlights");
                    if (h != null) {
                        List<Element> hs = h.getChildren("highlight");
                        if (hs != null) {
                            for (Element e : hs) {
                                str.add(e.getAttribute("phrase").getValue());
                            }
                        }
                    }
                } catch (Exception highs) {
                    highs.printStackTrace(System.err);
                }
                t = new TabWindow(name, s, str);
                if (el.getChild("run_by_default") != null) {
                    t.startThread();
                }
                this.windows.add(t);
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(2);
        }
    }
    public void shutdown() {
        
    }
}
