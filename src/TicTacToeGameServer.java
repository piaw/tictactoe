import java.util.HashMap;

public class TicTacToeGameServer {
    enum PlayerType { CONSOLE_CLIENT, ROBOT_PLAYER };

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

    public static void main(String[] args) {
        int id = CreateGame(PlayerType.CONSOLE_CLIENT, PlayerType.ROBOT_PLAYER);
        Game g = GetGame(id);
        System.out.printf("hello world\n");
        g.Loop();
        System.out.printf("Game over\n");
    }
}
