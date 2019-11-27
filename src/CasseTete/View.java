package CasseTete;

import CasseTete.Cell.Cell;
import CasseTete.Cell.CellPath;
import CasseTete.Cell.CellSymbol;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class View extends Application {

    private Model model;
    private int x = 4;
    private int y = 4;
    private GridPane gridPane;
    Cell[][] board;

    public View() {

    }

    @Override
    public void start(Stage stage) {


        model = new Model(x, y);

        board = model.getBoard();
        BorderPane borderPane = new BorderPane();
        BorderPane borderPane1 = new BorderPane();
        gridPane = new GridPane();

        Button buttonUndo = new Button("Undo");
        borderPane1.setLeft(buttonUndo);
        ControllerOnClickUndo(buttonUndo);
        Button buttonNewGame = new Button("New Game");
        borderPane1.setRight(buttonNewGame);
        ControllerOnClickNewGame(buttonNewGame);
        Button buttonBoardSize = new Button("Board Size");
        borderPane1.setCenter(buttonBoardSize);
        ControllerOnClickBoardSize(buttonBoardSize);

        GenerateGridPane();

        //System.out.println(gridPane.getChildren().toString());
        gridPane.setAlignment(Pos.CENTER);
        borderPane.setCenter(gridPane);
        borderPane.setTop(borderPane1);


        Scene scene = new Scene(borderPane);

        stage.setTitle("Casse tete");
        stage.setScene(scene);
        stage.show();

        model.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (arg instanceof CellPath) {
                    int x_cell = ((Cell) arg).getX();
                    int y_cell = ((Cell) arg).getY();
                    //System.out.println("model observer");
                    String text = CellPathToImg((CellPath) arg);
                    System.out.println(text);
                    Image image = new Image("File:img/" + text + ".png");
                    ImageView imageView = (ImageView)gridPane.getChildren().get(y * x_cell + y_cell);
                    imageView.setImage(image);
                    setDDOnImageView(imageView, x_cell, y_cell);
                } else if (arg instanceof CellSymbol) {
                    int x_cell = ((Cell) arg).getX();
                    int y_cell = ((Cell) arg).getY();
                    String symbol = ((CellSymbol) arg).getSymbol();
                    Image image = new Image("File:img/" + symbol + ".png");
                    ImageView imageView = (ImageView)gridPane.getChildren().get(y * x_cell + y_cell);
                    imageView.setImage(image);
                    setDDOnImageView(imageView, x_cell, y_cell);
                } else if ((Integer) arg == 1) {
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
                model.stopDD();
                event.consume();
            }
        });
    }

    private String CellPathToImg(CellPath cell) {
        int EntryX = cell.getPathEntry().getX();
        int EntryY = cell.getPathEntry().getY();
        int ExitX = cell.getPathExit().getX();
        int ExitY = cell.getPathExit().getY();
        return EntryX + "_" + EntryY + "_" + ExitX + "_" + ExitY;
    }

    private void ControllerOnClickUndo(Button button) {
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Click undo");
                model.OnClickUndo();
            }
        });
    }

    private void ControllerOnClickNewGame(Button button) {
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Click New Game");
                model.GenerateBoard(x, y);
            }
        });
    }

    private void victoryPopup() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("You Win !!!!!!!");
        Button button = new Button("OK !");
        Text text = new Text("You won !!!!");
        text.setFont(Font.font(30));

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                popup.close();
                model.GenerateBoard(x, y);
            }
        });
        VBox vBox = new VBox();
        vBox.getChildren().addAll(text, button);
        vBox.setAlignment(Pos.BOTTOM_CENTER);

        Scene scene = new Scene(vBox);
        popup.setScene(scene);
        popup.show();
    }

    private void ControllerOnClickBoardSize(Button button){
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage popup = new Stage();
                popup.initModality(Modality.APPLICATION_MODAL);
                popup.setTitle("Choose your Size");

                final TextField textField = new TextField();
                Text text = new Text("Set x size !");
                text.setFont(Font.font(20));
                HBox hBox = new HBox();
                hBox.getChildren().addAll(text, textField);
                hBox.setAlignment(Pos.BOTTOM_CENTER);

                final TextField textField2 = new TextField();
                text = new Text("Set y size !");
                text.setFont(Font.font(20));
                HBox hBox2 = new HBox();
                hBox2.getChildren().addAll(text, textField2);
                hBox2.setAlignment(Pos.BOTTOM_CENTER);

                Button buttonOk = new Button("OK");
                buttonOk.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int x_temp  = Integer.parseInt((textField.getCharacters()).toString());
                        int y_temp = Integer.parseInt((textField2.getCharacters()).toString());
                        if (x_temp > 2 && y_temp > 2){
                            gridPane.getChildren().remove(0,x*y);
                            x = x_temp;
                            y = y_temp;
                            for(int i = 0; i< x;i++){
                                for(int j = 0; j< y;j++){
                                    gridPane.add(new ImageView(new Image("File:img/" + "0_0_0_0" + ".png")),i,j);
                                }
                            }
                            System.out.println("coucou");
                            model.GenerateBoard(x,y);
                            popup.close();
                        }
                    }
                });

                VBox vBox = new VBox();
                vBox.getChildren().addAll(hBox,hBox2,buttonOk);
                vBox.setAlignment(Pos.CENTER);

                Scene scene = new Scene(vBox);
                popup.setScene(scene);
                popup.show();
            }
        });
    }

    private void GenerateGridPane(){
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                Cell cell = board[i][j];
                Image image = new Image("File:img/" + "0_0_0_0" + ".png");
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
    }
}