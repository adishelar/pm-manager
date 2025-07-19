package com.PM_scheduler.Service;

import java.io.ByteArrayOutputStream; // for pdf output
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PM_scheduler.Enitity.Employee;
import com.PM_scheduler.Repo.EmployeeRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@Service
public class EmployeeService{ // main service class for Employee

@Autowired
private EmployeeRepository repo; // repo object for DB
// map to track machines assigned
private Map<String,Integer> machineAssignments=new HashMap<>();
// Machines list by dept wise 
private Map<String,String[]> departmentMachines=Map.of(
"A",new String[]{"A1","A2","A3","A4","A5","A6","A7","A8","A9","A10"},
"B",new String[]{"B1","B2","B3","B4","B5","B6","B7","B8","B9","B10"},
"C",new String[]{"C1","C2","C3","C4","C5","C6","C7","C8","C9","C10"},
"D",new String[]{"D1","D2","D3","D4","D5","D6","D7","D8","D9","D10"}
);
public EmployeeService(){    
// initializing all dept to 0 machines used
machineAssignments.put("A",0);
machineAssignments.put("B",0);
machineAssignments.put("C",0);
machineAssignments.put("D",0); 
}

public Employee addEmployee(Employee e){
// get machines for dept
String[] machines=departmentMachines.get(e.getDepartment());
// current index
int index=machineAssignments.get(e.getDepartment());
// set machine to employee
e.setMachine(machines[index]);
// update index 
    /*index+1 is used for increasing index by one ,but suppose if it reaches to and of machine no
    it will become array out of bound ex.if dept has 10 machines that means 0 to 9 so after 
    last machine that is 9 it will index will become 10 and it is invalid ,so i use percentage to make a loop and comes back to
        0 ex.index =9,machine length is 10 thus (9+1)%*10=0 */
machineAssignments.put(e.getDepartment(),(index+1)%machines.length);
// saving to DB
return repo.save(e);
}



public Employee updateEmployee(String id,String status,String feedback){
    
// fetch emp by id
Employee e=repo.findById(id).orElse(null);
if(e!=null){
e.setStatus(status); // setting status
e.setFeedback(feedback); // setting feedback
return repo.save(e); // save updated emp
}
return null; // if not found
}
public List<Employee> getAll(){ 
// return all employees list
return repo.findAll();
}

public byte[] generatePdfFromHtml(){

// making HTML for PDF
StringBuilder html=new StringBuilder();

html.append("<html><head><style>");
html.append("table{width:100%;border-collapse:collapse;}");
html.append("th,td{padding:8px;border:1px solid #ddd;text-align:left;}");
html.append("th{background-color:#f2f2f2;}");
html.append("</style></head><body>");
html.append("<h1>Employee Report</h1><table>");
html.append("<tr><th>Name</th><th>Department</th><th>Machine</th><th>Status</th><th>Feedback</th><th>Timestamp</th></tr>");
// loop through employees
for(Employee e:getAll()){
html.append("<tr>")
.append("<td>").append(e.getName()).append("</td>")
.append("<td>").append(e.getDepartment()).append("</td>")
.append("<td>").append(e.getMachine()).append("</td>")
.append("<td>").append(e.getStatus()).append("</td>")
.append("<td>").append(e.getFeedback()!=null?e.getFeedback():"").append("</td>")
.append("<td>").append(e.getTimestamp()).append("</td>")
.append("</tr>");
}
// closing table
html.append("</table></body></html>");
// trying PDF creation
try(ByteArrayOutputStream output=new ByteArrayOutputStream()){

PdfRendererBuilder builder=new PdfRendererBuilder();
builder.withHtmlContent(html.toString(),null);
builder.toStream(output);
builder.run();
return output.toByteArray();
}catch(Exception ex){
// print error
ex.printStackTrace();
return new byte[0]; 
}

}
}
