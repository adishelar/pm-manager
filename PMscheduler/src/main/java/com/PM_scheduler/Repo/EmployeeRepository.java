package com.PM_scheduler.Repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PM_scheduler.Entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long>{
	

}
