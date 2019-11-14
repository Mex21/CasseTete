package CasseTete;

import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.text.Text;

public class Controller {

    public Controller() {

    }

    public void ControllerOnDragDetected(Text t) {
        t.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard db = t.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(""); // non utilisé actuellement
                db.setContent(content);
                event.consume();
                System.out.println("DragDetected");
            }
        });
    }

    public void ControllerOnDragEntered(Text t) {
        t.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                System.out.println("I Move !!!");
                event.consume();
            }
        });
    }

    public void ControllerOnDragDone(Text t) {
        t.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                System.out.println("End");
                event.consume();
            }
        });
    }

}
