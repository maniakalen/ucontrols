package plugins.manager;

import java.lang.*;
import java.sql.*;
public class Task
{
    private int _id;
    private int _groupId;
    private String _title;
    private String _desc;
    private long _created;
    private boolean _status;
    private int _openTimeId;
    private Connection _connect;
    public Task(Connection connect) {
        this._connect = connect;
    }
    public Task(Connection connect, int id, int groupId, String title, String desc, long created, boolean status, int openTime) {
        this._connect = connect;
        this._id = id;
        this._groupId = groupId;
        this._title = title;
        this._desc = desc;
        this._created = created;
        this._status = status;
        this._openTimeId = openTime;
    }
    public void setId(int id) {
        this._id = id;
    }
    public void setOpenTimeId(int id) {
        this._openTimeId = id;
    }
    
    public boolean loadContent() {
        try {
            Statement statement = this._connect.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM tasks WHERE id = " + this._id);
            result.first();
            this._groupId = result.getInt("group_id");
            this._title = result.getString("title");
            this._desc = result.getString("description");
            this._created = result.getLong("created_on");
            this._status = result.getBoolean("status");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getTitle() {
        return this._title;
    }
    public String getDescription() {
        return this._desc;
    }
    public void setStartTask(long start) {
        try {
            Statement st = this._connect.createStatement();
            st.executeUpdate(String.format("INSERT INTO times (task_id, started) VALUES (%d,FROM_UNIXTIME(%d))", this._id, start), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()){
                this._openTimeId = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    public void setStopTask(long stop) {
        try {
            Statement st = this._connect.createStatement();
            st.executeUpdate(String.format("UPDATE times SET ended = FROM_UNIXTIME(%d) WHERE id = %d", stop, this._openTimeId));
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    
    public String toString() {
        return this._title != null && this._title.length() > 0 ? this._title : this._desc;
    }
}
