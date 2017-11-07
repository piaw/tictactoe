public abstract class Player {
    char _side;
    public void InitPlayer(char side) { _side = side; };   // let the player know which side he's on
    public abstract void MakeMove(Game g);         // ask the player to make a move
    public abstract void YouWon(); // let the player know he won
    public abstract void YouLost(); // let the player know he lost
    public abstract void YouTied();
}
