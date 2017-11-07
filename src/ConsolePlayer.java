import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ConsolePlayer extends Player {
    BufferedReader _buffer;

    public void InitPlayer(char side) {
        super.InitPlayer(side);
        _buffer = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void MakeMove(Game g) {
        String board = g.GetGameBoard();
        for (int i = 0; i < board.length(); i++) {
            System.out.printf("%c", board.charAt(i));
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
        int i = -1; int j = -1;
        while (!valid_input) {
            System.out.printf("Your move:");
            String input = "";
            try {
                input = _buffer.readLine();
            } catch(IOException e) {
                System.exit(-1);
            }
            Scanner scanner = new Scanner(input).useDelimiter(",");
            i = scanner.nextInt();
            j = scanner.nextInt();
            valid_input = (i >= 0 && i < 3) && (j >= 0 && j < 3);
            if (!valid_input) continue;
            // check for valid move
            valid_input = g.IsLegalMove(i, j, _side);
        }
        g.Play(i, j, _side);
    }

    @Override
    public void YouWon() {
        System.out.printf("You won!\n");
    }

    @Override
    public void YouLost() {
        System.out.printf("You lost!\n");
    }

    @Override
    public void YouTied() {
        System.out.printf("You tied!\n");
    }
}
