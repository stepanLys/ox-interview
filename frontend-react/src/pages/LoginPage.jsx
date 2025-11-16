import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';
import { useAuth } from '../context/AuthContext';

function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const navigate = useNavigate();
  const auth = useAuth();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await authService.login(email, password);

      if (response.data.token) {
        auth.login(response.data.token);

        navigate('/');
      }
    } catch (err) {
      setError('Wrong email or password');
      console.error('Login error:', err);
    }
  };

  return (
    <div>
      <h2>Login page</h2>
      <form onSubmit={handleLogin}>
        <div>
          <label>Email: </label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Password: </label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <br />
        <button type="submit">login</button>
        {error && <p style={{ color: 'red' }}>{error}</p>}
      </form>
    </div>
  );
}

export default LoginPage;