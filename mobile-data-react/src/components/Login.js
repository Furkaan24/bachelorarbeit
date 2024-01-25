import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './css/pages.css';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const response = await fetch('http://localhost:8080/mobile-data-web/connect', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Basic ' + btoa(`${username}:${password}`),
        },
      });

      if (response.ok) {
        setMessage('Authentication successful');
        navigate('/search');
      } else {
        setMessage('Authentication failed');
      }
    } catch (error) {
      console.error('Error during authentication:', error);
      setMessage('An error occurred during authentication');
    }
  };

  return (
    <div className="form">
      <label>
        Username:
        <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
      </label>
      <br />
      <label>
        Password:
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
      </label>
      <br />
      <button className="button-search" onClick={handleLogin}>Login</button>
      <br />
      <p>{message}</p>
    </div>
  );
};

export default Login;