package plugins.manager;

import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.tree.*;
public class UserInterface
{
    private JPanel _panel;
    private JLabel _title;
    private JLabel _desc;
    private JLabel _clock;
    private ControlButton _start;
    public UserInterface()
    {
        this._prepareMainFramePanel();
    }
    
    private void _prepareMainFramePanel() {
        try {
            this._panel = new JPanel();
            this._panel.setLayout(new BorderLayout());
            
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3,1));
            this._panel.add(new JSeparator(JSeparator.HORIZONTAL),BorderLayout.NORTH);
            this._title = new JLabel("Title here");
            this._desc = new JLabel("Description here");
            panel.add(this._title);
            panel.add(this._desc);
            JPanel subpanel = new JPanel();
            subpanel.setLayout(new GridLayout(1,2, 4, 4));
            this._clock = new JLabel("00:00:00");
            this._start = new ControlButton("Start", new Clock(this._clock));
            this._start.addActionListener(this._start);
            subpanel.add(this._start);
            subpanel.add(this._clock);
            panel.add(subpanel);
            this._panel.add(panel, BorderLayout.CENTER);
        } catch (Exception e) {}
    }
    
    public JPanel getMainFramePanel() {
        if (this._panel == null) 
            this._prepareMainFramePanel();
        
        return this._panel;
    }
    
    public JLabel getClockLabel() {
        return this._clock;
    }
    
    public void setMainFramePanelTask(Task task) {
        this._start.setTask(task);
        this._title.setText(task.getTitle());
        this._desc.setText(task.getDescription());
    }
    
    public JPanel buildDialogUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        Dimension d = new Dimension(400,220);
        JScrollPane pane = new JScrollPane(this.buildManagerTree());
        pane.setSize(d);
        panel.setSize(d);
        panel.add(pane, BorderLayout.CENTER);
        
        //panel.add(new JLabel("Test"),BorderLayout.CENTER);
        return panel;
    }
    
    
    private JTree buildManagerTree() {
        TaskGroup tg = new TaskGroup(DataBaseConnector.getConnection());
        tg.getTaskGroups();
        tg.loadTasks();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        this.addItemsToNode(root, tg);
        JTree tree = new JTree(root);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        tree.setRootVisible(false);
        ManagerTreeSelectionListener listener = new ManagerTreeSelectionListener(tree, this);
        //tree.addTreeSelectionListener(listener);
        tree.addMouseListener(listener);
        return tree;
    }
    
    private void addItemsToNode(DefaultMutableTreeNode parent, TaskGroup group) {
        try {
            if ( group.getChildren().size() > 0 ) {
                for ( TaskGroup grp : group.getChildren() ) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(grp);
                    this.addItemsToNode(node, grp);
                    parent.add(node);
                }
            }
            if ( group.getTasks().size() > 0 ) {
                for ( Task t : group.getTasks() ) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(t);
                    parent.add(node);
                }
            }
        } catch (Exception e) {
            System.err.println("Failing group " + group.getId());
        }
    }
    
    public boolean isClockRunning() {
        return this._start.isClockRunning();
    }
}
