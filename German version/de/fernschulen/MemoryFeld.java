package de.fernschulen;

//f�r die Klassen Arrays und Collections
import java.util.Arrays;
import java.util.Collections;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class MemoryFeld {
	//eine innere Klasse f�r den Eventhandler des Timer
	class TimerHandler implements EventHandler<ActionEvent> {
			@Override
			//die Methode ruft die Methode karteSchliessen() auf
			public void handle(ActionEvent arg0) {
				
				karteSchliessen();
			}
		}
	
	//######################################################################
	//
	class TimerEnde implements EventHandler<ActionEvent> {
		@Override
		//die Methode ruft die Methode karteSchliessen() auf
		public void handle(ActionEvent arg0) {
			Platform.exit();
		}
	}
	//######################################################################
	

	//das Array f�r die Karten
	private MemoryKarte[] karten;
	
	//das Array f�r die Namen der Grafiken
	private String[] bilder = {"grafiken/apfel.jpg", "grafiken/birne.jpg", "grafiken/blume.jpg", "grafiken/blume2.jpg",
			"grafiken/ente.jpg", "grafiken/fisch.jpg", "grafiken/fuchs.jpg", "grafiken/igel.jpg",
			"grafiken/kaenguruh.jpg", "grafiken/katze.jpg", "grafiken/kuh.jpg", "grafiken/maus1.jpg",
			"grafiken/maus2.jpg", "grafiken/maus3.jpg", "grafiken/melone.jpg", "grafiken/pilz.jpg",
			"grafiken/ronny.jpg", "grafiken/schmetterling.jpg","grafiken/sonne.jpg",
			"grafiken/wolke.jpg", "grafiken/maus4.jpg"};
	
	//f�r die Punkte
	private int menschPunkte, computerPunkte;
	//zwei Labels f�r die Punkte
	private Label menschPunkteLabel, computerPunkteLabel;
	
	//wie viele Karten sind aktuell umgedreht?
	private int umgedrehteKarten;
	
	//f�r das aktuell umdrehte Paar
	private MemoryKarte[] paar;
	
	//f�r den aktuellen Spieler
	private int spieler;
	
	//das "Ged�chtnis" f�r den Computer
	//er speichert hier wo das Gegenst�ck liegt
	private int[][] gemerkteKarten;
	
	//f�r die Spielst�rke
	private int spielstaerke;
	
	//f�r den Timer
	private Timeline timer;
	//######################################################################
	//Aufgabe 1:
	//Ende Timer
	private Timeline ende = new Timeline(new KeyFrame(Duration.millis(6000), new TimerEnde()));
	private Label ergebnis;
	//######################################################################
	//Aufgabe 2:
	private Label werZieht;
	//######################################################################
	
	//der Konstruktor
	public MemoryFeld() {
		//das Array f�r die Karten erstellen, insgesamt 42 St�ck
		karten = new MemoryKarte[42];

		//f�r das Paar
		paar = new MemoryKarte[2];

		//f�r das Ged�chtnis
		//es speichert f�r jede Karte paarweise die Position im Spielfeld
		gemerkteKarten = new int[2][21];
		
		//keiner hat zu Beginn einen Punkt
		menschPunkte = 0;
		computerPunkte = 0;
		
		//es ist keine Karte umgedreht
		umgedrehteKarten = 0;
		
		//der Mensch f�ngt an
		spieler = 0;
		
		//die Spielst�rke ist 10
		spielstaerke = 0;
		
		//es gibt keine gemerkten Karten
		for (int aussen = 0; aussen < 2; aussen++)
			for (int innen = 0; innen < 21; innen++)
				gemerkteKarten[aussen][innen] = -1;
	}

	//die Methode erstellt die Oberfl�che und zeichnet die Karten �ber eine eigene Methode
	//�bergeben wird ein FlowPane
	public FlowPane initGUI(FlowPane feld) {
		//f�r die Ausgaben
		kartenZeichnen(feld);
		menschPunkteLabel = new Label();
		computerPunkteLabel = new Label();
		
		werZieht = new Label();
		werZieht.setText("Der Mensch");
		//############################
		
		ergebnis = new Label();
		ergebnis.setFont(new Font("system", 20));
	
		//############################
		menschPunkteLabel.setText(Integer.toString(menschPunkte));
		computerPunkteLabel.setText(Integer.toString(computerPunkte));
		
		//in zwei Spalten anzeigen
		GridPane tempGrid = new GridPane();
		//und einf�gen, dabei werden die Koordinaten angegeben
		tempGrid.add(new Label("Mensch: "), 0 , 0 );
		tempGrid.add(menschPunkteLabel, 1, 0);
		tempGrid.add(new Label("Computer: "), 0, 1);
		tempGrid.add(computerPunkteLabel, 1 ,1);
		//############################
		//Aufgabe 2:
		tempGrid.add(new Label("Es zieht: "), 0 , 2 );
		tempGrid.add(werZieht, 1, 2);
		//############################
		//Aufgabe 1:
		tempGrid.add(ergebnis, 1, 3);
		//############################
		
		feld.getChildren().add(tempGrid);
		return feld;
	}
	
	//das eigentliche Spielfeld erstellen
	private void kartenZeichnen(FlowPane feld) {
		int count = 0;
		for (int i = 0; i <= 41; i++) {
			//eine neue Karte erzeugen
			karten[i] = new MemoryKarte(bilder[count], count, this);
			//bei jeder zweiten Karte kommt auch ein neues Bild
			if ((i + 1) % 2 == 0)
				count++;
		}
		//die Karten werden gemischt 
		Collections.shuffle(Arrays.asList(karten));

		//und ins Spielfeld gesetzt
		for (int i = 0; i <= 41; i++) {
			feld.getChildren().add(karten[i]);
			//die Position der Karte setzen
			karten[i].setBildPos(i);
		}
	}
	
	//die Methode �bernimmt die wesentliche Steuerung des Spiels
	//Sie wird beim Anklicken einer Karte ausgef�hrt
	public void karteOeffnen(MemoryKarte karte) {
		//zum Zwischenspeichern der ID und der Position
		int kartenID, kartenPos;

		//die Karten zwischenspeichern
		paar[umgedrehteKarten]=karte;
		
		//die ID und die Position beschaffen
		kartenID = karte.getBildID();
		kartenPos = karte.getBildPos();
		
		//die Karte in das Ged�chtnis des Computers eintragen
		//aber nur dann, wenn es noch keinen Eintrag an der entsprechenden Stelle gibt
		if ((gemerkteKarten[0][kartenID] == -1)) 
			gemerkteKarten[0][kartenID] = kartenPos;
		else
			//wenn es schon einen Eintrag gibt 
			//und der nicht mit der aktuellen Position �bereinstimmt, dann haben wir die
			//zweite Karte gefunden
			//die wird dann in die zweite Dimension eingetragen
			if (gemerkteKarten[0][kartenID] != kartenPos) 
				gemerkteKarten[1][kartenID] = kartenPos;
		//umgedrehte Karten erh�hen
		umgedrehteKarten++;
		
		//sind 2 Karten umgedreht worden?
		if (umgedrehteKarten == 2) {
			//dann pr�fen wir, ob es ein Paar ist
			paarPruefen(kartenID);
			//den Timer erzeugen
			timer = new Timeline(new KeyFrame(Duration.millis(2000), new TimerHandler()));
			//und starten
			timer.play();
		}
		//haben wir zusammen 21 Paare, dann ist das Spiel vorbei
		if (computerPunkte + menschPunkte == 1) {
			//###################################
			if(computerPunkte < menschPunkte) {
				
				ergebnis.setText("Ausgezeichnet, Hast du gewonnen!!  | " 
				+ Integer.toString(menschPunkte) + " : " + Integer.toString(computerPunkte));
				
				
			} else {
				ergebnis.setText("Leider, der Computer hat gewonnen  | " 
			+ Integer.toString(computerPunkte) + " : " + Integer.toString(menschPunkte));
			
			}
			
			ende.play();
			//###################################

		}
	}
	
	//die Methode dreht die Karten wieder auf die R�ckseite
	//bzw. nimmt sie aus dem Spiel
	private void karteSchliessen() {
		boolean raus = false;
		//ist es ein Paar?
		if (paar[0].getBildID() == paar[1].getBildID()) 
			raus = true;
		//wenn es ein Paar war, nehmen wir die Karten aus dem Spiel
		//sonst drehen wir sie nur wieder um
		paar[0].rueckseiteZeigen(raus);
		paar[1].rueckseiteZeigen(raus);
		//es ist keine Karte mehr ge�ffnet
		umgedrehteKarten = 0;
		//hat der Spieler kein Paar gefunden?
		if (raus == false) 
			//dann wird der Spieler gewechselt
			spielerWechseln();
		else
			//hat der Computer ein Paar gefunden?
			//dann ist er noch einmal an der Reihe
			if (spieler == 1)
				computerZug();
	}
	
	//die Methode pr�ft, ob ein Paar gefunden wurde
	private void paarPruefen(int kartenID) {
		if (paar[0].getBildID() == paar[1].getBildID()) {
			//die Punkte setzen
			paarGefunden();
			//die Karten aus dem Ged�chtnis l�schen
			gemerkteKarten[0][kartenID]=-2;
			gemerkteKarten[1][kartenID]=-2;
		}
	}

	//die Methode setzt die Punkte, wenn ein Paar gefunden wurde
	private void paarGefunden() {
		//spielt gerade der Mensch?
		if (spieler == 0) {
			menschPunkte++;
			menschPunkteLabel.setText(Integer.toString(menschPunkte));
		}
		else {
			computerPunkte++;
			computerPunkteLabel.setText(Integer.toString(computerPunkte));
		}
	}
	
	//die Methode wechselt den Spieler
	private void spielerWechseln() {
		//wenn der Mensch an der Reihe war,
		//kommt jetzt der Computer
		if (spieler == 0) {
			werZieht.setText("Der Computer");
			spieler = 1;
			
			computerZug();
			
		}
		else {
			werZieht.setText("Der Mensch");
			spieler = 0;
		}
			
			
	}
	
	//die Methode setzt die Computerz�ge um
	private void computerZug() {
		int kartenZaehler = 0;
		int zufall = 0;
		boolean treffer = false;
		//zur Steuerung �ber die Spielst�rke
		if ((int)(Math.random() * spielstaerke) == 0) {
			//erst einmal nach einem Paar suchen
			//dazu durchsuchen wir das Array gemerkteKarten, bis wir in beiden Dimensionen
			//einen Wert finden
			while ((kartenZaehler < 21) && (treffer == false)) {
				//gibt es in beiden Dimensionen einen Wert gr��er oder gleich 0?
				if ((gemerkteKarten[0][kartenZaehler] >=0) &&  (gemerkteKarten[1][kartenZaehler] >=0)) {
					//dann haben wir ein Paar
					treffer = true;
					//die Vorderseite der Karte zeigen
					karten[gemerkteKarten[0][kartenZaehler]].vorderseiteZeigen();
					//und dann die Karte �ffnen
					karteOeffnen(karten[gemerkteKarten[0][kartenZaehler]]);
					//die zweite Karte auch
					karten[gemerkteKarten[1][kartenZaehler]].vorderseiteZeigen();
					karteOeffnen(karten[gemerkteKarten[1][kartenZaehler]]);
				}
				kartenZaehler++;
			}
		}
		//wenn wir kein Paar gefunden haben, drehen wir zuf�llig zwei Karten um
		if (treffer == false) {
			//solange eine Zufallszahl suchen, bis eine Karte gefunden wird, die noch im Spiel ist
			do {
				zufall = (int)(Math.random() * karten.length);
			} while (karten[zufall].isNochImSpiel() == false);
			//die erste Karte umdrehen
			//die Vorderseite der Karte zeigen
			karten[zufall].vorderseiteZeigen();
			//und dann die Karte �ffnen
			karteOeffnen(karten[zufall]);

			//f�r die zweite Karte m�ssen wir au�erdem pr�fen, ob sie nicht gerade angezeigt wird
			do {
				zufall = (int)(Math.random() * karten.length);
			} while ((karten[zufall].isNochImSpiel() == false) || (karten[zufall].isUmgedreht() == true));
			//und die zweite Karte umdrehen
			karten[zufall].vorderseiteZeigen();
			karteOeffnen(karten[zufall]);
		}
	}
	
	//die Methode liefert, ob Z�ge des Menschen erlaubt sind
	//die R�ckgabe ist false, wenn gerade der Computer zieht
	//oder wenn schon zwei Karten umgedreht sind
	//sonst ist die R�ckgabe true
	public boolean zugErlaubt() {
		boolean erlaubt = true;
		//zieht der Computer?
		if (spieler == 1)
			erlaubt = false;
		//sind schon zwei Karten umdreht?
		if (umgedrehteKarten == 2)
			erlaubt = false;
		return erlaubt;
	}
	
	
	
}
