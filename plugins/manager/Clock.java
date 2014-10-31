package plugins.manager;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.lang.*;
class Clock extends Thread {
    private JLabel _label;
    private long _timestamp;
    private boolean _started = false;
    private long _seconds = 0;
    private long _ended;

    public void startClock() {
        this._timestamp = (new Date()).getTime();
        this._started = true;
        if (!this.isAlive()) {
            this.start();
        }
    }
    public Clock(JLabel label) {
        super();
        this._label = label;    
    }
    public void run() {      
        while ( true ) {
            if (this._started) {
                this._label.setText(this.format((new Date()).getTime() - this._timestamp));
            }
            try {
                this.sleep(1000);
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());        
            }
        }
    }
    public void reset() {
        this._timestamp = (new Date()).getTime();
        this._seconds = 0;
        this._label.setText("00:00:00");
    }
    public void requestStop() {     
        this._ended = (new Date()).getTime();
        this._started = false;
        this._seconds += this._ended - this._timestamp;    
    }
    public long getSecondsRun() {
        return this._seconds;
    }
    public long getTimeStarted() {
        return (long)Math.floor((double)(this._timestamp/1000));
    }
    public long getTimeStopped() {
        return (long)Math.floor((double)(this._ended/1000));
    }
    public boolean isRunning() {
        return this._started;
    }
    private String format(long num) {
        num = num/1000;
        Long h = (long)0;
        if ( num > 3600) {
            h = new Long(num/3600);
            num = num % 3600;
        }
        Long m = (long)0;
        if ( num > 60 ) {
            
            m = new Long(num/60);
            num = num % 60;
        }
        Long s = num;
        return String.format("%02d:%02d:%02d",h,m,s);
    }
}
