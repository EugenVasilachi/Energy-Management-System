import React, { useState, useEffect, useRef } from "react";
import { useTokenStorage } from "../hooks/useTokenStorage";
import axios from "axios";

function DeviceManagement() {
  const [devices, setDevices] = useState([]);
  const [selectedDevice, setSelectedDevice] = useState(null);
  const [newDeviceData, setNewDeviceData] = useState({
    description: "",
    address: "",
    maximumHourlyEnergyConsumption: "",
  });

  const { getToken } = useTokenStorage();
  const getTokenRef = useRef(getToken);

  useEffect(() => {
    fetchDevices();
  }, []);

  const fetchDevices = () => {
    axios
      .get("http://localhost:8081/devices", {
        headers: {
          Authorization: `Bearer ${getTokenRef.current()}`,
        },
      })
      .then((response) => setDevices(response.data))
      .catch((error) => console.error("Error fetching devices:", error));
  };

  const handleUpdateDevice = (device) => {
    setSelectedDevice(device);
    setNewDeviceData({
      description: device.description,
      address: device.address,
      maximumHourlyEnergyConsumption: device.maximumHourlyEnergyConsumption,
    });
  };

  const handleDeleteDevice = (device) => {
    axios
      .delete(`http://localhost:8081/devices/${device.id}`, {
        headers: {
          Authorization: `Bearer ${getTokenRef.current()}`,
        },
      })
      .then(() => {
        // After a successful delete, fetch the updated device list
        fetchDevices();
      })
      .catch((error) => console.error("Error deleting device:", error));
  };

  const handleUpdateDeviceData = () => {
    // Make an API request to update the device data
    axios
      .put(
        `http://localhost:8081/devices/${selectedDevice.id}`,
        newDeviceData,
        {
          headers: {
            Authorization: `Bearer ${getTokenRef.current()}`,
          },
        }
      )
      .then((response) => {
        const updatedDevice = response.data;
        setDevices(
          devices.map((device) =>
            device.id === updatedDevice.id ? updatedDevice : device
          )
        );
        setSelectedDevice(null);
      })
      .catch((error) => console.error("Error updating device data:", error))
      .finally(() => {
        // After a successful update, fetch the updated device list
        fetchDevices();
      });
  };

  return (
    <div>
      <h1>Device Management</h1>
      <table className="table-device">
        <thead>
          <tr>
            <th>Description</th>
            <th>Address</th>
            <th>Maximum Hourly Energy Consumption</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {devices.map((device) => (
            <tr key={device.id}>
              <td>{device.description}</td>
              <td>{device.address}</td>
              <td>{device.maximumHourlyEnergyConsumption}</td>
              <td>
                <button
                  className="update-button"
                  onClick={() => handleUpdateDevice(device)}
                >
                  Update
                </button>
                <button
                  className="delete-button"
                  onClick={() => handleDeleteDevice(device)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {selectedDevice && (
        <div>
          <h2>Update Device</h2>
          <input
            className="detail-input"
            type="text"
            placeholder="Description"
            value={newDeviceData.description}
            onChange={(e) =>
              setNewDeviceData({
                ...newDeviceData,
                description: e.target.value,
              })
            }
          />
          <input
            className="detail-input"
            type="text"
            placeholder="Address"
            value={newDeviceData.address}
            onChange={(e) =>
              setNewDeviceData({ ...newDeviceData, address: e.target.value })
            }
          />
          <input
            className="detail-input"
            type="text"
            placeholder="Maximum Hourly Energy Consumption"
            value={newDeviceData.maximumHourlyEnergyConsumption}
            onChange={(e) =>
              setNewDeviceData({
                ...newDeviceData,
                maximumHourlyEnergyConsumption: e.target.value,
              })
            }
          />
          <button className="update-button" onClick={handleUpdateDeviceData}>
            Update Device Data
          </button>
        </div>
      )}
    </div>
  );
}

export default DeviceManagement;
