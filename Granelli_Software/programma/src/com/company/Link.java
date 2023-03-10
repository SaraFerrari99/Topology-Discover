package com.company;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.Random;

public class Link implements EventHandler<MouseEvent> {

    Port src; //first port

    Port dst;// second port in relation with the first port

    Line link;

    Main main;

    Color colors[] = {Color.DARKRED, Color.DARKCYAN, Color.DARKKHAKI, Color.DARKGRAY, Color.CORAL, Color.DARKGREEN, Color.DARKORANGE, Color.NAVY, Color.DARKOLIVEGREEN};

    Random random = new Random();

    Group gp = new Group();

    public Link(Port src, Port dst, Main main) {

        this.src = src;
        this.dst = dst;
        this.link = new Line(src.port.getCenterX(), src.port.getCenterY(), dst.port.getCenterX(), dst.port.getCenterY());
        this.main = main;

        link.setVisible(true);
        link.addEventHandler(MouseEvent.MOUSE_CLICKED, this);
        link.setStroke(colors[(random.nextInt(colors.length-1))]);
        link.setStrokeWidth(2.3);

        gp.getChildren().add(link);
    }

    Group getImg(){//return the image
        return gp;
    }

    @Override
    public String toString() {//string that contain the information about the host

        return "LINK: \n\t src: \n " + src.toString() + "\t dst:  \n" + dst.toString() + "\nLINK:" + "\n\t src: \n " + dst.toString() + "\t dst:  \n" + src.toString() ;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {// when click the image the text of second stage is setting and reload the secondaryStage

        main.txt.setText(toString());
        main.secndaryStage.sizeToScene();
        main.secndaryStage.show();
    }
}


