import React from "react";
import { Routes, Route, Link } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import DashboardPage from "./pages/DashboardPage";
import ProtectedRoute from "./components/ProtectedRoute";
import ClientsPage from "./pages/ClientsPage";
import ContactsPage from "./pages/ContactsPage";
import TasksPage from "./pages/TasksPage";

function App() {
  return (
    <div>
      <h1>CRM App</h1>
      <nav>
        <Link to="/">Dashboard</Link> | <Link to="/clients">Clients</Link> |{" "}
        <Link to="/login">Login</Link>
      </nav>
      <hr />

      <Routes>
        <Route path="/login" element={<LoginPage />} />

        <Route
          path="/"
          element={
            <ProtectedRoute>
              <DashboardPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/clients"
          element={
            <ProtectedRoute>
              <ClientsPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/client/:clientId/contacts"
          element={
            <ProtectedRoute>
              <ContactsPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/contacts/:contactId/tasks"
          element={
            <ProtectedRoute>
              <TasksPage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </div>
  );
}

export default App;
