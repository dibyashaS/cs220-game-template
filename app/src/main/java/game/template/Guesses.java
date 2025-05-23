package game.template;

public class Guesses {
    int row;
    int col;
    int previousMove;

    Guesses(int row, int col, int previousMove) { //Initialize constructor for guesses
        this.row=row;
        this.col=col;
        this.previousMove=previousMove;
    }
}
