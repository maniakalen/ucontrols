package plugins.manager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.*;
import java.awt.event.*;
public class ManagerTreeSelectionListener extends MouseInputAdapter implements TreeSelectionListener
{
    private JTree _tree;
    private UserInterface _face;
    public ManagerTreeSelectionListener(JTree tree, UserInterface face) {
        this._tree = tree;
        this._face = face;
    }
    public void valueChanged(TreeSelectionEvent e) { 
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           _tree.getLastSelectedPathComponent();

        if (node == null) return;

        this._setTaskSelection(node);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            
            Object tree = e.getSource();
            
            if (tree instanceof JTree) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                   ((JTree)tree).getLastSelectedPathComponent();
                this._setTaskSelection(node);
            }
        }
    }
    private void _setTaskSelection(DefaultMutableTreeNode node) {
        Object nodeInfo = node.getUserObject();
        if ( nodeInfo instanceof Task ) {
            this._face.setMainFramePanelTask((Task)nodeInfo);
        }
    }
}
