import { useHistory } from "react-router-dom";
import { useTokenStorage } from "../hooks/useTokenStorage";

const UserNavbar = () => {
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
      <h1 className="admin-link">User Page</h1>
      <button onClick={handleChat}>Chat</button>
      <button onClick={handleLogout}>Log Out</button>
    </nav>
  );
};

export default UserNavbar;
