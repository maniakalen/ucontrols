package plugins.manager;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.awt.event.*;
public class ControlButton extends JButton implements ActionListener
{
    private Clock _clock;
    private Task _task;
    public ControlButton(String label, Clock clock, Task task) {
        super(label);
        this._clock = clock;
        this._task = task;
    }
    public ControlButton(String label, Clock clock) {
        super(label);
        this._clock = clock;
    }
    public void setTask(Task task) {
        if (this._clock.isRunning()) {
            this._clock.requestStop();         
            this._task.setStopTask(this._clock.getTimeStopped());
            this._clock.reset();
            this.setText("Start");
        }
        this._task = task;
    }
    public void actionPerformed(ActionEvent e) {
        if ( this._task != null) {
            if ( !this._clock.isRunning()) {
                this._clock.reset();
                this._clock.startClock();
                this.setText("Stop");
                this._task.setStartTask(this._clock.getTimeStarted());
                //this._tree.setEnabled(false);
            } else {
                
                this._clock.requestStop();
                this._task.setStopTask(this._clock.getTimeStopped());
                this.setText("Start");
                //this._tree.setEnabled(true); 
            }
        }
    }
    public boolean isClockRunning() {
        return this._clock.isRunning();
    }
}
