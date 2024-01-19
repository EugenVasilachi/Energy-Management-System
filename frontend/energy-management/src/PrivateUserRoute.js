import React from "react";
import { Route, Redirect } from "react-router-dom";
import UserNavbar from "./layouts/UserNavbar";

const PrivateUserRoute = ({ component: Component, ...rest }) => {
  const storedIdUser = localStorage.getItem("idUser");

  console.log(storedIdUser);

  return (
    <Route
      {...rest}
      render={(props) => {
        const urlIdUser = props.match.params.idUser;
        if (urlIdUser === storedIdUser) {
          return (
            <div>
              <UserNavbar />
              <Component {...props} />
            </div>
          );
        } else {
          return <Redirect to="/unauthorized" />;
        }
      }}
    />
  );
};

export default PrivateUserRoute;
