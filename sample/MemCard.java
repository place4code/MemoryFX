package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class MemCard extends Button {

    private int cardID;
    private ImageView front, back;
    private int position;
    private boolean reversed; //Ist die Karte umgedreht?
    private boolean available; //Ist die Karte noch im Spiel?
    private MemField field;

    public boolean isReversed() {
        return reversed;
    }

    public boolean isAvailable() {
        return available;
    }
//#################################################################
    //#########################################      Constructor:

    public MemCard(String frontSrc, int cardID, MemField field) {
        this.cardID = cardID;
        this.field = field;
        front = new ImageView(String.valueOf(getClass().getResource(frontSrc)));
        back = new ImageView(String.valueOf(getClass().getResource("images/back.png"))); //rückseite der Karte
        setGraphic(back);
        available = true;
        reversed = false;
        setOnAction(new CardListener());
    }

    //#################################################################
    //#########################################      Methods:

    public void showBack(boolean removeFromGame) {
        // 2 Karten wurden gefunden?
        if (removeFromGame) {
            setGraphic(new ImageView(String.valueOf(getClass().getResource("images/removed.png")))); // Wenn die Karte entfernt wurde
            available = false; // Die Karte wird nicht mehr verfügbar
        } else {
            //Wenn die Karte nich im Spiel ist, zeigt die Rückseite und setzt umgedreht auf false
            setGraphic(back);
            System.out.println("setGraphic(back)");
            reversed = false;
        }
    }

    //#################################################################
    //#########################################      Set/Getters:

    public int getCardID() {
        return cardID;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    //#################################################################
    //#########################################      InClasses:

    class CardListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {

            // Wenn die Karte schon nicht im Spiel ist
            if (!available) return;

            // Wenn die Karte noch nicht umgedreht wurde, vorderseite anzeigen:
            if (!reversed) {
                setGraphic(front);
                System.out.println("setGraphic(front)");
                reversed = true;
                field.showCard(MemCard.this); // wenn ich nur this übergebe, übergebe ich eine Instanz von Listener, nicht MemCard
            }
        }
    }


}
