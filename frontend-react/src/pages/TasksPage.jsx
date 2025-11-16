import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import {
  getTasksForContact,
  createTask,
  updateTaskDetails,
  updateTaskStatus,
  deleteTask,
} from "../services/apiClient";

const TASK_STATUSES = ["OPEN", "IN_PROGRESS", "COMPLETED"];

function TasksPage() {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const { contactId } = useParams();

  const [formData, setFormData] = useState({ description: "", dueDate: "" });
  const [editingTaskId, setEditingTaskId] = useState(null);

  const fetchTasks = async () => {
    try {
      setLoading(true);
      setError("");
      const response = await getTasksForContact(contactId);
      setTasks(response.data);
    } catch (err) {
      setError("Failed to load tasks for this contact.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, [contactId]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const resetForm = () => {
    setFormData({ description: "", dueDate: "" });
    setEditingTaskId(null);
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      if (editingTaskId) {
        await updateTaskDetails(editingTaskId, formData);
      } else {
        await createTask({ ...formData, contactId: Number(contactId) });
      }
      resetForm();
      fetchTasks();
    } catch (err) {
      setError("Failed to save task.");
      console.error(err);
    }
  };

  const handleEditClick = (task) => {
    setEditingTaskId(task.id);
    setFormData({
      description: task.description,
      dueDate: task.dueDate || "",
    });
  };

  const handleDeleteClick = async (id) => {
    if (window.confirm("Are you sure you want to delete this task?")) {
      try {
        await deleteTask(id);
        fetchTasks();
      } catch (err) {
        setError("Failed to delete task.");
        console.error(err);
      }
    }
  };

  const handleStatusChange = async (taskId, newStatus) => {
    try {
      await updateTaskStatus(taskId, newStatus);
      setTasks((prevTasks) =>
        prevTasks.map((task) =>
          task.id === taskId ? { ...task, status: newStatus } : task
        )
      );
    } catch (err) {
      setError("Failed to update status.");
      console.error(err);
    }
  };

  if (loading) return <p>Loading tasks...</p>;

  return (
    <div>
      <h2>Tasks for Contact #{contactId}</h2>

      <h3>{editingTaskId ? "Edit Task" : "Create New Task"}</h3>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: "10px" }}>
          <label style={{ display: "block" }}>Description:</label>
          <input
            name="description"
            value={formData.description}
            onChange={handleInputChange}
            required
          />
        </div>
        <div style={{ marginBottom: "10px" }}>
          <label style={{ display: "block" }}>Due Date (YYYY-MM-DD):</label>
          <input
            name="dueDate"
            value={formData.dueDate}
            onChange={handleInputChange}
            type="date"
          />
        </div>
        <button type="submit">
          {editingTaskId ? "Update Details" : "Create Task"}
        </button>
        {editingTaskId && (
          <button
            type="button"
            onClick={resetForm}
            style={{ marginLeft: "10px" }}
          >
            Cancel
          </button>
        )}
      </form>
      {error && <p style={{ color: "red" }}>{error}</p>}

      <hr />

      <table border="1">
        <thead>
          <tr>
            <th>Description</th>
            <th>Due Date</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {tasks.map((task) => (
            <tr key={task.id}>
              <td>{task.description}</td>
              <td>{task.dueDate}</td>
              <td>
                <select
                  value={task.status}
                  onChange={(e) => handleStatusChange(task.id, e.target.value)}
                >
                  {TASK_STATUSES.map((status) => (
                    <option key={status} value={status}>
                      {status}
                    </option>
                  ))}
                </select>
              </td>
              <td>
                <button onClick={() => handleEditClick(task)}>
                  Edit Details
                </button>
                <button onClick={() => handleDeleteClick(task.id)}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default TasksPage;
