import { BrowserRouter, Routes, Route } from "react-router-dom";
import MainPage from "./MainPage";
import SupervisorLogin from "./SupervisorLogin";

export default function App() {
  return (
    <BrowserRouter basename="/PM-Scheduler">
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/Supervisor-login" element={<SupervisorLogin />} />
      </Routes>
    </BrowserRouter>
  );
}
