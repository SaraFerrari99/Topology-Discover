package com.company;

import javafx.event.Event;
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

public class Host implements EventHandler<MouseEvent>, Comparable<Host>{
    Group gp = new Group(); //gp containes the text of the different host

    Image img;
    ImageView imgv;

    Main main;

    Rectangle frame;//to set in correct position the port

    String mac;

    Port port;

    float x , y; //set in the main to set the position of the link


    public Host(String mac, Port port, Main main)  throws FileNotFoundException  {
        this.mac = mac;
        this.port = port;
        this.main= main;

        FileInputStream fis = new FileInputStream("C:\\Users\\sf129\\Desktop\\Granelli_Software\\immagini host e switch\\host.png");
        img = new Image(fis);
        imgv = new ImageView(img);
        imgv.setFitWidth(67.2);
        imgv.setFitHeight(54.55);

        frame = new Rectangle(imgv.getFitWidth()+2, imgv.getFitHeight()+2);
        frame.setStroke(Color.BLACK);
        frame.setStrokeWidth(1.7);

        imgv.addEventHandler(MouseEvent.MOUSE_CLICKED, this);

        StackPane sp = new StackPane();
        sp.getChildren().addAll(frame, imgv);
        gp.getChildren().add(sp);
        gp.getChildren().add(port.port);

        putPorts();
    }

    void putPorts(){//to set the position of the port

        float offset = 0;
        offset = (float) (imgv.getFitWidth()/2);
        port.setPort(offset, (float) (frame.getX()-1.7));

    }

    Group getImg(){//return the image

        return gp;
    }

    @Override
    public String toString() {//string that contain the information about the host

        return "mac: " + mac + "\n" + port.toString();
    }

    @Override
    public void handle(MouseEvent mouseEvent) { // when click the image the text of second stage is setting and reload the secondaryStage

        main.txt.setText(toString());
        main.secndaryStage.sizeToScene();
        main.secndaryStage.show();
    }

    @Override
    public int compareTo(Host o) {// to ordinate the information about the host  comparing the pid

        return this.port.port_dpid.compareTo(o.port.port_dpid);
    }
}


