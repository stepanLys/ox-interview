import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import socketService from "../services/socketService";

function DashboardPage() {
  const auth = useAuth();
  const [notifications, setNotifications] = useState([]);
  const formatNotificationText = (message) => {
    switch (message.eventType) {
      case "TASK_STATUS_CHANGED":
        return `Task #${message.taskId} status changed to: ${message.newStatus}`;
      case "TASK_DEADLINE":
        return `DEADLINE: Task #${message.taskId} is due tomorrow (${message.dueDate})!`;
      default:
        return "New uncategorized notification.";
    }
  };

  useEffect(() => {
    const onMessageReceived = (message) => {
      const key = message.taskId + message.eventType;

      setNotifications((prev) => {
        const newMessage = { ...message, timestamp: Date.now() };

        const existingIndex = prev.findIndex(
          (n) => n.taskId + n.eventType === key
        );

        if (existingIndex !== -1) {
          const newArray = [...prev];
          newArray[existingIndex] = newMessage;
          return newArray;
        } else {
          return [newMessage, ...prev];
        }
      });
    };

    socketService.connect(auth.token, onMessageReceived);

    return () => {
      socketService.disconnect();
    };
  }, [auth.token]);

  return (
    <div>
      <h2>Dashboard (Protected)</h2>
      <p>Welcome, {auth.user?.sub}!</p>
      <button onClick={auth.logout}>Log Out</button>

      <hr />
      <h3>Real-time Notifications:</h3>
      <ul
        style={{
          maxHeight: "200px",
          overflowY: "auto",
          border: "1px solid #ccc",
        }}
      >
        {notifications.length === 0 && <p>No notifications yet...</p>}
        {notifications.map(msg => (
          <li key={msg.id}>{formatNotificationText(msg)}</li>
        ))}
      </ul>
    </div>
  );
}

export default DashboardPage;
