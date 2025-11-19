package com.PM_scheduler.Repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.PM_scheduler.Enitity.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee,String>{
	

}
