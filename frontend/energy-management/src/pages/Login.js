import React, { useState } from "react";
import { useHistory } from "react-router-dom";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { useTokenStorage } from "../hooks/useTokenStorage";
import "./login.css";
import "./create.css";
import "./admin.css";

function Login() {
  const [userData, setUserData] = useState({
    username: "",
    password: "",
    role: "CLIENT",
  });

  const [errorEncountered, setErrorEncountered] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserData({ ...userData, [name]: value });
    setErrorEncountered(false);
  };

  const history = useHistory();

  const { saveToken } = useTokenStorage();

  const handleSubmit = (e) => {
    e.preventDefault();
    axios
      .post("http://localhost:8083/auth/login", userData)
      .then((response) => {
        const token = response.data.token;
        saveToken(token);
        const decodedToken = jwtDecode(token);
        const role = String(decodedToken.role);
        const idUser = decodedToken.id;
        const name = decodedToken.name;

        localStorage.setItem("role", role);
        localStorage.setItem("idUser", idUser);
        localStorage.setItem("name", name);

        console.log("Login successfully:", role);
        if (role === "ADMIN") {
          history.push("/admin");
        } else {
          history.push("/user/" + idUser);
        }
      })
      .catch((error) => {
        setErrorEncountered(true);
      });
  };

  const handleSignUp = (e) => {
    e.preventDefault();
    history.push("/signup");
  };

  return (
    <div className="create">
      <h1>Welcome</h1>
      <form onSubmit={handleSubmit} className="login-form">
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
          <button type="submit" className="signup-button">
            Login
          </button>
        </div>
        {errorEncountered && (
          <div className="error-message">Invalid username or password</div>
        )}
        <br />
        <div className="signup-container">
          <h3 className="signup-heading">Don't have an account yet?</h3>
          <button className="signup-button" onClick={handleSignUp}>
            Sign Up
          </button>
        </div>
      </form>
    </div>
  );
}

export default Login;
