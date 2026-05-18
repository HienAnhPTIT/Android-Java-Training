package com.example.myapplication.db.entity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
@Entity(tableName = "employees")
public class EmployeeEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "department")
    private String department;
    @ColumnInfo(name = "position")
    private String position;
    @ColumnInfo(name = "salary")
    private double salary;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "phone")
    private String phone;
    @ColumnInfo(name = "synced_at")
    private long syncedAt;

    public EmployeeEntity() {
    }

    public EmployeeEntity(int id, String name, String department, String position, double salary, String email, String phone, long syncedAt) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.position = position;
        this.salary = salary;
        this.email = email;
        this.phone = phone;
        this.syncedAt = syncedAt;
    }

    public EmployeeEntity(int id, String name, String department, String position, double salary, String email, String phone) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.position = position;
        this.salary = salary;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public double getSalary() {
        return salary;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public long getSyncedAt() {
        return syncedAt;
    }
    public void setSyncedAt(long syncedAt) {
        this.syncedAt = syncedAt;
    }
}
