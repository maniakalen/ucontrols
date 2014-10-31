package application;
import java.awt.event.*;
import javax.swing.JFrame;
public abstract class PluginItemAbstract implements ActionListener
{
    protected JFrame _frame;
    protected Plugin _parent;
    public void setParentFrame(JFrame frame) {
        this._frame = frame;
    }
    public void setParentPlugin(Plugin p) {
        this._parent = p;
    }
    public Plugin getParent() {
        return this._parent;
    }
}
