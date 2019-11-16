package CasseTete;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

public class View extends Application{
    private Controller controller = new Controller();

    public View(){

    }

    @Override
    public void start(Stage stage) {

        final int x = 3;
        final int y = 3;

        Model model = new Model(x,y);
        Cell[][] board = model.getBoard();
        model.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (o == model){
                    System.out.println("Le plateau a été modifier");
                }
            }
        });

        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                String symbolText = board[i][j].getSymbol();
                Image image = new Image("File:img/" + symbolText + ".png");
                ImageView imageView = new ImageView(image);
                controller.ControllerOnDragDetected(imageView, i, j);
                controller.ControllerOnDragEntered(imageView, i, j);
                controller.ControllerOnDragDone(imageView, i, j);
                gridPane.add(imageView, i, j);
            }
        }
        gridPane.setGridLinesVisible(true);

        borderPane.setCenter(gridPane);

        Scene scene = new Scene(borderPane, Color.LIGHTGREEN);

        stage.setTitle("Casse tete");
        stage.setScene(scene);
        stage.show();
    }
}