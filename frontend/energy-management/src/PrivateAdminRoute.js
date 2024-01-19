import React from "react";
import { Route, Redirect } from "react-router-dom";

const PrivateAdminRoute = ({ children }) => {
  const role = localStorage.getItem("role");

  return (
    <Route
      render={({ location }) =>
        role === "ADMIN" ? (
          children
        ) : (
          <Redirect
            to={{
              pathname: "/",
              state: { from: location },
            }}
          />
        )
      }
    />
  );
};

export default PrivateAdminRoute;
