export default function SupervisorLogin() {
  return (
    <div style={{ padding: 50 }}>
      <h2>Supervisor Login</h2>

      <form action="http://localhost:8080/Supervisor/login" method="POST">
        <input name="username" placeholder="Username" required />
        <input name="password" type="password" placeholder="Password" required />
        <button type="submit">Login</button>
      </form>
    </div>
  );
}
