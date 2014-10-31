package plugins.manager;

import java.sql.*;
import java.util.*;

public class TaskGroup
{
    private int _id;
    private Connection _connect;
    private List<Task> _tasks;
    
    private int _parentId;
    private boolean _status;
    private String _title;
    
    private List<TaskGroup> _children;
    
    public TaskGroup(Connection con) {
        this._connect = con;
        this._id = 0;
    }
    public TaskGroup(Connection con, int id, int parent, boolean status, String title) {
        this._connect = con;
        this._id = id;
        this._parentId = parent;
        this._status = status;
        this._title = title;
    }
    public void getTaskGroups() {
        try {
            this._children = new ArrayList<TaskGroup>();
            Statement statement = this._connect.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM groups WHERE status = 1 AND parent_id = " + this.getId());
            
            while (result.next()) {
                int id = result.getInt("id");               
                TaskGroup g = new TaskGroup(this._connect, id, result.getInt("parent_id"), result.getBoolean("status"), result.getString("title"));
                g.getTaskGroups();
                g.loadTasks();
                this._children.add(g);             
            }
            
        } catch (Exception e) {
            
        }
    }
    public int getId() {
        return this._id;
    }
    public void loadTasks() {
        this._tasks = new ArrayList<Task>();
        try {
            Statement statement = this._connect.createStatement();
            ResultSet result = statement.executeQuery("SELECT t.*,i.id as started_id FROM tasks t left join times i ON t.id = i.task_id AND i.started IS NOT NULL AND i.ended IS NULL WHERE t.status = 1 AND t.group_id = " + this.getId() + " GROUP BY t.id");
            while (result.next()) {
                Task t = new Task(this._connect, result.getInt("id"),result.getInt("group_id"),result.getString("title"),result.getString("description"),result.getLong("created_on"),result.getBoolean("status"),result.getInt("started_id"));
                this._tasks.add(t);
            }
        } catch (Exception e) {}
    }
    
    public List<TaskGroup> getChildren() {
        return this._children;
    }
    public List<Task> getTasks() {
        return this._tasks;
    }
    public String toString() {
        return this._title;
    }
}
