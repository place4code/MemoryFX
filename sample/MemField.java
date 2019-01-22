package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Collections;

public class MemField {

    //#################################################################
    //#########################################      Timer-class:
    class TimerHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            System.out.println("timerHandler");
            turn();
        }
    }

    //#################################################################
    //#########################################      Variables:

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

    private MemCard[] pair;

    private int player;

    private int[][] cardsPosition;

    private Timeline timer;

    //#################################################################
    //#########################################      Constructor:

    public MemField() {

        card = new MemCard[42]; // Tabele für 42 Karten
        myScore = 0;
        computerScore = 0;
        cardsTurned = 0;
        pair = new MemCard[2];
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
            // ID's sind doppelt wegen count
            card[i] = new MemCard(images[count], count, this); // COUNT sondern i !!!
            if ((i+1) % 2 == 0) count++;
        }

        //die Karen werden gemischt
        //die Methode shuffle erwartet List daswegen Arrays.asList...
        Collections.shuffle(Arrays.asList(card));

        for (int i = 0; i <= 41; i++) {
            field.getChildren().add(card[i]); // Karte hinzufügen
            card[i].setPosition(i);
        }
    }


    public void showCard(MemCard memCard) {

        int cardID = memCard.getCardID();
        int position = memCard.getPosition();

        pair[cardsTurned++] = memCard; // cards Turned: 0,1
        //schneller da oben
        //cardsTurned++;

        System.out.println(cardID);

        if (cardsPosition[0][cardID] == -1) { // Wenn noch kein Eintrag ist.
            cardsPosition[0][cardID] = position;
        } else if (cardsPosition[0][cardID] != position) { // zwei ID's aber ungleiche positions
            cardsPosition[1][cardID] = position;
        }



        if (cardsTurned == 2) {

            checkPair(cardID); // Karten prüfen

            timer = new Timeline(new KeyFrame(Duration.millis(2000), new TimerHandler()));
            timer.play();
            //hier WAR turn(), jetzt ist im Timer
            //turn(); //die Karten wieder umdrehen
        }

        if (computerScore + myScore == 21) {
            Platform.exit();
        }

    }

    private void checkPair(int cardID) {
        if (pair[0].getCardID() == pair[1].getCardID()) {
            System.out.println("pair[0].getCardID() == pair[1].getCardID()");
            found();

            //die Karten aus dem Gedächtnis "löschen"
            cardsPosition[0][cardID] = -2;
            cardsPosition[1][cardID] = -2;
        }
    }

    //die Methode dreht die Karte wieder auf die Rückseite, oder nimmt sie aus dem Speil
    private void turn() {

        boolean removeCards = false;
        if (pair[0].getCardID() == pair[1].getCardID()) removeCards = true; //Paar gefunden

        System.out.println("turn");

        cardsTurned = 0; // Schon wider keine Karte umgedreht

        pair[0].showBack(removeCards);
        pair[1].showBack(removeCards);

        if (!removeCards) {
            //hat der Spieler kein Paar gefunden
            changePlayer();
        } else if(player == 1) {
            //hat den Computer ein Paar gefunden?
            //Dann ist er noch einmal an der Reihe
            computerTurn();
        }
        timer.stop();
    }

    private void changePlayer() {
        if (player == 0) {
            player = 1;
            computerTurn();
        } else {
            player = 0;
        }
    }

    //Die Methode setzt dioe Computerzüge um.
    private void computerTurn() {
        System.out.println("computerTurn");
        int cardCounter = 0;
        int random = 0;

        boolean hit = false;

        //nach einem Paar suchen:
        while ((cardCounter < 21) && !hit) {
            //hast du eine Karte gefunden, mit position >= 0
            if ((cardsPosition[0][cardCounter] >= 0) && (cardsPosition[1][cardCounter] >= 0)) {
                card[cardsPosition[0][cardCounter]].fire();
                card[cardsPosition[1][cardCounter]].fire();
                hit = true;
            }
            cardCounter++;
        }

        if (!hit) {

        }
    }

    private void found() {
        System.out.println("found");
        if (player == 0) myScoreLabel.setText(Integer.toString(++myScore));
        else computerScoreLabel.setText(Integer.toString(++computerScore));
    }
}
