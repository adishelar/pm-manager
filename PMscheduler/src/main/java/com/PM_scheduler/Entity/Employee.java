package com.PM_scheduler.Entity;

import java.util.Date;

import jakarta.persistence.Id;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Employee {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	    private Long id;
	    private String name;
	    private String department;
	    private String machine;
	    private String status="Not Active";
	    private String feedback;
	    private Date timestamp=new Date();
	    
	    
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
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
		public String getMachine() {
			return machine;
		}
		public void setMachine(String machine) {
			this.machine = machine;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getFeedback() {
			return feedback;
		}
		public void setFeedback(String feedback) {
			this.feedback = feedback;
		}
		public Date getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(Date timestamp) {
			this.timestamp = timestamp;
		}
}
