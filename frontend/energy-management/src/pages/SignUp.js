import React, { useState } from "react";
import { useHistory } from "react-router-dom";
import axios from "axios";
import "./create.css";

function SignUp() {
  const [userData, setUserData] = useState({
    name: "",
    username: "",
    password: "",
    role: "CLIENT",
  });

  const history = useHistory();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserData({ ...userData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axios
      .post("http://localhost:8083/auth/signup", userData)
      .then((response) => {
        history.push("/");
      })
      .catch((error) => {
        console.error("Error creating user:", error);
      });
  };

  return (
    <div className="create">
      <h1>Sign Up</h1>
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
          <button type="submit">Create User</button>
        </div>
      </form>
    </div>
  );
}

export default SignUp;
