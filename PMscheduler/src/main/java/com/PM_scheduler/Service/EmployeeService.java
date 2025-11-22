package com.PM_scheduler.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PM_scheduler.Entity.Employee;
import com.PM_scheduler.Repo.EmployeeRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;
  // Track machine assignment index for each department
    private Map<String, Integer> machineAssignments=new HashMap<>();

    // Department-wise machine list
    private final Map<String, String[]> departmentMachines=Map.of(
        "A",new String[]{"A1","A2","A3","A4","A5","A6","A7","A8","A9","A10"},
        "B",new String[]{"B1","B2","B3","B4","B5","B6","B7","B8","B9","B10"},
        "C",new String[]{"C1","C2","C3","C4","C5","C6","C7","C8","C9","C10"},
        "D",new String[]{"D1","D2","D3","D4","D5","D6","D7","D8","D9","D10"}
    );

    public EmployeeService() {
        // Initialize department counters
        machineAssignments.put("A", 0);
        machineAssignments.put("B", 0);
        machineAssignments.put("C", 0);
        machineAssignments.put("D", 0);
    }

    // Add new employee + auto-assign machine
    public Employee addEmployee(Employee e) {

        String department = e.getDepartment();
        String[] machines = departmentMachines.get(department);
        if (machines == null) {
            throw new RuntimeException("Invalid Department: " + department);
        }
        // Get current index
        int index = machineAssignments.get(department);
        // Assign machine to employee
        e.setMachine(machines[index]);
  // Update index using modulo to cycle (0–9)
        machineAssignments.put(department, (index + 1) % machines.length);
       return repo.save(e);
    }

    // Update status + feedback
    public Employee updateEmployee(Long id, String status, String feedback) {
        Employee e = repo.findById(id).orElse(null);
         if (e != null) {
            e.setStatus(status);
            e.setFeedback(feedback);
            return repo.save(e);
        }
        return null;
    }

    // Fetch all employees
    public List<Employee> getAll() {
        return repo.findAll();
    }
  // Generate PDF report
    public byte[] generatePdfFromHtml() {
   StringBuilder html = new StringBuilder();
 html.append("<html><head><style>")
            .append("table{width:100%;border-collapse:collapse;}")
            .append("th,td{padding:8px;border:1px solid #ddd;text-align:left;}")
            .append("th{background-color:#f2f2f2;}")
            .append("</style></head><body>");
   html.append("<h1>Employee Report</h1><table>");
        html.append("<tr>")
            .append("<th>Name</th>")
            .append("<th>Department</th>")
            .append("<th>Machine</th>")
            .append("<th>Status</th>")
            .append("<th>Feedback</th>")
            .append("<th>Timestamp</th>")
            .append("</tr>");
    // Add all employees row-by-row
        for (Employee e : getAll()) {
            html.append("<tr>")
                .append("<td>").append(e.getName()).append("</td>")
                .append("<td>").append(e.getDepartment()).append("</td>")
                .append("<td>").append(e.getMachine()).append("</td>")
                .append("<td>").append(e.getStatus()).append("</td>")
                .append("<td>").append(e.getFeedback() != null ? e.getFeedback() : "").append("</td>")
                .append("<td>").append(e.getTimestamp()).append("</td>")
                .append("</tr>");
        }
    html.append("</table></body></html>");
 try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
  PdfRendererBuilder builder = new PdfRendererBuilder();
          builder.withHtmlContent(html.toString(), null);
          builder.toStream(output);
          builder.run();
        return output.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new byte[0];
        }
    }

    public byte[] generatePdf(List<Employee> employees) {
        StringBuilder html = new StringBuilder();
      html.append("<html><head><style>");
        html.append("table{width:100%;border-collapse:collapse;}");
        html.append("th,td{padding:8px;border:1px solid #ddd;text-align:left;}");
        html.append("th{background-color:#f2f2f2;}");
        html.append("</style></head><body>");
        html.append("<h1>Employee Report</h1><table>");
        html.append("<tr><th>Name</th><th>Department</th><th>Machine</th><th>Status</th><th>Feedback</th><th>Timestamp</th></tr>");
     for (Employee e : employees) {
            html.append("<tr>")
                    .append("<td>").append(e.getName()).append("</td>")
                    .append("<td>").append(e.getDepartment()).append("</td>")
                    .append("<td>").append(e.getMachine()).append("</td>")
                    .append("<td>").append(e.getStatus()).append("</td>")
                    .append("<td>").append(e.getFeedback() != null ? e.getFeedback() : "").append("</td>")
                    .append("<td>").append(e.getTimestamp()).append("</td>")
                    .append("</tr>");
        }
 html.append("</table></body></html>");
 try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html.toString(), null);
            builder.toStream(output);
            builder.run();
            return output.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new byte[0];
        }
    }


}
