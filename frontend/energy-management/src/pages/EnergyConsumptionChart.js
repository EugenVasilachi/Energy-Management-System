import { Chart, registerables } from "chart.js";
import { useState, useEffect, useRef } from "react";
import { useTokenStorage } from "../hooks/useTokenStorage";
import axios from "axios";
import { Line } from "react-chartjs-2";
import "./chart.css";

Chart.register(...registerables);

const EnergyConsumptionChart = () => {
  const [chartData, setChartData] = useState(null);
  const [legendItems, setLegendItems] = useState([]);

  const { getToken } = useTokenStorage();
  const getTokenRef = useRef(getToken);

  useEffect(() => {
    const idUser = localStorage.idUser;

    axios
      .get(`http://localhost:8082/energy-consumption/${idUser}`, {
        headers: {
          Authorization: `Bearer ${getTokenRef.current()}`,
        },
      })
      .then((response) => {
        const data = [];
        const legends = [];

        // Iterate over the response data and add datasets for non-empty lists
        for (const [deviceId, energyConsumptions] of Object.entries(
          response.data
        )) {
          if (energyConsumptions.length > 0) {
            const dataset = {
              label: `Device ${deviceId}`,
              data: energyConsumptions.map((entry) => ({
                x: new Date(entry.timestamp).getHours(),
                y: entry.value,
              })),
            };
            data.push(dataset);

            // Add legend item
            legends.push({
              id: deviceId,
              label: `Device ${deviceId}`,
            });
          }
        }

        setChartData(data);
        setLegendItems(legends);
      })
      .catch((error) => {
        console.error("Error fetching energy consumption data:", error);
      });
  }, []);

  return (
    <div className="chart-container">
      <h2 className="chart-title">Energy Consumption Chart</h2>
      <div className="chart">
        {chartData && (
          <Line
            data={{
              datasets: chartData,
            }}
            options={{
              scales: {
                x: {
                  type: "linear",
                  position: "bottom",
                  title: {
                    display: true,
                    text: "Hours",
                  },
                },
                y: {
                  title: {
                    display: true,
                    text: "Energy Value (kWh)",
                  },
                },
              },
            }}
          />
        )}
      </div>
      <div className="legend">
        {legendItems.map((item) => (
          <div key={item.id} className="legend-item">
            <span>{item.label}</span>
          </div>
        ))}
      </div>
    </div>
  );
};

export default EnergyConsumptionChart;
