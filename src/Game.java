public class Game {
    int _id; // game id
    GameBoard _board; // representation of gameboard
    char _nextPlayer; // who the next player is
    Player _white, _black;

    public Game(int id, Player white, Player black) {
        _board = new GameBoard();
        _nextPlayer = 'W'; // white always goes first
        _id = id;
        _white = white;
        _black = black;
    }

    public char WhoWon() {
        return _board.CheckWin();
    }

    public char NextPlayer() {
        return _nextPlayer;
    }

    public String GetGameBoard() {
        return _board.toString();
    }

    Player getPlayer() {
        if (_nextPlayer == 'W')
            return _white;
        return _black;
    }

    void changeNextPlayer() {
        if (_nextPlayer == 'W')
            _nextPlayer = 'B';
        else
            _nextPlayer = 'W';
    }

    public boolean IsLegalMove(int i, int j, char side) {
        if (side != _nextPlayer) return false;
        return _board.CheckLegalMove(i, j);
    }

    public char Play(int i, int j, char side) {
        if (!IsLegalMove(i, j, side))
            return 'X';
        _board.Play(i, j, side);
        changeNextPlayer();
        return WhoWon();
    }

    public void Loop() {
        while (WhoWon() == ' ') {
            getPlayer().MakeMove(this);
        }
        if (WhoWon() == 'W') {
            _white.YouWon();
            _black.YouLost();
        } else if (WhoWon() == 'B') {
            _black.YouWon();
            _white.YouLost();
        } else {
            _white.YouTied();
            _black.YouTied();
        }
    }
}
