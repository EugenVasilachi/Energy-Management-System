import React from "react";
import UserManagement from "./UserManagement";
import DeviceManagement from "./DeviceManagement";
import "./admin.css";

function Admin() {
  return (
    <div className="admin-container">
      <div className="left-column">
        <UserManagement />
      </div>
      <div className="right-column">
        <DeviceManagement />
      </div>
    </div>
  );
}

export default Admin;
