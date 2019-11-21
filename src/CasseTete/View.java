package CasseTete;

import CasseTete.Cell.Cell;
import CasseTete.Cell.CellSymbol;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

public class View extends Application {

    private Model model;

    public View(){

    }

    @Override
    public void start(Stage stage) {
        final int x = 5;
        final int y = 5;

        model = new Model(x, y);

        Cell[][] board = model.getBoard();
        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                String symbolText = ((CellSymbol) board[i][j]).getSymbol();
                Image image = new Image("File:img/" + symbolText + ".png");
                System.out.println(symbolText);
                ImageView imageView = new ImageView(image);
                setDDOnImageView(imageView, i, j);
                gridPane.add(imageView, i, j);
                System.out.println(gridPane.toString());
            }
        }
        gridPane.setGridLinesVisible(true);
        //System.out.println(gridPane.getChildren().toString());

        borderPane.setCenter(gridPane);

        Scene scene = new Scene(borderPane);

        stage.setTitle("Casse tete");
        stage.setScene(scene);
        stage.show();

        model.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println("model observer");
                int x_cell = ((CellSymbol) arg).getX();
                int y_cell = ((CellSymbol) arg).getY();
                String symbol = ((CellSymbol) arg).getSymbol();
                Image image = new Image("File:img/" + symbol + ".png");
                ImageView imageView = new ImageView(image);
                setDDOnImageView(imageView, x_cell, y_cell);
                gridPane.add(imageView, x_cell, y_cell);

            }
        });
    }

    private void setDDOnImageView(ImageView imageView, int x, int y) {
        ControllerOnDragDetected(imageView, x, y);
        ControllerOnDragEntered(imageView, x, y);
        ControllerOnDragDone(imageView, x, y);
    }

    private void ControllerOnDragDetected(ImageView imageView, int x, int y) {
        imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(""); // non utilisé actuellement
                db.setContent(content);
                model.startDD(x,y);
                System.out.println("Start DD Detected " + x + "-" + y);
                event.consume();
            }
        });
    }

    private void ControllerOnDragEntered(ImageView imageView, int x, int y) {
        imageView.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                model.parcoursDD(x,y);
                System.out.println("Drag detected " + x + "-" + y);
                event.consume();
            }
        });
    }

    private void ControllerOnDragDone(ImageView imageView, int x, int y) {
        imageView.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                System.out.println("Stop DD Detected " + x + "-" + y);
                event.consume();
            }
        });
    }


}