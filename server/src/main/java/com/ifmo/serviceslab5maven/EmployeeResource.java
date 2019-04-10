package com.ifmo.serviceslab5maven;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/employees")
@Produces({MediaType.APPLICATION_JSON})
public class EmployeeResource {
    PostgreSQLDAO dao;
    
    public EmployeeResource(){
        this.dao = new PostgreSQLDAO();
    }
    
    @GET
    public List<Employee> filterEmployees(
            @QueryParam("name") String name,
            @QueryParam("surname") String surname,
            @QueryParam("age") int age,
            @QueryParam("city") String city,
            @QueryParam("department") int department) {
        List<Employee> employees = new PostgreSQLDAO().filterEmployees(name, surname, age, city, department);
        return employees;
    }
    
    @POST
    public int createEmployee(@QueryParam("name") String name, @QueryParam("surname") String surname, @QueryParam("age") int age, @QueryParam("city") String city, @QueryParam("department") int department){
        return dao.createEmployee(name, surname, age, city, department);
    }

    @PUT
    public HashMap updateEmployee(@QueryParam("id") int id, @QueryParam("name") String name, @QueryParam("surname") String surname, @QueryParam("age") int age, @QueryParam("city") String city, @QueryParam("department") int department){
        String status = dao.updateEmployee(id, name, surname, age, city, department);
        HashMap map = new HashMap<String, String>();
        map.put("status", status);
        return map;
    }

    @DELETE
    public HashMap deleteEmployee(@QueryParam("id") int id){
        String status = dao.deleteEmployee(id);
        HashMap map = new HashMap<String, String>();
        map.put("status", status);
        return map;
    }

}