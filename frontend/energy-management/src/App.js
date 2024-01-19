import "./App.css";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import AdminNavbar from "./layouts/AdminNavbar";
import Admin from "./pages/Admin";
import CreateUser from "./pages/CreateUser";
import CreateDevice from "./pages/CreateDevice";
import MappingsManagement from "./pages/MappingsManagement";
import User from "./pages/User";
import Login from "./pages/Login";
import SignUp from "./pages/SignUp";
import PrivateAdminRoute from "./PrivateAdminRoute";
import PrivateUserRoute from "./PrivateUserRoute";
import Unauthorized from "./pages/Unauthorized";
import ChatPage from "./pages/ChatPage";

function App() {
  return (
    <Router>
      <div>
        <div className="content">
          <Switch>
            <Route exact path="/">
              <Login />
            </Route>
            <Route path="/unauthorized">
              <Unauthorized />
            </Route>
            <Route path="/signup">
              <SignUp />
            </Route>
            <Route path="/chat">
              <ChatPage />
            </Route>
            <PrivateUserRoute path="/user/:idUser" component={User} />
            <PrivateAdminRoute>
              <Route path="/admin" exact>
                <AdminNavbar />
                <Admin />
              </Route>
              <Route path="/admin/createUser">
                <AdminNavbar />
                <CreateUser />
              </Route>
              <Route path="/admin/createDevice">
                <AdminNavbar />
                <CreateDevice />
              </Route>
              <Route path="/admin/userDevices">
                <AdminNavbar />
                <div style={{ margin: "40px 0" }}>
                  <MappingsManagement />
                </div>
              </Route>
            </PrivateAdminRoute>
          </Switch>
        </div>
      </div>
    </Router>
  );
}

export default App;
