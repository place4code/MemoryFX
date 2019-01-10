package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class MemCard extends Button {

    private int imgID;
    private ImageView front, back;
    private int position;
    private boolean reversed; //Ist die Karte umgedreht?
    private boolean available; //Ist die Karte noch im Spiel?

    //#################################################################
    //#########################################      Constructor:

    public MemCard(int imgID, String frontSrc) {
        this.imgID = imgID;
        front = new ImageView(frontSrc);
        back = new ImageView(""); //rückseite der Karte
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
            setGraphic(new ImageView("")); // Wenn die Karte entfernt wurde
            available = false; // Die Karte wird nicht mehr verfügbar
        } else {
            //Wenn die Karte nich im Spiel ist, zeigt die Rückseite und setzt umgedreht auf false
            setGraphic(back);
            reversed = false;
        }
    }

    //#################################################################
    //#########################################      Set/Getters:

    public int getImgID() {
        return imgID;
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
                reversed = true;
            }
        }
    }


}
