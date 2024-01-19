import { Link, useHistory } from "react-router-dom";
import { useTokenStorage } from "../hooks/useTokenStorage";

const AdminNavbar = () => {
  const { clearToken } = useTokenStorage();
  const history = useHistory();

  const handleLogout = () => {
    clearToken();
    localStorage.removeItem("role");
    localStorage.removeItem("idUser");
    history.push("/");
  };

  const handleChat = () => {
    history.push("/chat");
  };

  return (
    <nav className="navbar">
      <Link to="/admin" className="admin-link">
        <h1>Admin Page</h1>
      </Link>
      <Link to="/admin/createUser">Create User</Link>
      <Link to="/admin/createDevice">Create Device</Link>
      <Link to="/admin/userDevices">Mappings</Link>
      <button onClick={handleChat}>Chat</button>
      <button onClick={handleLogout}>Log Out</button>
    </nav>
  );
};

export default AdminNavbar;
