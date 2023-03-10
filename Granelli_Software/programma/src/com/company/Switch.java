package com.company;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Switch implements EventHandler<MouseEvent>, Comparable<Switch> {

    Group gp = new Group();//gp containes the text of the different host

    Image img;
    ImageView imgv;

    String dpid;

    Main main;

    Rectangle frame;//to set in correct position the port

    Port ports[];

    int numP;//to calcolate where put the port

    float x , y;//set in the main to set the position of the link

    public Switch(String dpid, Port[] ports, Main main) throws FileNotFoundException{

        FileInputStream fis = new FileInputStream("C:\\Users\\sf129\\Desktop\\Granelli_Software\\immagini host e switch\\switch.jpeg");

        this.img = new Image(fis);
        this.imgv = new ImageView(img);

        this.main = main;

        imgv.setFitWidth(67.2);
        imgv.setFitHeight(54.55);

        frame = new Rectangle(imgv.getFitWidth()+2, imgv.getFitHeight()+2);
        frame.setStroke(Color.BLACK);
        frame.setStrokeWidth(1.7);

        this.dpid = dpid;

        this.ports = ports;

        this.numP = (ports.length)+1;

        putPorts();

        imgv.addEventHandler(MouseEvent.MOUSE_CLICKED,this);

        StackPane sp = new StackPane();
        sp.getChildren().addAll(frame, imgv);
        gp.getChildren().add(sp);

        for (Port p: ports ) {
            gp.getChildren().add(p.port);
        }
    }

    void putPorts(){//to set the position of the port

        float offset = 0;
        offset = (float) (imgv.getFitWidth()/numP);
        for(int i =0; i< (numP-1); i++){
            ports[i].setPort((i+1)*offset, (float) (frame.getX()+(frame.getHeight()+1.5)));

        }
    }

    Group getImg(){//return the image

        return gp;
    }

    @Override
    public String toString() {//string that contain the information about the host
        String printPort = "";

        for (Port port: ports) {
            printPort += port.toString() + "";
        }
        return "dpid: " + dpid + "\n\tports { \n" + printPort + "}";
    }

    @Override
    public void handle(MouseEvent mouseEvent) {// when click the image the text of second stage is setting and reload the secondaryStage

        main.txt.setText(toString());
        main.secndaryStage.sizeToScene();
        main.secndaryStage.show();
    }
    public int compareTo(Switch o) {// to ordinate the information about the host  comparing the pid

        return this.dpid.compareTo(o.dpid);
    }
}

