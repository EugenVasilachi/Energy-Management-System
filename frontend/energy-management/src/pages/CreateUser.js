import React, { useState, useRef } from "react";
import { useTokenStorage } from "../hooks/useTokenStorage";
import axios from "axios";
import "./create.css";

function CreateUser() {
  const [userData, setUserData] = useState({
    name: "",
    username: "",
    password: "",
    role: "CLIENT",
  });

  const { getToken } = useTokenStorage();
  const getTokenRef = useRef(getToken);

  const handleChange = (e) => {
    const { name, value } = e.target;
    const newValue = name === "role" ? String(value) : value;
    setUserData({ ...userData, [name]: newValue });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axios
      .post("http://localhost:8083/users", userData, {
        headers: {
          Authorization: `Bearer ${getTokenRef.current()}`,
        },
      })
      .then((response) => {
        console.log("User created:", response.data);
      })
      .catch((error) => {
        console.error("Error creating user:", error);
      });
  };

  return (
    <div className="create">
      <h1>Create User</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Name:</label>
          <input
            type="text"
            name="name"
            value={userData.name}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Username:</label>
          <input
            type="text"
            name="username"
            value={userData.username}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={userData.password}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Role:</label>
          <select
            name="role"
            value={userData.role}
            onChange={handleChange}
            required
          >
            <option value="CLIENT">CLIENT</option>
            <option value="ADMIN">ADMIN</option>
          </select>
        </div>
        <div>
          <button type="submit">Create User</button>
        </div>
      </form>
    </div>
  );
}

export default CreateUser;
