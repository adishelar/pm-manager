package com.PM_scheduler.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PM_scheduler.Service.EmployeeService;
import com.PM_scheduler.Enitity.Employee;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin
public class EmployeeController{

@Autowired
private EmployeeService service; // for calling service methods

@PostMapping
public ResponseEntity<Employee> add(@RequestBody Employee e){
Employee saved=service.addEmployee(e); // save employee
	return ResponseEntity.status(201).body(saved); // return saved emp
}

@PutMapping("/{id}")
public ResponseEntity<?> update(@PathVariable String id,@RequestBody Map<String,String> body){
 Employee updated=service.updateEmployee(id,body.get("status"),body.get("feedback"));
  if(updated!=null){
  return ResponseEntity.ok(updated);
 }
else{
  return ResponseEntity.status(404).body("Employee Not Found");
}
}

@GetMapping
public ResponseEntity<List<Employee>>all(){
 return ResponseEntity.ok(service.getAll());
}
	
@GetMapping("/pdf")
public ResponseEntity<?>getPdf(@RequestHeader String role){
    // check role is supervisor
 if(!role.equalsIgnoreCase("supervisor")){
return ResponseEntity.status(403).body("Access Denied");
}
byte[] pdfData=service.generatePdfFromHtml();
return ResponseEntity.ok()
 .header("Content-Disposition","attachment; filename=employees.pdf")
  .body(pdfData);
}

// health check api to see if working
@GetMapping("/health")
public String health(){
 return "API Working Fine";
}

}
