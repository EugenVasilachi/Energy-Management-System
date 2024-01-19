import React, { useState, useEffect, useRef } from "react";
import { useTokenStorage } from "../hooks/useTokenStorage";
import axios from "axios";

function UserManagement() {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [newUserData, setNewUserData] = useState({
    name: "",
    username: "",
    password: "",
  });

  const { getToken } = useTokenStorage();
  const getTokenRef = useRef(getToken);

  useEffect(() => {
    axios
      .get("http://localhost:8083/users", {
        headers: {
          Authorization: `Bearer ${getTokenRef.current()}`,
        },
      })
      .then((response) => setUsers(response.data))
      .catch((error) => console.error("Error fetching users:", error));
  }, [getTokenRef]);

  const handleUpdateUser = (user) => {
    setSelectedUser(user);
    setNewUserData({
      name: user.name,
      username: user.username,
      password: user.password,
    });
  };

  const handleDeleteUser = (user) => {
    axios
      .delete(`http://localhost:8083/users/${user.id}`, {
        headers: {
          Authorization: `Bearer ${getTokenRef.current()}`,
        },
      })
      .then(() => {
        setUsers(users.filter((u) => u.id !== user.id));
      })
      .catch((error) => console.error("Error deleting user:", error));
  };

  const handleUpdateUserData = () => {
    axios
      .put(`http://localhost:8083/users/${selectedUser.id}`, newUserData, {
        headers: {
          Authorization: `Bearer ${getTokenRef.current()}`,
        },
      })
      .then((response) => {
        const updatedUser = response.data;
        setUsers(
          users.map((user) => (user.id === updatedUser.id ? updatedUser : user))
        );
        setSelectedUser(null);
      })
      .catch((error) => console.error("Error updating user data:", error));
  };

  return (
    <div>
      <h1>User Management</h1>
      <table className="table-user">
        <thead>
          <tr>
            <th>Name</th>
            <th>Username</th>
            <th>Password</th>
            <th>Role</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.name}</td>
              <td>{user.username}</td>
              <td>{user.password}</td>
              <td>{user.role}</td>
              <td>
                <button
                  className="update-button"
                  onClick={() => handleUpdateUser(user)}
                >
                  Update
                </button>
                <button
                  className="delete-button"
                  onClick={() => handleDeleteUser(user)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {selectedUser && (
        <div>
          <h2>Update User</h2>
          <input
            className="detail-input"
            type="text"
            placeholder="Name"
            value={newUserData.name}
            onChange={(e) =>
              setNewUserData({ ...newUserData, name: e.target.value })
            }
          />
          <input
            className="detail-input"
            type="text"
            placeholder="Username"
            value={newUserData.username}
            onChange={(e) =>
              setNewUserData({ ...newUserData, username: e.target.value })
            }
          />
          <input
            className="detail-input"
            type="text"
            placeholder="Password"
            value={newUserData.password}
            onChange={(e) =>
              setNewUserData({ ...newUserData, password: e.target.value })
            }
          />
          <button className="update-button" onClick={handleUpdateUserData}>
            Update User Data
          </button>
        </div>
      )}
    </div>
  );
}

export default UserManagement;
