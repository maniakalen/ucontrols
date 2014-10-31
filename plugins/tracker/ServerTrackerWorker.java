package plugins.tracker;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: peter.georgiev
 * Date: 10/24/14
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerTrackerWorker extends SwingWorker<Integer, String> {

    private ServerInterface server;
    private JTextPane textarea;
    private String mask;
    private boolean cancelled;
    private Connection conn;
    private Session sess;
    private MutableAttributeSet attrs;
    private Color original;
    private StyledDocument doc;
    private TabWindow window;
    public ServerTrackerWorker(ServerInterface server, JTextPane area, TabWindow window) {
        super();
        this.server = server;
        this.textarea = area;
        this.window = window;
        doc = this.textarea.getStyledDocument();
        attrs = this.textarea.getInputAttributes();
        original = StyleConstants.getForeground(attrs);
        StyleConstants.setLineSpacing(attrs, 2.5f);
    }

    @Override
    protected Integer doInBackground() throws Exception {
        this.cancelled = false;
        this.doTask();
        return 1;
    }
    @Override
    protected void process(List<String> lines) {
        try {
            if (this.textarea != null) {

                TrayIcon currentIcon = null;
                TrayIcon[] icons = SystemTray.getSystemTray().getTrayIcons();
                for (TrayIcon i : icons) {
                    currentIcon = i;
                }
                for (String line : lines) {
                    if (line.matches(this.mask)) {
                        StyleConstants.setForeground(attrs, Color.GREEN);
                        if (currentIcon != null) {
                            currentIcon.displayMessage(this.server.getName() + " -- " + this.mask, line, TrayIcon.MessageType.WARNING);
                        }
                    } else {
                        StyleConstants.setForeground(attrs, original);
                    }
                    doc.insertString(doc.getEndPosition().getOffset()-1,line + "\n", attrs);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace(System.err);

        }
    }

    protected void doTask() {
        try
        {
            String hostname = server.getHost();
            String username = server.getUsername();
            String password = server.getPassword();
            conn = new Connection(hostname);

            conn.connect();

            boolean isAuthenticated = conn.authenticateWithPassword(username, password);

            if (!isAuthenticated) {
                throw new IOException("Authentication failed.");
            }
            this.publish("Connection established");
            sess  = conn.openSession();
            this.publish("Session opened");
            sess.execCommand("tail -fn30 /usr/local/global_local/logs/php_error.log");
            StreamGobbler gb = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(gb));


            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                this.publish(line);
                Thread.sleep(50);
            }


            this.publish("All connections killed");

        } catch (InterruptedException x) {
            x.printStackTrace(System.err);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    @Override
    protected void done() {
        this.window.setStartButtonActive();
    }
    public void setHighlights(String mask) {
        this.mask = mask;
    }

    public boolean cancelWorker(boolean stop) {
        this.cancelled = true;
        if (this.sess != null) {
            this.sess.close();
            this.sess = null;
        }
        if (this.conn != null) {
            this.killAll();
            this.conn.close();
            this.conn = null;
        }
        return true;
    }

    private void killAll() {
        if (conn != null) {
            try {
                Session s = conn.openSession();
                s.execCommand("killall tail");
                s.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }
}
