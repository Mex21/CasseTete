package CasseTete;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class View extends Application {
    private Controller controller = new Controller();
    private Model model = new Model();

    public View() {

    }

    @Override
    public void start(Stage stage) {
        final int x = 5;
        final int y = 5;
        Cell[][] gridName = model.GenerateBoard(x,y);

        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                //Text t = new Text("X");
                Text t = gridName[i][j].getSymbol();
                t.setFont(Font.font("Time new roman", 30));
                t.setFill(Color.BLACK);
                controller.ControllerOnDragDetected(t);
                controller.ControllerOnDragEntered(t);
                controller.ControllerOnDragDone(t);
                System.out.println(gridName[i][j].getSymbol());
                gridPane.add(t,i,j);
            }
        }
        gridPane.setGridLinesVisible(true);

        borderPane.setCenter(gridPane);

        Scene scene = new Scene(borderPane, Color.LIGHTGREEN);

        stage.setTitle("Casse tête");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}