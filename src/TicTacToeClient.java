import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TicTacToeClient {


    public static void main(String args[]) throws IOException {
        Socket clientSocket = new Socket(args[0], Integer.parseInt(args[1]));
        Writer writer = new OutputStreamWriter(clientSocket.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedReader user = new BufferedReader(new InputStreamReader(System.in));

        TicTacToeGameServer.TicTacToeMessage message = new TicTacToeGameServer.TicTacToeMessage();
        message.messageType = TicTacToeGameServer.NEW_GAME_REQ;
        message.newgame_request = new TicTacToeGameServer.NewGameRequest();
        message.newgame_request.p1 = TicTacToeGameServer.PlayerType.HTML_CLIENT;
        message.newgame_request.p2 = TicTacToeGameServer.PlayerType.ROBOT_PLAYER;

        Gson gson = sendToServer(writer, message);

        TicTacToeGameServer.TicTacToeMessage reply = getTicTacToeMessage(gson, reader);

        assert(reply.messageType.equals(TicTacToeGameServer.NEW_GAME_REPLY));
        int gameid = reply.newgame_reply.gameid;

        System.out.printf("Gameid = %d\n", gameid);

        boolean gameInProgress = true;

        while (gameInProgress) {
            TicTacToeGameServer.TicTacToeMessage req = new TicTacToeGameServer.TicTacToeMessage();
            req.messageType = TicTacToeGameServer.STATUS_REQ;
            req.gamestatus_request = new TicTacToeGameServer.GameStatusRequest();
            req.gamestatus_request.gameid = gameid;

            clientSocket = new Socket(args[0], Integer.parseInt(args[1]));
            writer = new OutputStreamWriter(clientSocket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sendToServer(writer, req);
            reply = getTicTacToeMessage(gson, reader);
            assert (reply.messageType.equals(TicTacToeGameServer.STATUS_REPLY));
            char board[] = reply.gamestatus_reply.board;
            for (int i = 0; i < 9; i++) {
                System.out.printf("%c", board[i]);
                if (i != 2 && i != 5 && i != 8) {
                    System.out.printf("|");
                } else {
                    if (i == 2 || i == 5)
                        System.out.printf("\n-----\n");
                    else
                        System.out.printf("\n");
                }
            }

            boolean valid_input = false;
            int i = -1;
            int j = -1;
            while (!valid_input) {
                System.out.printf("Your move:");
                String input = "";
                try {
                    input = user.readLine();
                } catch (IOException e) {
                    System.exit(-1);
                }
                Scanner scanner = new Scanner(input).useDelimiter(",");
                i = scanner.nextInt();
                j = scanner.nextInt();
                valid_input = (i >= 0 && i < 3) && (j >= 0 && j < 3);
                if (!valid_input) continue;
            }

            clientSocket = new Socket(args[0], Integer.parseInt(args[1]));
            writer = new OutputStreamWriter(clientSocket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            TicTacToeGameServer.TicTacToeMessage move = new TicTacToeGameServer.TicTacToeMessage();
            move.messageType = TicTacToeGameServer.MOVE_REQ;
            move.playmove_request = new TicTacToeGameServer.PlayMoveRequest();
            move.playmove_request.gameid = gameid;
            move.playmove_request.i = i;
            move.playmove_request.j = j;
            move.playmove_request.side = 'W'; // we're always playing white
            sendToServer(writer, move);
            reply = getTicTacToeMessage(gson, reader);
            if (!reply.messageType.equals(TicTacToeGameServer.MOVE_REPLY))
                continue;
            char whowon = reply.playmove_reply.whowon;
            if (whowon != ' ') {
                if (whowon == 'W') {
                    System.out.println("You won!");
                } else if (whowon == 'T') {
                    System.out.println("Tie!");
                } else {
                    System.out.println("You lost!");
                }
                gameInProgress = false;
            }
        }
    }

    private static TicTacToeGameServer.TicTacToeMessage getTicTacToeMessage(Gson gson, BufferedReader reader) throws IOException {
        String content = reader.readLine();
        System.out.println("Read:" + content);
        TicTacToeGameServer.TicTacToeMessage reply;
        reply = gson.fromJson(content, TicTacToeGameServer.TicTacToeMessage.class);
        return reply;
    }

    private static Gson sendToServer(Writer writer, TicTacToeGameServer.TicTacToeMessage message) throws IOException {
        Gson gson = new Gson();
        String toServer = gson.toJson(message);
        System.out.println(toServer);
        writer.write(toServer+"\n");
        writer.flush();
        return gson;
    }
}
