package CasseTete;

import CasseTete.Cell.Cell;
import CasseTete.Cell.CellPath;
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

    public View() {

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
                Cell cell = board[i][j];
                Image image = new Image("File:img/" + "E" + ".png");
                if (cell instanceof CellSymbol) {
                    String symbolText = ((CellSymbol) cell).getSymbol();
                    image = new Image("File:img/" + symbolText + ".png");
                }
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
                if (arg != null) {
                    System.out.println("model observer");
                    int x_cell = ((CellPath) arg).getX();
                    int y_cell = ((CellPath) arg).getY();
                    Image image = new Image("File:img/" + "P" + ".png");
                    ImageView imageView = new ImageView(image);
                    setDDOnImageView(imageView, x_cell, y_cell);
                    gridPane.add(imageView, x_cell, y_cell);
                }
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
                model.startDD(x, y);
                System.out.println("Start DD Detected " + x + "-" + y);
                event.consume();
            }
        });
    }

    private void ControllerOnDragEntered(ImageView imageView, int x, int y) {
        imageView.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                model.parcoursDD(x, y);
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