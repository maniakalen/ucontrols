package application;
import javax.swing.JFrame;

public interface PluginInterface
{
    public void registerPlugin(JFrame frame);
    public void postRegister();
    public void shutdown();
}
