import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./App.css";

function MainPage() {
  const [employees, setEmployees] = useState([]);
  const [form, setForm] = useState({ name: "", department: "" });
  const [emergencyMessages, setEmergencyMessages] = useState([]);
  const [role, setRole] = useState("operator");
  const [selectedDate, setSelectedDate] = useState("");
  const navigate = useNavigate();

  const departments = ["A", "B", "C", "D"];

  // Detect supervisor after login
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    if (params.get("role") === "Supervisor") {
      setRole("Supervisor");
    }
  }, []);

  // Load all employees on first load
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/employees")
      .then((res) => setEmployees(res.data))
      .catch((err) => console.error(err));
  }, []);

  // Add employee
  const handleSubmit = (e) => {
    e.preventDefault();
    axios
      .post("http://localhost:8080/api/employees", form)
      .then((res) => {
        setEmployees([...employees, res.data]);
        setForm({ name: "", department: "" });
      })
      .catch((err) => console.error(err));
  };

  // Update employee
  const handleUpdate = (id, status, feedback) => {
    axios
      .put(`http://localhost:8080/api/employees/${id}`, { status, feedback })
      .then((res) =>
        setEmployees(
          employees.map((emp) => (emp.id === id ? res.data : emp))
        )
      )
      .catch((err) => console.error(err));
  };

  // Download full PDF
  const downloadPdf = () => {
    axios
      .get("http://localhost:8080/api/employees/pdf", {
        responseType: "blob",
        headers: { role },
      })
      .then((res) => {
        const url = URL.createObjectURL(new Blob([res.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "employees.pdf");
        document.body.appendChild(link);
        link.click();
      })
      .catch(() => alert("Only Supervisor can download PDF!"));
  };

  // Add emergency message
  const addEmergencyMessage = () => {
    const msg = prompt("Enter Emergency Message:");
    if (msg) setEmergencyMessages([...emergencyMessages, msg]);
  };

  // Fetch employees by date
  const fetchDataByDate = (date) => {
    setSelectedDate(date);
    if (!date) return;

    axios
      .get(`http://localhost:8080/api/employees/by-date?date=${date}`)
      .then((res) => setEmployees(res.data))
      .catch((err) => console.error(err));
  };

  // Download PDF by date
  const downloadPdfByDate = () => {
    if (!selectedDate) {
      alert("Select a date first!");
      return;
    }

    axios
      .get(
        `http://localhost:8080/api/employees/pdf-by-date?date=${selectedDate}`,
        {
          responseType: "blob",
          headers: { role },
        }
      )
      .then((res) => {
        const url = URL.createObjectURL(new Blob([res.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `employees-${selectedDate}.pdf`);
        document.body.appendChild(link);
        link.click();
      })
      .catch(() => alert("Only Supervisor can download PDF!"));
  };

  return (
    <div className="App">
      <h1>Predictive Maintenance Scheduler</h1>

      {/* ROLE SELECT */}
      <div>
        <label>Select Role: </label>
        <select
          value={role}
          onChange={(e) => {
            setRole(e.target.value);
            if (e.target.value === "Supervisor")
              navigate("/Supervisor-login");
          }}
        >
          <option value="operator">Operator</option>
          <option value="Supervisor">Supervisor</option>
        </select>
      </div>

      {/* EMPLOYEE FORM */}
      <form onSubmit={handleSubmit}>
        <input
          placeholder="Name"
          value={form.name}
          onChange={(e) => setForm({ ...form, name: e.target.value })}
          required
        />

        <select
          value={form.department}
          onChange={(e) =>
            setForm({ ...form, department: e.target.value })
          }
          required
        >
          <option value="">Select Department</option>
          {departments.map((d) => (
            <option key={d} value={d}>
              {d}
            </option>
          ))}
        </select>

        <button type="submit">Add Employee</button>
      </form>

      {/* SUPERVISOR FEATURES */}
      {role === "Supervisor" && (
        <>
          <button onClick={downloadPdf}>Download PDF (All Data)</button>

          {/* DATE FILTER */}
          <div style={{ marginTop: "20px" }}>
            <label>Select Date: </label>
            <input
              type="date"
              onChange={(e) => fetchDataByDate(e.target.value)}
            />
            <button onClick={downloadPdfByDate}>Download PDF by Date</button>
          </div>
        </>
      )}

      {/* EMERGENCY MESSAGES */}
      <div style={{ marginTop: "20px" }}>
        <button onClick={addEmergencyMessage}>Add Emergency Message</button>

        <h3 style={{ color: "red", fontWeight: "bold" }}>
          Emergency Messages:
        </h3>

        {emergencyMessages.map((msg, i) => (
          <div
            key={i}
            style={{
              color: "white",
              backgroundColor: "red",
              padding: "8px",
              marginBottom: "5px",
              fontWeight: "bold",
              borderRadius: "5px",
            }}
          >
            ⚠️ {msg}
          </div>
        ))}
      </div>

      {/* EMPLOYEE TABLE */}
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
          {employees.map((emp) => (
            <tr key={emp.id}>
              <td>{emp.name}</td>
              <td>{emp.department}</td>
              <td>{emp.machine}</td>
              <td>
                <select
                  value={emp.status || "Not Active"}
                  onChange={(e) =>
                    handleUpdate(emp.id, e.target.value, emp.feedback)
                  }
                >
                  <option value="Not Active">Not Active</option>
                  <option value="Active">Active</option>
                </select>
              </td>

              <td>
                <textarea
                  value={emp.feedback || ""}
                  onChange={(e) =>
                    handleUpdate(emp.id, emp.status, e.target.value)
                  }
                />
              </td>

              <td>{new Date(emp.timestamp).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default MainPage;
