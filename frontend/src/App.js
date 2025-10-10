import { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

function App(){
const[employees, setEmployees]= useState([]);
 const[form, setForm]= useState({ name: '', department: '' });
 const[emergencyMessages, setEmergencyMessages]= useState([]);
 const[role, setRole]= useState('operator'); // Change to 'supervisor' for download button

 const departments = ['A','B','C','D'];
useEffect(() => {
     axios.get('http://pm-scheduler.onrender.com/api/employees')
     .then(res =>setEmployees(res.data))
     .catch(err =>console.error(err));
    }, []);

    const handleSubmit =(e) =>{
        e.preventDefault();
        axios.post('http://pm-scheduler.onrender.com/api/employees', form)
            .then(res =>{
             setEmployees([...employees, res.data]);
             setForm({ name: '', department: '' });
            })
            .catch(err =>console.error(err));
    };

    const handleUpdate =(id, status, feedback) =>{
        axios.put(`http://pm-scheduler.onrender.com/api/employees/${id}`, { status, feedback })
            .then(res =>{
             setEmployees(
                  employees.map(emp => emp.id === id ? res.data : emp)
              );
            })
            .catch(err =>console.error(err));
    };

    const downloadPdf =() =>{
        axios.get('http://pm-scheduler.onrender.com/api/employees/pdf', {
            responseType: 'blob',
            headers: { role: role } // Sending role to backend
        })
            .then(res =>{
             const url =URL.createObjectURL(new Blob([res.data]));
            const link =document.createElement('a');
            link.href =url;
             link.setAttribute('download','employees.pdf');
             document.body.appendChild(link);
             link.click();
            })
            .catch(err =>alert("Only Supervisor can download PDF!"));
    };

    const addEmergencyMessage =()=>{
        const msg =prompt("Enter Emergency Message:");
        if (msg){
            setEmergencyMessages([...emergencyMessages, msg]);
        }
    };

    return (
        <div className="App">
            <h1>Predictive Maintenance Scheduler</h1>

            <div>
                <label>Select Role: </label>
                <select value={role} onChange={e => setRole(e.target.value)}>
                <option value="operator">Operator</option>
                <option value="supervisor">Supervisor</option>
                </select>
            </div>

            <form onSubmit={handleSubmit}>
                <input
                    placeholder="Name"
                    value={form.name}
                    onChange={e => setForm({ ...form, name: e.target.value })}
                    required
                />

                <select
                    value={form.department}
                    onChange={e => setForm({ ...form, department: e.target.value })}
                    required
                >
                    <option value="">Select Department</option>
                    {departments.map(d => (
                        <option key={d} value={d}>{d}</option>
                    ))}
                </select>

                <button type="submit">Add Employee</button>
            </form>

            {role === "supervisor" && (
                <button className="download-btn" onClick={downloadPdf}>Download PDF</button>
            )}

            <button style={{ marginTop: "10px" }} onClick={addEmergencyMessage}>Add Emergency Message</button>

            <h2>Employee List</h2>
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Department</th>
                        <th>Machine</th>
                        <th>Status</th>
                        <th>Feedback</th>
                        <th>Timestamp</th>
                    </tr>
                </thead>
                <tbody>
                    {employees.map(emp => (
                        <tr key={emp.id}>
                            <td>{emp.name}</td>
                            <td>{emp.department}</td>
                            <td>{emp.machine}</td>
                            <td>
                                <select
                                    value={emp.status || 'Not Active'}
                                    onChange={e => handleUpdate(emp.id, e.target.value, emp.feedback)}
                                >
                                    <option value="Not Active">Not Active</option>
                                    <option value="Active">Active</option>
                                </select>
                            </td>
                            <td>
                                <textarea
                                    className="feedback-textarea"
                                    value={emp.feedback || ''}
                                    onChange={e => handleUpdate(emp.id, emp.status, e.target.value)}
                                />
                            </td>
                            <td>{new Date(emp.timestamp).toLocaleString()}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <div className="emergency-messages">
                <h3>Emergency Messages:</h3>
                {emergencyMessages.map((msg, i) => (
                    <div key={i} className="emergency-message-alert">{msg}</div>
                ))}
            </div>
        </div>
    );
}

export default App;
