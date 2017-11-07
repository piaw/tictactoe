import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class TicTacToeGameServer {
    enum PlayerType { CONSOLE_CLIENT, ROBOT_PLAYER, HTML_CLIENT };

    //TODO: Move all the message protocol stuff into its own self-contained module shared with client
    static final String NEW_GAME_REQ = "new_game_request";
    static final String NEW_GAME_REPLY = "new_game_reply";
    static final String STATUS_REQ = "game_status_request";
    static final String STATUS_REPLY = "game_status_reply";
    static final String MOVE_REQ = "move_request";
    static final String MOVE_REPLY = "move_reply";
    static final String ERROR = "error";

    static class NewGameRequest {
        PlayerType p1;
        PlayerType p2;
    }

    static class NewGameReply {
        int gameid;
    }

    static class GameStatusRequest {
        int gameid;
    }

    static class GameStatusReply {
        char board[];
        char nextplayer;
        char whowon;
    }

    static class PlayMoveRequest {
        int gameid;
        int i;
        int j;
        char side;
    }

    static class PlayMoveReply {
        char board[];
        char whowon;
    }

    static class ErrorMessage {
        String error;
    }

    static class TicTacToeMessage {
        String messageType;

        NewGameRequest newgame_request;
        NewGameReply newgame_reply;
        GameStatusRequest gamestatus_request;
        GameStatusReply gamestatus_reply;
        PlayMoveRequest playmove_request;
        PlayMoveReply playmove_reply;
        ErrorMessage error;

        public String toString() {
            return messageType;
        }
    }

    static void Log(String s) {
        System.out.println(s);
    }

    static int gameId = 0;
    static HashMap games_table = new HashMap<Integer, Game>();
    public TicTacToeGameServer() {

    }

    static Player generatePlayer(PlayerType ptype) {
        switch(ptype) {
            case CONSOLE_CLIENT:
                return new ConsolePlayer();
            case ROBOT_PLAYER:
                return new RandomPlayer();
            case HTML_CLIENT:
                return new HTMLPlayer();
        }

        return null;
    }

    public static synchronized int CreateGame(PlayerType p1_type, PlayerType p2_type) {
        Player p1 = generatePlayer(p1_type);
        p1.InitPlayer('W');
        Player p2= generatePlayer(p2_type);
        p2.InitPlayer('B');

        Game ng = new Game(gameId, p1, p2);
        int id = gameId;
        games_table.put(id, ng);
        gameId++;
        return id;
    }

    public static Game GetGame(int id) {
        return (Game) games_table.get(id);
    }

    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(portNumber);
        while (!serverSocket.isClosed()) {
            Log("Socket opened");
            Socket clientSocket = serverSocket.accept();
            Log("Accept");
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String content = input.readLine();
            System.out.println("Read:" + content);
            Writer output = new OutputStreamWriter(clientSocket.getOutputStream());
            Gson gson = new Gson();
            TicTacToeMessage message = gson.fromJson(content, TicTacToeMessage.class);
            Log("Message received" + message);

            try {
                if (message.messageType.equals(NEW_GAME_REQ)) {
                    Log("New Game Requested");
                    int gameid = CreateGame(message.newgame_request.p1, message.newgame_request.p2);
                    TicTacToeMessage reply = new TicTacToeMessage();
                    reply.messageType = NEW_GAME_REPLY;
                    reply.newgame_reply = new NewGameReply();
                    reply.newgame_reply.gameid = gameid;
                    output.write(gson.toJson(reply));
                } else if (message.messageType.equals(STATUS_REQ)) {
                    Log("Status Requested");
                    int gameid = message.gamestatus_request.gameid;
                    Game game = (Game) games_table.get(gameid);
                    TicTacToeMessage reply = new TicTacToeMessage();

                    if (game == null) {
                        reply.messageType = ERROR;
                        reply.error = new ErrorMessage();
                        reply.error.error = "No such game";
                    } else {
                        reply.messageType = STATUS_REPLY;
                        reply.gamestatus_reply = new GameStatusReply();
                        reply.gamestatus_reply.board = game.GetGameBoard().toCharArray();
                        reply.gamestatus_reply.whowon = game.WhoWon();
                        reply.gamestatus_reply.nextplayer = game.NextPlayer();
                    }
                    output.write(gson.toJson(reply));
                } else if (message.messageType.equals(MOVE_REQ)) {
                    Log("Move requested");
                    int gameid = message.playmove_request.gameid;
                    Game game = (Game) games_table.get(gameid);
                    TicTacToeMessage reply = new TicTacToeMessage();
                    if (game == null) {
                        reply.messageType = ERROR;
                        reply.error = new ErrorMessage();
                        reply.error.error = "No such game";
                    } else {
                        int i = message.playmove_request.i;
                        int j = message.playmove_request.j;
                        char side = message.playmove_request.side;
                        if (!game.IsLegalMove(i, j, side)) {
                            reply.messageType = ERROR;
                            reply.error = new ErrorMessage();
                            reply.error.error = "Illegal Move";
                        } else {
                            char whoWon = game.Play(i, j, side);
                            reply.playmove_reply = new PlayMoveReply();
                            reply.messageType = MOVE_REPLY;
                            reply.playmove_reply.board = game.GetGameBoard().toCharArray();
                            reply.playmove_reply.whowon = whoWon;
                            if (whoWon == ' ') {
                                // this only matters when you have computer players
                                game.getPlayer().MakeMove(game);
                            }
                        }
                        output.write(gson.toJson(reply));
                    }
                } else {
                    // invalid message
                    TicTacToeMessage reply = new TicTacToeMessage();
                    reply.messageType = ERROR;
                    reply.error = new ErrorMessage();
                    reply.error.error = "Invalid Request";
                    output.write(gson.toJson(reply));
                }
            } finally {
                output.flush();
                clientSocket.close();
            }
        }
    }
}
