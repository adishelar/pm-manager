export default function SupervisorLogin() {
  return (
    <div style={{ padding: 50 }}>
      <h2>Supervisor Login</h2>

      <form action="http://https://pm-manager-y8b1.onrender.com/api/endpoint/Supervisor/login" method="POST">
        <input name="username" placeholder="Username" required />
        <input name="password" type="password" placeholder="Password" required />
        <button type="submit">Login</button>
      </form>
    </div>
  );
}
