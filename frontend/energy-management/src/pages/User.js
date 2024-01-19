import { useParams } from "react-router-dom";
import { useState, useEffect, useRef } from "react";
import { useTokenStorage } from "../hooks/useTokenStorage";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import axios from "axios";
import EnergyConsumptionChart from "./EnergyConsumptionChart";
import "./admin.css";
import "./create.css";
import "./chart.css";

const User = () => {
  const [devices, setDevices] = useState([]);
  const { idUser } = useParams();
  const [notification, setNotification] = useState("");
  const [showChart, setShowChart] = useState(false);

  const { getToken } = useTokenStorage();
  const getTokenRef = useRef(getToken);

  useEffect(() => {
    axios
      .get(`http://localhost:8081/userdevices/getDevicesForUser/${idUser}`, {
        headers: {
          Authorization: `Bearer ${getTokenRef.current()}`,
        },
      })
      .then((response) => {
        setDevices(response.data);
      })
      .catch((error) => {
        console.error("Error fetching UserDevice data:", error);
      });

    const handleOpenSocket = () => {
      const sock = new SockJS("http://localhost:8082/notify-high-consumption");
      sock.onopen = () => {
        console.log("open socket");
      };

      sock.onmessage = (e) => {
        sock.close();
        //setMessage(JSON.parse(e.body));
      };

      sock.onclose = () => {
        console.log("close");
      };

      const stompcli = Stomp.over(sock);

      stompcli.connect({}, (frame) => {
        stompcli.subscribe("/broker/notify-high-consumption", (res) => {
          const parsedRes = JSON.parse(res.body);
          if (localStorage.idUser === parsedRes.idUser) {
            setNotification(
              `Device ${parsedRes.description} exceeded the maximum hourly consumption.`
            );
          }
        });
      });
    };

    handleOpenSocket();
  }, [idUser]);

  const handleCloseNotification = () => {
    setNotification("");
  };

  return (
    <div className="div-mapping">
      <div className="show-mapping">
        <h1>Associated Devices</h1>
        <table className="table-mapping">
          <thead>
            <tr>
              <th>Description</th>
              <th>Address</th>
              <th>Maximum Hourly Energy Consumption</th>
            </tr>
          </thead>
          <tbody>
            {devices.map((device) => (
              <tr key={device.id}>
                <td>{device.description}</td>
                <td>{device.address}</td>
                <td>{device.maximumHourlyEnergyConsumption}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {notification && (
        <div className="notification">
          <p>{notification}</p>
          <button onClick={handleCloseNotification}>Close</button>
        </div>
      )}
      <div className="button-container">
        <button
          className="show-chart-button"
          onClick={() => setShowChart(!showChart)}
        >
          {showChart ? "Hide Chart" : "Show Chart"}
        </button>
      </div>
      {showChart && <EnergyConsumptionChart />}
    </div>
  );
};

export default User;
