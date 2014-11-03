package plugins.tracker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.Dimension;
import javax.swing.text.StyledDocument;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import java.util.List;
public class TabWindow extends JPanel implements ActionListener
{
    private ServerInterface server;
    private JTextComponent text;
    private ServerTrackerWorker t;
    private String name;
    private JPanel controls;
    private JButton start;
    private JButton stop;
    private StyledDocument _doc;
    private Highlights highlights;
    public TabWindow(String name, ServerInterface s) {
        super();
        this.name = name;
        this.server = s;
        this.text = this.getTextPane();
        
        this.text.setSize(new Dimension(1700,800));
        JScrollPane sp = new JScrollPane(this.text);
        sp.setPreferredSize(new Dimension(1700,800));
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(sp);

        this.controls = new JPanel();
        
        this.start = new JButton("Start");
        this.stop = new JButton("Stop");
        this.start.addActionListener(this);
        this.start.setActionCommand("start");
        this.stop.addActionListener(this);
        this.stop.setActionCommand("stop");
        this.stop.setEnabled(false);
        this.controls.add(this.start);
        this.controls.add(this.stop);
        this.add(this.controls);
    }
    public TabWindow(String name, ServerInterface s, List<String> highlist) {
        this(name, s);
        if (highlist.size() > 0) {
            this.highlights = new Highlights(highlist);
        }
    }

    public ServerTrackerWorker getThread() {
        ServerTrackerWorker worker = new ServerTrackerWorker(this.server, (JTextPane)this.text, this);
        worker.setHighlights(this.highlights.getMask());
        this.t = worker;
        return worker;
    }
    public void stopExistingWorker() {
        if (this.t != null && !this.t.isCancelled() && !this.t.isDone()) {
            this.t.cancelWorker(true);
        }
    }
    public String getName() {
        return this.name;
    }
    public void startThread() {
        this.getThread().execute();
        this.stop.setEnabled(true);
        this.start.setEnabled(false);
    }
    private JTextPane getTextPane() {
        JTextPane pane = new JTextPane();
        pane.setEditable(false);
        this._doc = new DefaultStyledDocument();
        pane.setStyledDocument(this._doc);
        return pane;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("stop".equals(e.getActionCommand())) {
            t.cancelWorker(true);
            setStartButtonActive();
        } else {
            this.getThread().execute();
            setStopButtonActive();
        }
    }

    public void setStartButtonActive(){
        this.stop.setEnabled(false);
        this.start.setEnabled(true);
    }
    public void setStopButtonActive(){
        this.stop.setEnabled(true);
        this.start.setEnabled(false);
    }
}
