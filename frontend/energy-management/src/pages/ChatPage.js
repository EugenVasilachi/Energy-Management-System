import React, { useState } from "react";
import { over } from "stompjs";
import SockJS from "sockjs-client";
import "./chatpage.css";

var stompClient = null;

const ChatRoom = () => {
  const [publicChats, setPublicChats] = useState([]);
  const [privateChats, setPrivateChats] = useState(new Map());
  const [tab, setTab] = useState("CHATROOM");
  const [userIsTyping, setUserIsTyping] = useState({
    isTyping: false,
    sender: "",
  });
  const [userData, setUserData] = useState({
    username: "",
    receiverName: "",
    connected: false,
    content: "",
  });

  const connectUser = () => {
    let Sock = new SockJS("http://localhost:8084/ws");
    stompClient = over(Sock);
    stompClient.connect({}, onConnected, onError);
  };

  const onConnected = () => {
    setUserData({ ...userData, connected: true, username: localStorage.name });
    stompClient.subscribe("/chatroom/public", onPublicMessageReceived);
    stompClient.subscribe(
      "/user/" + localStorage.name + "/private",
      onPrivateMessageReceived
    );
    userJoin();
  };

  const onError = (err) => {
    console.log(err);
  };

  const handleTyping = (senderName) => {
    setUserIsTyping({ isTyping: true, sender: senderName });

    setTimeout(() => {
      setUserIsTyping({ ...userIsTyping, isTyping: false });
    }, 2000);
  };

  const handleMessage = (event) => {
    const { value } = event.target;
    setUserData({ ...userData, content: value });
  };

  const handlePublicMessage = (event) => {
    handleMessage(event);
    handleTyping(userData.username);
  };

  const handleKeyDown = (event, sendMessageFunction) => {
    if (event.key === "Enter") {
      sendMessageFunction();
    }
  };

  const userJoin = () => {
    var chatMessage = {
      senderName: localStorage.name,
      status: "JOIN",
    };
    stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
  };

  const onPublicMessageReceived = (payload) => {
    let payloadData = JSON.parse(payload.body);
    switch (payloadData.status) {
      case "JOIN":
        if (!privateChats.get(payloadData.senderName)) {
          privateChats.set(payloadData.senderName, []);
          setPrivateChats(new Map(privateChats));
        }
        break;
      case "MESSAGE":
        publicChats.push(payloadData);
        setPublicChats([...publicChats]);
        break;
      case "TYPING":
        handleTyping(payloadData.senderName);
        break;
      default:
        break;
    }
  };

  const onPrivateMessageReceived = (payload) => {
    let payloadData = JSON.parse(payload.body);
    if (privateChats.get(payloadData.senderName)) {
      privateChats.get(payloadData.senderName).push(payloadData);
      setPrivateChats(new Map(privateChats));
    } else {
      let list = [];
      list.push(payloadData);

      privateChats.set(payloadData.senderName, list);
      setPrivateChats(new Map(privateChats));
    }
  };

  const sendPublicMessage = () => {
    if (stompClient) {
      var chatMessage = {
        senderName: userData.username,
        content: userData.content,
        status: "MESSAGE",
      };
      stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
      setUserData({ ...userData, content: "" });
    }
  };

  const sendPrivateMessage = () => {
    if (stompClient) {
      var chatMessage = {
        senderName: userData.username,
        receiverName: tab,
        content: userData.content,
        status: "MESSAGE",
      };

      if (userData.username !== tab) {
        privateChats.get(tab).push(chatMessage);
        setPrivateChats(new Map(privateChats));
      }
      stompClient.send("/app/private-message", {}, JSON.stringify(chatMessage));
      setUserData({ ...userData, content: "" });
    }
  };

  const sendTypingMessage = () => {
    if (stompClient) {
      var chatMessage = {
        senderName: userData.username,
        status: "TYPING",
      };
      stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
    }
  };

  return (
    <div className="container">
      {userData.connected ? (
        <div className="chat-box">
          <div className="member-list">
            <ul>
              <li
                onClick={() => {
                  setTab("CHATROOM");
                }}
                className={`member ${tab === "CHATROOM" && "active"}`}
              >
                Chatroom
              </li>
              {[...privateChats.keys()].map((name, index) => (
                <li
                  onClick={() => {
                    setTab(name);
                  }}
                  className={`member ${tab === name && "active"}`}
                  key={index}
                >
                  {name}
                </li>
              ))}
            </ul>
          </div>
          {tab === "CHATROOM" && (
            <div className="chat-content">
              <ul className="chat-messages">
                {publicChats.map((chat, index) => (
                  <li
                    className={`message ${
                      chat.senderName === userData.username && "self"
                    }`}
                    key={index}
                  >
                    {chat.senderName !== userData.username && (
                      <div className="avatar">{chat.senderName}</div>
                    )}
                    <div className="message-data">{chat.content}</div>
                    {chat.senderName === userData.username && (
                      <div className="avatar self">{chat.senderName}</div>
                    )}
                  </li>
                ))}
              </ul>

              {userIsTyping.isTyping && (
                <p className="user-typing">
                  {userIsTyping.sender} is typing...
                </p>
              )}

              <div className="send-message">
                <input
                  type="text"
                  className="input-message"
                  placeholder="enter the message"
                  value={userData.content}
                  onChange={(event) => {
                    handlePublicMessage(event);
                    sendTypingMessage();
                  }}
                  onKeyDown={(event) => {
                    handleKeyDown(event, sendPublicMessage);
                  }}
                />
                <button
                  type="button"
                  className="send-button"
                  onClick={sendPublicMessage}
                >
                  send
                </button>
              </div>
            </div>
          )}

          {tab !== "CHATROOM" && (
            <div className="chat-content">
              <ul className="chat-messages">
                {[...privateChats.get(tab)].map((chat, index) => (
                  <li
                    className={`message ${
                      chat.senderName === userData.username && "self"
                    }`}
                    key={index}
                  >
                    {chat.senderName !== userData.username && (
                      <div className="avatar">{chat.senderName}</div>
                    )}
                    <div className="message-data">{chat.content}</div>
                    {chat.senderName === userData.username && (
                      <div className="avatar self">{chat.senderName}</div>
                    )}
                  </li>
                ))}
              </ul>

              <div className="send-message">
                <input
                  type="text"
                  className="input-message"
                  placeholder="enter the message"
                  value={userData.content}
                  onChange={handleMessage}
                  onKeyDown={(event) =>
                    handleKeyDown(event, sendPrivateMessage)
                  }
                />
                <button
                  type="button"
                  className="send-button"
                  onClick={sendPrivateMessage}
                >
                  send
                </button>
              </div>
            </div>
          )}
        </div>
      ) : (
        <button className="connect-button" onClick={connectUser}>
          connect
        </button>
      )}
    </div>
  );
};

export default ChatRoom;
