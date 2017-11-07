import java.util.Random;

public class RandomPlayer extends Player {
    Random _rand;

    RandomPlayer() {
        _rand = new Random();
    }

    @Override
    public void MakeMove(Game g) {
        // make a random move
        int i = _rand.nextInt(3);
        int j = _rand.nextInt(3);

        while (!g.IsLegalMove(i, j, _side)) {
            i = _rand.nextInt(3);
            j = _rand.nextInt(3);
        }
        g.Play(i, j, _side);
    }

    @Override
    public void YouWon() {
    }

    @Override
    public void YouLost() {

    }

    @Override
    public void YouTied() {

    }
}
