import { Client } from "@stomp/stompjs";

const SOCKET_URL = "http://localhost:8081/ws";

let stompClient = null;

const connect = (token, onMessageReceived) => {
  stompClient = new Client({
    brokerURL: SOCKET_URL,
    connectHeaders: {
      Authorization: `Bearer ${token}`,
    },

    debug: (str) => {
      console.log("STOMP: " + str);
    },

    onConnect: (frame) => {
      console.log("Connected to WebSocket");

      stompClient.subscribe("/topic/notifications", (message) => {
        onMessageReceived(JSON.parse(message.body));
      });
    },

    onStompError: (frame) => {
      console.error("Broker reported error: " + frame.headers["message"]);
      console.error("Additional details: " + frame.body);
    },
  });

  stompClient.activate();
};

const disconnect = () => {
  if (stompClient) {
    stompClient.deactivate();
    console.log("Disconnected from WebSocket");
  }
};

const socketService = {
  connect,
  disconnect,
};

export default socketService;
