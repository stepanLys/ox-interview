import axios from "axios";

const API_URL = "http://localhost:8080/api/";

const apiClient = axios.create({
  baseURL: API_URL,
});

apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("userToken");
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export const getAllClients = (searchParams = {}) => {
  return apiClient.post("/clients/search", searchParams);
};

export const createClient = (clientData) => {
  return apiClient.post("/clients", clientData);
};

export const updateClient = (id, clientData) => {
  return apiClient.put(`/clients/${id}`, clientData);
};

export const deleteClient = (id) => {
  return apiClient.delete(`/clients/${id}`);
};

export const createContact = (contactData) => {
  return apiClient.post("/contacts", contactData);
};

export const getContactsForClient = (clientId, searchParams = {}) => {
  return apiClient.post(`/contacts/client/${clientId}`, searchParams);
};

export const updateContact = (id, contactData) => {
  return apiClient.put(`/contacts/${id}`, contactData);
};

export const deleteContact = (id) => {
  return apiClient.delete(`/contacts/${id}`);
};

export const getTasksForContact = (contactId) => {
  return apiClient.get(`/tasks/contact/${contactId}`);
};

export const createTask = (taskData) => {
  return apiClient.post("/tasks", taskData);
};

export const updateTaskDetails = (id, taskData) => {
  return apiClient.put(`/tasks/${id}`, taskData);
};

export const updateTaskStatus = (id, status) => {
  return apiClient.patch(`/tasks/${id}/status`, { status });
};

export const deleteTask = (id) => {
  return apiClient.delete(`/tasks/${id}`);
};

export const exportClients = (criteria, format) => {
  return apiClient.post(`/clients/export?format=${format}`, criteria, {
    responseType: "blob",
  });
};
