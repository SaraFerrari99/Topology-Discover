package com.company;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Port implements EventHandler<MouseEvent> {

    String port_dpid;
    String port_no;
    String port_hw_addr;
    String port_name;

    Circle port;

    Main main;

    public Port(String port_dpid, String port_no, String port_hw_addr, String port_name, Main main) {

        this.port_dpid = port_dpid;
        this.port_no = port_no;
        this.port_hw_addr = port_hw_addr;
        this.port_name = port_name;
        this.port= new Circle(4.6, Color.BLACK);
        this.main = main;

        port.addEventHandler(MouseEvent.MOUSE_CLICKED, this);
    }

    void setPort(float x, float y){//to set the position of the port

        this.port.setCenterX(x);
        this.port.setCenterY(y);
    }

    @Override
    public String toString() {//string that contain the information about the host

        return "Port[" + "\n\tport_dpid= " + port_dpid  + "\n\tport_no=" + port_no + "\n\tport_hw_addr=" + port_hw_addr + "\n\tport_name=" + port_name  + "\n]\n";
    }

    @Override
    public void handle(MouseEvent mouseEvent) {// when click the image the text of second stage is setting and reload the secondaryStage

        main.txt.setText(toString());
        main.secndaryStage.sizeToScene();
        main.secndaryStage.show();
    }
}
