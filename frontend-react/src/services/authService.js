import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth/';

const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

const register = (email, password) => {
  return apiClient.post('register', {
    email,
    password,
  });
};

const login = (email, password) => {
  return apiClient.post('login', {
    email,
    password,
  });
};

const logout = () => {
  localStorage.removeItem('userToken');
};

const authService = {
  register,
  login,
  logout,
};

export default authService;