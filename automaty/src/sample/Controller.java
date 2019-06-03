package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class Controller{

    public Canvas canvas;
    public TextField text_field2;
    public TextField text_field3;
    public TextField text_field4;
    public TextField field_row;
    public TextField field_column;
    public TextField field_ray;
    public ComboBox comboBox;
    public ChoiceBox choiceBox;
    ObservableList<String> rule;
    ObservableList<String> boundary;
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private Alert alert2 = new Alert(Alert.AlertType.ERROR);
    private Alert alert3 = new Alert(Alert.AlertType.ERROR);
    private Principle principle;
    private Thread t=new Thread();


    public void start() {
        principle=new Principle(canvas,choiceBox);
        principle.stop=false;

try {
    principle.height = Integer.parseInt(text_field3.getText());
    principle.width= Integer.parseInt(text_field2.getText());
    principle.amount= Integer.parseInt(text_field4.getText());
    principle.row= Integer.parseInt(field_column.getText());
    principle.column= Integer.parseInt(field_row.getText());
    principle.ray= Integer.parseInt(field_ray.getText());
    principle.board =  new int[principle.width][principle.height];
    principle.next_step = new int[principle.width][principle.height];

    if (comboBox.getValue().toString().equals("Homogeneous"))
    {
        principle.homogeneous();
    }

    if (comboBox.getValue().toString().equals("Random"))
    {
        principle.random();
    }
    if (comboBox.getValue().toString().equals("Ray"))
    {
        principle.ray();
        text_field4.setText(principle.counter-1+"");
    }
       if(t.isAlive())
        {
        t.stop();
        }

        t=new Thread(principle);
        t.start();


}
catch(NumberFormatException e)
{
    alert.setTitle("Error Dialog");
    alert.setHeaderText("Incorrect size");
    alert.showAndWait();
}
catch(NullPointerException e)
{
    alert2.setTitle("Error Dialog");
    alert2.setHeaderText("You didn't choose pattern");
    alert2.showAndWait();
}

catch(ArrayIndexOutOfBoundsException e)
{
    alert3.setTitle("Error Dialog");
    alert3.setHeaderText("ArrayIndexOutOfBoundsException");
    alert3.showAndWait();
}


    }
    public void configurate(){
        ObservableList<String> rule =
                FXCollections.observableArrayList("Homogeneous","Random","Ray","Set");
        comboBox.setItems(rule);


    }
    public void configurate2(){
        ObservableList<String> boundary = FXCollections.observableArrayList("Absorbing","Periodic");
        choiceBox.setItems(boundary);
    }
    public void stop(){
        principle.stop=!(principle.stop);
    }

}
