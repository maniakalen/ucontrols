package plugins.manager;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
class CustomTreeCellRenderer extends DefaultTreeCellRenderer {

    private JLabel label;

    CustomTreeCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
    }

    @Override
    public Component getTreeCellRendererComponent(
        JTree tree,
        Object value,
        boolean selected,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocus) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        Object target = node.getUserObject();
        ImageIcon i;
        if ( target instanceof TaskGroup ) {
            i = this.getScaledIcon("group.jpg");
        } else {
            i = this.getScaledIcon("task.jpg");
        }
        label.setIcon(i);
        label.setText(node.toString());
        label.setToolTipText("some text");
        if (selected) {
            label.setBackground(backgroundSelectionColor);
        } else {
            label.setBackground(backgroundNonSelectionColor);
        }
        return label;
    }
    
    private ImageIcon getScaledIcon(String path) {
        ImageIcon i = new ImageIcon(path);
        Image img = i.getImage();
        Image scaled = img.getScaledInstance(14, 14, java.awt.Image.SCALE_SMOOTH);
        i = new ImageIcon(scaled);
        return i;
    }
}