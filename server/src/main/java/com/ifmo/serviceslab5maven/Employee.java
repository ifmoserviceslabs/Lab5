package com.ifmo.serviceslab5maven;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Employee {
    private int id;
    private String name;
    private String surname;
    private int age;
    private String city;
    private int department;
    
    public Employee() {
    }
    
    public Employee(int id, String name, String surname, int age, String city, int department) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.city = city;
        this.department = department;    
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSurname() {
        return surname;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getCity() {
        return city;
    }
    
    public int getDepartment() {
        return department;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public void setDepartment(int department) {
        this.age = department;
    }
    
    @Override
    public String toString() {
        return "Person{" + "name=" + name + ", surname=" + surname + ", age=" + age + '}';
    }
}
