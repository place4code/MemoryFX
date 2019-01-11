package sample;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class MemField {

    private MemCard[] card;

    private String[] images = {
            "images/img(1).jpg", "images/img(2).jpg",
            "images/img(3).jpg", "images/img(4).jpg",
            "images/img(5).jpg", "images/img(6).jpg",
            "images/img(7).jpg", "images/img(8).jpg",
            "images/img(9).jpg", "images/img(10).jpg",
            "images/img(11).jpg", "images/img(12).jpg",
            "images/img(13).jpg", "images/img(14).jpg",
            "images/img(15).jpg", "images/img(16).jpg",
            "images/img(17).jpg", "images/img(18).jpg",
            "images/img(19).jpg", "images/img(20).jpg",
            "images/img(21).jpg"
    };

    private int myScore, computerScore;
    private Label myScoreLabel, computerScoreLabel;

    private int cardsTurned; // Wie viele Karten sind umgedreht

    private MemCard[] currentPair;

    private int player;

    private int[][] cardsPosition;

    //#################################################################
    //#########################################      Constructor:

    public MemField() {

        card = new MemCard[42]; // Tabele für 42 Karten
        myScore = 0;
        computerScore = 0;
        cardsTurned = 0;
        currentPair = new MemCard[2];
        player = 0; // 0 = Mensch
        cardsPosition = new int[2][21];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 21; j++) {
                cardsPosition[i][j] = -1;
            }
        }

    }

    //#################################################################
    //#########################################       Methods:

    public FlowPane initGUI(FlowPane field) {

        drawCards(field);

        myScoreLabel = new Label(Integer.toString(myScore));
        computerScoreLabel = new Label(Integer.toString(computerScore));

        GridPane grid = new GridPane();

        grid.add(new Label("Player"), 0, 0);
        grid.add(myScoreLabel, 1, 0);
        grid.add(new Label("Computer"), 0, 1);
        grid.add(computerScoreLabel, 1, 1);

        field.getChildren().add(grid);

        return field;
    }

    private void drawCards(FlowPane field) {
        int count = 0;
        for (int i = 0; i <= 41; i++) {
            card[i] = new MemCard(images[count], i);
            if ((i+1) % 2 == 0) count++;
            field.getChildren().add(card[i]);
            card[i].setPosition(i);
        }
    }

}
