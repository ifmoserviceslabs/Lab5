package com.ifmo.serviceslab5maven;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSQLDAO {
    private List<Employee> convertSetToList(ResultSet rs) throws SQLException{
        List<Employee> employees = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            int age = rs.getInt("age");
            int department = rs.getInt("department");
            String city = rs.getString("city");
            Employee employee = new Employee(id, name, surname, age, city, department);
            employees.add(employee);
        }
        return employees;
    }
    
    public List<Employee> filterEmployees(String name, String surname, int age, String city, int department) {
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()){
            Statement stmt = connection.createStatement();
            String query = "select * from employee";
            List<String> filterOptions = new ArrayList<>();
            if(name != null && !name.trim().isEmpty()){
                filterOptions.add("name = '" + name + "'");
            }
            if(surname != null && !surname.trim().isEmpty()){
                filterOptions.add("surname = '" + surname + "'");
            }
            if(age > 0){
                filterOptions.add("age = " + age);
            }
            if(city != null && !city.trim().isEmpty()){
                filterOptions.add("city = '" + city + "'");
            }
            if(department > 0){
                filterOptions.add("department = " + department);
            }
            for(int i=0; i<filterOptions.size(); ++i)
                if(i == 0)
                    query += " where " + filterOptions.get(i);
                else
                    query += " and " + filterOptions.get(i);
            ResultSet rs = stmt.executeQuery(query);
            employees = convertSetToList(rs);
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return employees;
    }
    
    private String escapeStringValue(String value){
        if(value == null){
            return "''";
        }
        else {
            return "'" + value + "'";
        }
    }
    
    private Map<String, String> getColumnValuesMap(String name, String surname, int age, String city, int department, boolean enableEmpty){
        Map<String, String> map = new HashMap<>();
        if(name != null || enableEmpty){
            map.put("name", escapeStringValue(name));
        }
        if(surname != null || enableEmpty){
            map.put("surname", escapeStringValue(surname));
        }
        if(age >= 0 || enableEmpty){
            map.put("age", String.valueOf(age));
        }
        if(city != null || enableEmpty){
            map.put("city", escapeStringValue(city));
        }
        if(department >= 0 || enableEmpty){
            map.put("department", String.valueOf(department));
        }
        return map;
    }
   
    public int createEmployee(String name, String surname, int age, String city, int department){
        int result = 0;
        Map<String, String> map = getColumnValuesMap(name, surname, age, city, department, true);  
        String columnsString = "";
        String valuesString = "";
        List<Map.Entry<String, String>> entrySet = new ArrayList<>();
        entrySet.addAll(map.entrySet());
        for(int i = 0; i < entrySet.size(); ++i){
            columnsString += entrySet.get(i).getKey();
            valuesString += entrySet.get(i).getValue();
            if(i + 1 < entrySet.size()){
                columnsString += ", ";
                valuesString += ", ";
            }
        }
        String query = "INSERT INTO employee(" + columnsString + ") VALUES (" + valuesString + ") RETURNING id";
        Connection connection = ConnectionUtil.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            result = rs.getInt("id");
        } catch (SQLException ex){
            Logger.getLogger(PostgreSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public String updateEmployee(int id, String name, String surname, int age, String city, int department){
        String status = "Something went wrong";
        if(id <= 0){
            return status;
        }
        Map<String, String> map = getColumnValuesMap(name, surname, age, city, department, false);  
        String setString = "";
        List<Map.Entry<String, String>> entrySet = new ArrayList<>();
        entrySet.addAll(map.entrySet());
        for(int i = 0; i < entrySet.size(); ++i){
            setString += entrySet.get(i).getKey() + " = " + entrySet.get(i).getValue();
            if(i + 1 < entrySet.size()){    
                setString += ", ";
            }
        }
        String query = "UPDATE employee SET " + setString + " WHERE id=" + id;
        Connection connection = ConnectionUtil.getConnection();
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            status = "Success";
        } catch (SQLException ex){
            Logger.getLogger(PostgreSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }
    
    public String deleteEmployee(int id){
        String status = "Something went wrong";
        if(id <= 0){
            return status;
        }
        String query = "DELETE FROM employee WHERE id=" + id;
        Connection connection = ConnectionUtil.getConnection();
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            status = "Success";
        } catch (SQLException ex){
            Logger.getLogger(PostgreSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }
}