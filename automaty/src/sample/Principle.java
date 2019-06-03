package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.nio.BufferOverflowException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;


public class Principle extends Task{
    public Canvas canvas;
    public ChoiceBox choiceBox;
    GraphicsContext gc ;
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    int width,height,amount,row,column,ray;
    int[][] board,next_step;
    private int[] neighberi={0,-1,1,0};
    private int[] neighberj={-1,0,0,1};
    private int[]neighbers=new int[4];
    public int counter=1,new_i,new_j,size=5;
    public boolean stop=false;
    public boolean change=true;
    public boolean col=false;
    List<Color> c_list = new ArrayList<Color>();
    Principle(Canvas a, ChoiceBox b){
        canvas=a;
        choiceBox=b;
    }
    public Object call() throws InterruptedException {

            gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.WHITE);
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.fillRect(0, 0, width * size, height * size);
            add_color();



        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                next_step[i][j] = board[i][j];
            }

            while (!stop && change) {
                change = false;
                for (int i = 0; i < width; i++)
                    for (int j = 0; j < height; j++) {
                        if (board[i][j]==0) {
                            change = true;
                            for (int k = 0; k < 4; k++) {
                                new_i = i + neighberi[k];
                                new_j = j + neighberj[k];

                                if(choiceBox.getValue().toString().equals("Absorbing")) {
                                    if (new_i > width - 1)
                                        neighbers[2] = 0;
                                    if (new_i < 0)
                                        neighbers[1] = 0;
                                    if (new_j > height - 1)
                                        neighbers[3] = 0;
                                    if (new_j < 0)
                                        neighbers[0] = 0;
                                    if(new_i <= width - 1 && new_i>=0 && new_j <= height-1 && new_j>=0)
                                        neighbers[k] = board[new_i][new_j];
                                }
                                else if(choiceBox.getValue().toString().equals("Periodic")){
                                    if (new_i > width - 1)
                                        new_i=0;
                                    if (new_i < 0)
                                       new_i=width-1;
                                    if (new_j > height - 1)
                                        new_j=0;
                                    if (new_j < 0)
                                        new_j=height-1;
                                    if(new_i <= width - 1 && new_i>=0 && new_j <= height-1 && new_j>=0)
                                        neighbers[k] = board[new_i][new_j];
                                }
                            }
                            //most frequently number in array

                            int count = 0, tempCount = 0;
                            int popular=0;
                            int temp = 0;
                            for (int z = 0; z < (neighbers.length); z++) {
                                temp = neighbers[z];
                                tempCount = 0;
                                if(temp!=0) {
                                    for (int zz = 0; zz < neighbers.length; zz++) {
                                        if (temp == neighbers[zz])
                                            tempCount++;
                                    }
                                }
                                if (tempCount >= count ) {
                                        popular = temp;
                                        count = tempCount;

                                }
                            }
                            next_step[i][j] = popular;

                        }
                    }


                for (int i = 0; i < width; i++)
                    for (int j = 0; j < height; j++) {
                        board[i][j] = next_step[i][j];
                    }

                Platform.runLater(new Runnable() {
                    @Override public void run(){
                        for (int i = 0; i < width; i++) {
                            for (int j = 0; j < height; j++) {
                                if(board[i][j]!=0) {
                                    gc.setFill(c_list.get(board[i][j]));
                                    gc.fillRect(i * size, j * size, size, size);
                                }
                            }

                        }

                    }
                });


                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (stop) {
                    Thread.sleep(1000);

                }

                canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        board[(int) (event.getX() / size)][(int) (event.getY() / size)] = counter;
                        counter++;
                        add_color();

                        gc.setFill(c_list.get(board[(int) (event.getX() / size)][(int) (event.getY() / size)]));
                        gc.fillRect((int) (event.getX() / size) * size, (int) (event.getY() / size) * size, size, size);
                    }
                });

            }



        return 0;
    }
    void homogeneous()
    {
        int x=0,y=0;
        for(int i=0;i<row;i++)
        {
            for(int j=0;j<column;j++)
           {

               x=i*width/row+width/row/2;
               y=j*height/column+height/column/2;
               if(x>width-1)
                   i=width-1;
               if(y>height-1)
                   j=height-1;
               board[x][y]=counter;
                       counter++;
               add_color();
           }

        }
    }

    void random()
    {
        Random rand = new Random();
        for(int i=0;i<amount;i++)
        {
            int r_i=rand.nextInt(width);
            int r_j=rand.nextInt(height);
            if(board[r_i][r_j]==0) {
                board[r_i][r_j] = counter;
                counter++;
                add_color();
            }
            else
                i--;
        }
    }
    void ray() {
        try {
            int exception = 0;
            Random rand = new Random();
            Boolean agremeent = true;
            for (int i = 0; i < amount; i++) {
                int r_i = rand.nextInt(width);
                int r_j = rand.nextInt(height);

                for (int ii = 0; ii < width; ii++)
                    for (int jj = 0; jj < height; jj++) {
                        if (board[ii][jj] != 0 && (Math.sqrt(Math.pow(ii - r_i, 2) + Math.pow(jj - r_j, 2)) < ray)) {
                            agremeent = false;
                        }
                    }

                if (board[r_i][r_j] == 0 && agremeent) {
                    board[r_i][r_j] = counter;
                    counter++;
                    add_color();
                } else {
                    i--;
                    agremeent = true;
                    exception++;
                }
                if (exception > 30000) {
                    throw new RuntimeException();
                }

            }

        }
        catch(RuntimeException e)
        {
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Cannot find place");
                alert.showAndWait();
        }
    }

    void add_color(){
        while(true) {
            Random rand = new Random();
            int r = rand.nextInt(255);
            int g = rand.nextInt(255);
            int b = rand.nextInt(255);
            Color c = Color.rgb(r, g, b);
            for (int j = 0; j < c_list.size(); j++) {
                if (c == c_list.get(j)) {
                    col = false;
                }

            }
            if (col) {
                c_list.add(c);
                break;
            }
            col=true;
        }
    }

}
