package no.hvl.dat250.feedapp.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.hvl.dat250.feedapp.dto.iot.IoTRequest;
import no.hvl.dat250.feedapp.dto.iot.IoTResponse;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.repository.PollRepository;
import no.hvl.dat250.feedapp.service.JwtService;
import no.hvl.dat250.feedapp.service.VoteService;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Component
public class IoTWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private VoteService voteService; 

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private JwtService jwtService;

    private Map<String, Boolean> authenticatedSessions = new ConcurrentHashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String sessionId = session.getId();
            ObjectMapper objectMapper = new ObjectMapper();

            if (!authenticatedSessions.getOrDefault(sessionId, false)) {
                String token = message.getPayload();
                if (jwtService.isValidSessionToken(token)) {
                    authenticatedSessions.put(sessionId, true);

                    ObjectNode json = objectMapper.createObjectNode();
                    json.put("type", "authenticationSuccess");
                    json.put("message", "Authentication successful");

                    session.sendMessage(new TextMessage(json.toString()));

                } else {
                    ObjectNode json = objectMapper.createObjectNode();
                    json.put("type", "authenticationFailure");
                    json.put("message", "Authentication failed");
                    session.sendMessage(new TextMessage(json.toString()));
                    session.close(CloseStatus.NOT_ACCEPTABLE);
                    return;
                }
            } else {
                JsonNode rootNode = objectMapper.readTree(message.getPayload());
                String messageType = rootNode.path("type").asText();

                switch (messageType) {
                    case "vote":
                        IoTResponse response = new IoTResponse();
                        response.setPollId(rootNode.path("pollId").asLong());
                        response.setYesVotes(rootNode.path("yesVotes").asInt());
                        response.setNoVotes(rootNode.path("noVotes").asInt());
                        voteService.createIoTVote(response);

                        ObjectNode json = objectMapper.createObjectNode();
                        json.put("type", "voteSuccess");
                        json.put("message", "Votes registered successfully");

                        session.sendMessage(new TextMessage(json.toString()));
                        break;
                    case "fetchQuestion":
                        int pollPin = rootNode.path("pollPin").asInt();
                        Poll publicPoll = pollRepository.findPublicPollsByPollPin(pollPin)
                                .orElseThrow(() -> new Exception("No public poll with the given pin: " + pollPin));
                        IoTRequest iotRequest = new IoTRequest();
                        iotRequest.setQuestion(publicPoll.getQuestion());
                        iotRequest.setPollId(publicPoll.getId());

                        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(iotRequest)));
                        break;
                    default:
                        ObjectNode json3 = objectMapper.createObjectNode();
                        json3.put("type", "unknownMessageType");
                        json3.put("message", "Unknown message type");
                        session.sendMessage(new TextMessage(json3.toString()));
                        break;
                }
            }
        } catch (Exception e) {
            try {
                session.sendMessage(new TextMessage("Error: " + e.getMessage()));
            } catch (Exception ignored) {
            } 
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        authenticatedSessions.remove(session.getId());
    }
}
