import React, { useState, useEffect, useRef } from "react";
import { useTokenStorage } from "../hooks/useTokenStorage";
import axios from "axios";
import "./admin.css";
import "./create.css";

function MappingsManagement() {
  const [mappings, setMappings] = useState([]);
  const [selectedAddButton, setSelectedAddButon] = useState(false);
  const [mappingData, setMappingData] = useState({
    idUser: "",
    idDevice: "",
  });

  const { getToken } = useTokenStorage();
  const getTokenRef = useRef(getToken);

  useEffect(() => {
    fetchUserDevices();
  }, []);

  const fetchUserDevices = () => {
    axios
      .get("http://localhost:8081/userdevices", {
        headers: {
          Authorization: `Bearer ${getTokenRef.current()}`,
        },
      })
      .then((response) => setMappings(response.data))
      .catch((error) => console.error("Error fetching userdevices:", error));
  };

  const handleDeleteMapping = (mapping) => {
    axios
      .delete(`http://localhost:8081/userdevices/${mapping.idUserDevice}`, {
        headers: {
          Authorization: `Bearer ${getTokenRef.current()}`,
        },
      })
      .then(() => {
        // After a successful delete, fetch the updated device list
        fetchUserDevices();
      })
      .catch((error) => console.error("Error deleting userdevice:", error));
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setMappingData({ ...mappingData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axios
      .post("http://localhost:8081/userdevices", mappingData, {
        headers: {
          Authorization: `Bearer ${getTokenRef.current()}`,
        },
      })
      .then((response) => {
        console.log("Mapping created:", response.data);
        fetchUserDevices();
      })
      .catch((error) => {
        console.error("Error creating mapping:", error);
      });
  };

  return (
    <div className="div-mapping">
      <div className="show-mapping">
        <h1>Mappings Management</h1>
        <table className="table-mapping">
          <thead>
            <tr>
              <th>User ID</th>
              <th>Device ID</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {mappings.map((mapping) => (
              <tr key={mapping.idUserDevice}>
                <td>{mapping.idUser}</td>
                <td>{mapping.idDevice}</td>
                <td>
                  <button
                    className="delete-button"
                    onClick={() => handleDeleteMapping(mapping)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <br />
        <br />
        <br />
        <button
          className="add-button"
          onClick={() => {
            setSelectedAddButon(true);
          }}
        >
          Add mapping
        </button>
        {selectedAddButton && (
          <div className="create">
            <h1>Create Mapping</h1>
            <form onSubmit={handleSubmit}>
              <div>
                <label>User ID:</label>
                <input
                  type="text"
                  name="idUser"
                  value={mappingData.idUser}
                  onChange={handleChange}
                  required
                />
              </div>
              <div>
                <label>Device ID:</label>
                <input
                  type="text"
                  name="idDevice"
                  value={mappingData.idDevice}
                  onChange={handleChange}
                  required
                />
              </div>
              <div>
                <button type="submit" style={{ background: "#35f1b9" }}>
                  Bind
                </button>
              </div>
            </form>
          </div>
        )}
      </div>
    </div>
  );
}

export default MappingsManagement;
