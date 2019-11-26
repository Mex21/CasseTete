package CasseTete;

import CasseTete.Cell.Cell;
import CasseTete.Cell.CellPath;
import CasseTete.Cell.CellSymbol;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

public class View extends Application {

    private Model model;
    private final int x = 5;
    private final int y = 5;

    public View() {

    }

    @Override
    public void start(Stage stage) {

        model = new Model(x, y);

        Cell[][] board = model.getBoard();
        BorderPane borderPane = new BorderPane();
        BorderPane borderPane1 =new BorderPane();
        GridPane gridPane = new GridPane();

        Button buttonUndo = new Button("Undo");
        borderPane1.setLeft(buttonUndo);
        ControllerOnClickUndo(buttonUndo);
        Button buttonNewGame = new Button("New Game");
        borderPane1.setRight(buttonNewGame);
        ControllerOnClickNewGame(buttonNewGame);

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
                //System.out.println(gridPane.toString());
            }
        }
        //System.out.println(gridPane.getChildren().toString());

        borderPane.setCenter(gridPane);
        borderPane.setTop(borderPane1);


        Scene scene = new Scene(borderPane);

        stage.setTitle("Casse tete");
        stage.setScene(scene);
        stage.show();

        model.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                int x_cell = ((Cell)arg).getX();
                int y_cell = ((Cell) arg).getY();
                if (arg instanceof CellPath) {
                    //System.out.println("model observer");
                    String text = CellPathToImg((CellPath)arg);
                    Image image = new Image("File:img/" + text + ".png");
                    ImageView imageView = new ImageView(image);
                    setDDOnImageView(imageView, x_cell, y_cell);
                    gridPane.add(imageView, x_cell, y_cell);
                }
                else if (arg instanceof  CellSymbol){
                    String symbole = ((CellSymbol)arg).getSymbol();
                    Image image = new Image("File:img/" + symbole + ".png");
                    ImageView imageView = new ImageView(image);
                    setDDOnImageView(imageView,x_cell,y_cell);
                    gridPane.add(imageView,x_cell,y_cell);
                }
                else if ((Integer)arg == 1){
                    victoryPopup();
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
                //System.out.println("Start DD Detected " + x + "-" + y);
                event.consume();
            }
        });
    }

    private void ControllerOnDragEntered(ImageView imageView, int x, int y) {
        imageView.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                model.parcoursDD(x, y);
                //System.out.println("Drag detected " + x + "-" + y);
                event.consume();
            }
        });
    }

    private void ControllerOnDragDone(ImageView imageView, int x, int y) {
        imageView.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                //System.out.println("Stop DD Detected " + x + "-" + y);
                model.stopDD(x,y);
                event.consume();
            }
        });
    }

    private String CellPathToImg (CellPath cell){
        int EntryX = cell.getPathEntry().getX();
        int EntryY = cell.getPathEntry().getY();
        int ExitX = cell.getPathExit().getX();
        int ExitY = cell.getPathExit().getY();
        return EntryX+"_"+EntryY+"_"+ExitX+"_"+ExitY;
    }

    private void ControllerOnClickUndo (Button button){
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Click undo");
                model.OnClickUndo();
            }
        });
    }

    private void ControllerOnClickNewGame (Button button){
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Click New Game");
                model.GenerateBoard(x,y);
            }
        });
    }

    private void victoryPopup(){
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("You Win !!!!!!!");
        Button button = new Button("OK !");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                popup.close();
            }
        });
        VBox vBox = new VBox();
        vBox.getChildren().addAll(button);

        Scene scene = new Scene(vBox,90,90);
        popup.setScene(scene);
        popup.show();
    }
}