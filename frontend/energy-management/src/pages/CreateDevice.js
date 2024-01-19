import React, { useState } from "react";
import axios from "axios";
import "./create.css";

function CreateDevice() {
  const [deviceData, setDeviceData] = useState({
    description: "",
    address: "",
    maximumHourlyEnergyConsumption: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setDeviceData({ ...deviceData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Make a POST request to create a new device
    axios
      .post("http://localhost:8081/devices", deviceData)
      .then((response) => {
        console.log("Device created:", response.data);
        // You can add a success message or redirect to another page here
      })
      .catch((error) => {
        console.error("Error creating device:", error);
        // Handle the error, e.g., display an error message
      });
  };

  return (
    <div className="create">
      <h1>Create Device</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Description:</label>
          <input
            type="text"
            name="description"
            value={deviceData.description}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Address:</label>
          <input
            type="text"
            name="address"
            value={deviceData.address}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Maximum Hourly Energy Consumption:</label>
          <input
            type="number"
            name="maximumHourlyEnergyConsumption"
            value={deviceData.maximumHourlyEnergyConsumption}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <button type="submit">Create Device</button>
        </div>
      </form>
    </div>
  );
}

export default CreateDevice;
