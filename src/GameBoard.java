public class GameBoard {
    private char _board[][];
    // space ' ' - not played
    // 'B' - black
    // 'W' - white

    public GameBoard() {
        // empty gameboard
        _board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                _board[i][j] = ' ';
            }
        }
    }

    public GameBoard(String s) {
        assert(s.length() == 9);
        char gb[] = s.toCharArray();
        _board = new char[3][3];
        int idx = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char position = gb[idx];
                assert(position == ' ' || position == 'B' || position == 'W');
                _board[i][j] = position;
                idx++;
            }
        }
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                s += _board[i][j];
        return s;
    }

    private boolean CheckRowWin(char side, int i) {
        assert(i >= 0);
        assert(i < 3);
        for (int j = 0; j < 3; j++)
            if (_board[i][j] != side)
                return false;
        return true;
    }

    private boolean CheckColumnWin(char side, int j) {
        assert(j >= 0);
        assert(j < 3);
        for (int i = 0; i < 3; i++)
            if (_board[i][i] != side)
                return false;
        return true;
    }

    boolean CheckLegalMove(int i, int j) {
        return _board[i][j] == ' ';
    }

    boolean CheckWin(char side) {
        // check all rows and columns
        for (int i = 0; i < 3; i++) {
            if (CheckRowWin(side, i)) return true;
            if (CheckColumnWin(side, i)) return true;
        }
        // check two diagonals
        if (_board[0][0] == side && _board[1][1] == side && _board[2][2] == side) return true;
        if (_board[0][2] == side && _board[1][1] == side && _board[2][0] == side) return true;
        return false;
    }

    boolean CheckTie() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (_board[i][j] == ' ')
                    return false;
            }
        }

        return true;
    }

    public char CheckWin() {
        if (CheckWin('B')) return 'B';
        if (CheckWin('W')) return 'W';
        if (CheckTie()) return 'T';
        return ' ';
    }

    public boolean Play(int i, int j, char side) {
        assert(side == 'B' || side == 'W');
        if (!CheckLegalMove(i, j)) return false;
        _board[i][j] = side;
        return true;
    }

}
