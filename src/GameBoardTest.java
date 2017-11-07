import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    GameBoard makeGameBoard(String s) {
        return new GameBoard(s);
    }

    @org.junit.jupiter.api.Test
    void checkLegalMove() {
        String emptyBoard = "         ";
        GameBoard gb = new GameBoard(emptyBoard);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                assertTrue(gb.CheckLegalMove(i, j));
        }

        String allB = "BBBBBBBBB";
        gb = new GameBoard(allB);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                assertFalse(gb.CheckLegalMove(i, j));
        }

        String midgame = "BW B BB B";
        gb = new GameBoard(midgame);
        assertFalse(gb.CheckLegalMove(0, 0));
        assertFalse(gb.CheckLegalMove(2, 0));
        assertTrue(gb.CheckLegalMove(0,2));
        assertFalse(gb.CheckLegalMove(1,2));
        assertFalse(gb.CheckLegalMove(2,2));
    }

    @org.junit.jupiter.api.Test
    void checkWin() {
        assertEquals(makeGameBoard("BBB      ").CheckWin(), 'B');
        assertEquals(makeGameBoard("WWW      ").CheckWin(), 'W');
        assertEquals(makeGameBoard("   BBB   ").CheckWin(), 'B');
        assertEquals(makeGameBoard("   WWW   ").CheckWin(), 'W');
        assertEquals(makeGameBoard("      BBB").CheckWin(), 'B');
        assertEquals(makeGameBoard("      WWW").CheckWin(), 'W');
        assertEquals(makeGameBoard("B   B   B").CheckWin(), 'B');
        assertEquals(makeGameBoard("  W W W  ").CheckWin(), 'W');

    }

    @org.junit.jupiter.api.Test
    void play() {
        GameBoard gb = new GameBoard();
        assertEquals(gb.toString(), "         ");
        assertEquals(gb.CheckWin(), ' ');
        gb.Play(0, 0, 'B');
        assertEquals(gb.toString(), "B        ");
        assertEquals(gb.CheckWin(), ' ');
        gb.Play(1, 1, 'W');
        assertEquals(gb.toString(), "B   W    ");
        assertEquals(gb.CheckWin(), ' ');
        gb.Play(1, 0, 'B');
        assertEquals(gb.toString(), "B  BW    ");
        assertEquals(gb.CheckWin(), ' ');
        gb.Play(2, 0, 'W');
        assertEquals(gb.toString(), "B  BW W  ");
        assertEquals(gb.CheckWin(), ' ');
        gb.Play(0, 1, 'B');
        assertEquals(gb.toString(), "BB BW W  ");
        assertEquals(gb.CheckWin(), ' ');
        gb.Play(2, 2, 'W');
        assertEquals(gb.toString(), "BB BW W W");
        assertEquals(gb.CheckWin(), ' ');
        gb.Play(0, 2, 'B');
        assertEquals(gb.toString(), "BBBBW W W");
        assertEquals(gb.CheckWin(), 'B');
        assertTrue(gb.CheckWin('B'));
        assertFalse(gb.CheckWin('W'));
        gb = new GameBoard();
        gb.Play(1,1, 'W');
        assertEquals(gb.CheckWin(), ' ');
        gb.Play(2, 0, 'B');
        assertEquals(gb.CheckWin(), ' ');
        gb.Play(0, 1, 'W');
        assertEquals(gb.CheckWin(), ' ');
        gb.Play(0, 2, 'B');
        assertEquals(gb.CheckWin(), ' ');
        gb.Play(2, 1, 'W');
        assertEquals(gb.CheckWin(), 'W');
    }

    @org.junit.jupiter.api.Test
    void checkTie() {
        GameBoard gb = new GameBoard("BWWWBBBBW");
        assertEquals(gb.CheckWin(), 'T');
        gb = new GameBoard("BWWWBWBWB");
        assertEquals(gb.CheckWin(), 'B');
    }

}