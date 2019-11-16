package CasseTete;

import CasseTete.Cell.CellPath;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;

import java.util.Observable;


public class Controller extends Observable {


    public Controller() {

    }

    public void ControllerOnDragDetected(ImageView imageView, int x, int y) {
        imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(""); // non utilisé actuellement
                db.setContent(content);
                event.consume();
            }
        });
    }

    public void ControllerOnDragEntered(ImageView imageView, int x, int y) {
        imageView.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                CellPath cellPath = new CellPath(x, y, false, "E");
                setChanged();
                notifyObservers(cellPath);
                System.out.println("Drag detected " + x + "-" + y);
                event.consume();
            }
        });
    }

    public void ControllerOnDragDone(ImageView imageView, int x, int y) {
        imageView.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.consume();
                //model.stopDD(x, y);
            }
        });
    }

}
