package com.PM_scheduler.Controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PM_scheduler.Entity.Employee;
import com.PM_scheduler.Repo.EmployeeRepository;
import com.PM_scheduler.Service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin
public class EmployeeController{
  @Autowired
    private EmployeeRepository repo;
  @Autowired
    private EmployeeService service;

    // Add employee
    @PostMapping
    public ResponseEntity<Employee> add(@RequestBody Employee e){
        Employee saved = service.addEmployee(e);
        return ResponseEntity.status(201).body(saved);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody Map<String, String> body){
        Employee updated = service.updateEmployee(id, body.get("status"),body.get("feedback"));
      if (updated != null){
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.status(404).body("Employee Not Found");
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<Employee>> all() {
        return ResponseEntity.ok(service.getAll());
    }

    // PDF for all employees
    @GetMapping("/pdf")
    public ResponseEntity<?> getPdf(@RequestHeader String role){
       if (!role.equalsIgnoreCase("Supervisor")){
            return ResponseEntity.status(403).body("Access Denied");
        }
        byte[] pdfData = service.generatePdfFromHtml();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=employees.pdf")
                .body(pdfData);
    }

    @GetMapping("/by-date")
    public List<Employee> getByDate(@RequestParam String date) {
        LocalDate selectedDate = LocalDate.parse(date);
        return repo.findAll().stream()
                .filter(emp -> emp.getTimestamp().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .equals(selectedDate)
                )
                .collect(Collectors.toList());
    }

    // PDF filtered by date
    @GetMapping("/pdf-by-date")
    public ResponseEntity<byte[]> getPdfByDate( @RequestParam String date, @RequestHeader("role") String role){
      if (!role.equalsIgnoreCase("Supervisor")) {
            return ResponseEntity.status(403).build();
        }
        LocalDate selectedDate = LocalDate.parse(date);
        List<Employee> filtered = repo.findAll()
                .stream()
                .filter(emp ->
                emp.getTimestamp().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .equals(selectedDate)
        )
                .collect(Collectors.toList());

        // NEW method you must add in service
        byte[] pdf = service.generatePdf(filtered);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=employees-" + date + ".pdf")
                .body(pdf);
    }

    @GetMapping("/health")
    public String health() {
        return "API Working Fine";
    }
}
