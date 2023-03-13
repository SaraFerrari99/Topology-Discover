package com.company;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Main extends Application {

    ArrayList<Switch> switches = new ArrayList<>();
    ArrayList<Port> ports = new ArrayList<>();
    ArrayList<Host> hosts = new ArrayList<>();
    ArrayList<Link> linksS = new ArrayList<>();
    ArrayList<Link> linksH = new ArrayList<>();

    Button backTopo = new Button("Avanti");
    Button startTopo = new Button("Start");
    Button refreshTopo = new Button("Refresh");

    ImageView imageview1 = new ImageView();

    Group images = new Group();

    int numS, numH,  numL; //count the Switch/Host/Link to not repeat the reading

    Scene scene;
    BorderPane root = new BorderPane();

    Text txt = new Text();

    Scene sceneTxt;

    boolean exists;

    Stage secndaryStage = new Stage();
    HBox gp;

    MediaPlayer player;
    MediaPlayer player1;

    void switchAddParser() throws ParseException {

        int counter = 0;

        BufferedReader reader;// to read the file

        String line;

        JSONParser jsonParser = new JSONParser();

        try {

            reader = new BufferedReader(new FileReader("C:\\Users\\sf129\\Desktop\\Granelli_Software\\topologia.txt\\addedSwithces.txt"));

            line = reader.readLine();

            while (line != null) {
                counter++;
                if (counter > numS) {//if counter is greater than numS, continue to read the next Switch

                    Switch s;

                    Object object = jsonParser.parse(line);//take all the data from the file
                    JSONObject jsonObj = (JSONObject) object;

                    String dpid = (String) jsonObj.get("dpid");//save on dpid the data of the fie that contain dpid

                    JSONArray port_array = (JSONArray) jsonObj.get("ports");

                    Port p[] = new Port[port_array.size()];

                    for (int i = 0; i < port_array.size(); i++) {

                        JSONObject port = (JSONObject) port_array.get(i);
                        String port_dpid = (String) port.get("dpid");
                        String port_no = (String) port.get(("port_no"));
                        String hw_addr = (String) port.get(("hw_addr"));
                        String name = (String) port.get(("name"));
                        p[i] = new Port(port_dpid, port_no, hw_addr, name, this);
                        ports.add(p[i]);
                    }

                    s = new Switch(dpid, p,this);
                    switches.add(s);
                    numS++;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void hostAddParser() throws ParseException {
        int counter = 0;

        BufferedReader reader;

        String line;

        JSONParser jsonParser = new JSONParser();

        try {
            reader = new BufferedReader(new FileReader("C:\\Users\\sf129\\Desktop\\Granelli_Software\\topologia.txt\\addedHosts.txt"));
            line = reader.readLine();

            while (line != null) {
                counter++;

                if (counter > numH) {
                    Host h;
                    Object object = jsonParser.parse(line);
                    JSONObject jsonObj = (JSONObject) object;
                    String mac = (String) jsonObj.get("mac");
                    JSONArray ipv6 = (JSONArray) jsonObj.get("ipv6");
                    String ipv = (String) ipv6.get(0);
                    org.json.JSONObject jsonObj1 = new org.json.JSONObject(line);
                    String port_dpid = jsonObj1.getJSONObject("port").getString("dpid");
                    String port_no = jsonObj1.getJSONObject("port").getString("port_no");
                    String hw_addr = jsonObj1.getJSONObject("port").getString("hw_addr");
                    String name = jsonObj1.getJSONObject("port").getString("name");

                    Port p = new Port(port_dpid, port_no, hw_addr, name,this);
                    ports.add(p);

                    h = new Host(mac, p,this);
                    hosts.add(h);

                    numH++;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    double getPositionXByPort(Port p){//to set next the link we create a function that save the center of all the port --> x

        double pos = 0;

        for (Switch s : switches) {

            for(int i=0; i< (s.numP-1); i++){

                if((s.ports[i].port_hw_addr.equals(p.port_hw_addr)) && (s.ports[i].port_name.equals(p.port_name)) && (s.ports[i].port_no.equals(p.port_no)) && (s.ports[i].port_dpid.equals(p.port_dpid))){

                    pos = s.x + s.ports[i].port.getCenterX();
                    break;
                }
            }
        }

        for(Host h: hosts){

            if((h.mac.equals(p.port_hw_addr)) || ((h.port.port_hw_addr.equals(p.port_hw_addr)) && (h.port.port_dpid.equals(p.port_dpid)) && (h.port.port_no.equals(p.port_no)) && (h.port.port_name.equals(p.port_name)))){
                pos = h.x + h.port.port.getCenterX();
                break;
            }
        }
        return pos;
    }

    double getPositionYByPort(Port p, int n){//to set next the link we create a function that save the center of all the port --> y

        double pos = 0;

        for (Switch s : switches) {

            for(int i=0; i< (s.numP-1); i++){

                if((s.ports[i].port_hw_addr.equals(p.port_hw_addr)) && (s.ports[i].port_name.equals(p.port_name)) && (s.ports[i].port_no.equals(p.port_no)) && (s.ports[i].port_dpid.equals(p.port_dpid))){
                    pos = ((s.y + s.ports[i].port.getCenterY() + n*1.2)-2.5);
                    break;
                }
            }
        }

        for(Host h: hosts){

            if((h.mac.equals(p.port_hw_addr)) || ((h.port.port_hw_addr.equals(p.port_hw_addr)) && (h.port.port_dpid.equals(p.port_dpid)) && (h.port.port_no.equals(p.port_no)) && (h.port.port_name.equals(p.port_name)))){
                pos = (h.y + h.port.port.getCenterY());
                break;
            }
        }
        return pos;
    }

    void switchRemoveParser() throws ParseException {
        JSONParser jsonParser = new JSONParser();
        BufferedReader reader;
        String line;
        try {
            reader = new BufferedReader(new FileReader("C:\\Users\\sf129\\Desktop\\Granelli_Software\\topologia.txt\\removedSwitches.txt"));
            line = reader.readLine();
            while (line != null) {
                Object object = jsonParser.parse(line);
                JSONObject jsonObj = (JSONObject) object;
                String dpid = (String) jsonObj.get("dpid");
                Iterator<Switch> itr = switches.iterator();
                while (itr.hasNext()) {
                    Switch swi = itr.next();
                    if (swi.dpid.equals(dpid)) {
                        itr.remove();
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void linkAddParser(){
        int counter = 0;
        BufferedReader reader;
        String line;
        try {
            reader = new BufferedReader(new FileReader("C:\\Users\\sf129\\Desktop\\Granelli_Software\\topologia.txt\\addedLinks.txt"));
            line = reader.readLine();
            while (line != null) {
                counter++;
                if(counter > numL){
                    org.json.JSONObject jsonObj = new org.json.JSONObject(line);
                    String src_dpid = jsonObj.getJSONObject("src").getString("dpid");
                    String src_port_no = jsonObj.getJSONObject("src").getString("port_no");
                    String src_hw_addr = jsonObj.getJSONObject("src").getString("hw_addr");
                    String src_name = jsonObj.getJSONObject("src").getString("name");
                    String dst_dpid = jsonObj.getJSONObject("dst").getString("dpid");
                    String dst_port_no = jsonObj.getJSONObject("dst").getString("port_no");
                    String dst_hw_addr = jsonObj.getJSONObject("dst").getString("hw_addr");
                    String dst_name = jsonObj.getJSONObject("dst").getString("name");
                    Port src = new Port(src_dpid, src_port_no, src_hw_addr, src_name,this);
                    Port dst = new Port(dst_dpid, dst_port_no, dst_hw_addr, dst_name,this);
                    Link l = new Link(src, dst,this);
                    for (Link lnk : linksS) {
                        if ((lnk.src.port_dpid.equals(dst_dpid) && lnk.src.port_hw_addr.equals(dst_hw_addr) && lnk.src.port_name.equals(dst_name) && lnk.src.port_no.equals(dst_port_no) && lnk.dst.port_dpid.equals(src_dpid) && lnk.dst.port_hw_addr.equals(src_hw_addr) && lnk.dst.port_name.equals(src_name) && lnk.dst.port_no.equals(src_port_no))) {
                            exists = true;
                            break;
                        }
                    }
                    if(!exists) {
                        linksS.add(l);
                        numL++;
                    }
                    exists = false;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void linkRemoveParser() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("C:\\Users\\sf129\\Desktop\\Granelli_Software\\topologia.txt\\removedLinks.txt");
        Scanner sc = new Scanner(fis);
        while(sc.hasNextLine()) {
            org.json.JSONObject jsonObj = new org.json.JSONObject(sc.nextLine());
            String src_dpid = jsonObj.getJSONObject("src").getString("dpid");
            String src_port_no = jsonObj.getJSONObject("src").getString("port_no");
            String src_hw_addr = jsonObj.getJSONObject("src").getString("hw_addr");
            String src_name = jsonObj.getJSONObject("src").getString("name");
            String dst_dpid = jsonObj.getJSONObject("dst").getString("dpid");
            String dst_port_no = jsonObj.getJSONObject("dst").getString("port_no");
            String dst_hw_addr = jsonObj.getJSONObject("dst").getString("hw_addr");
            String dst_name = jsonObj.getJSONObject("dst").getString("name");
            Iterator<Link> itr = linksS.iterator();
            while (itr.hasNext()) {
                Link l = itr.next();
                if ((l.src.port_dpid.equals(src_dpid) && l.src.port_hw_addr.equals(src_hw_addr) && l.src.port_name.equals(src_name) && l.src.port_no.equals(src_port_no) && l.dst.port_dpid.equals(dst_dpid) && l.dst.port_hw_addr.equals(dst_hw_addr) && l.dst.port_name.equals(dst_name) && l.dst.port_no.equals(dst_port_no))){
                    itr.remove();
                    numL--;
                }
            }
        }
    }

    void hostRemove(){
        Iterator<Host> itr = hosts.iterator();
        while (itr.hasNext()) {
            Host h = itr.next();
            boolean found = false;
            for (int i = 0; i < switches.size(); i++) {
                for (int j = 0; j < switches.get(i).ports.length; j++) {
                    if (h.port.port_name.equals(switches.get(i).ports[j].port_name)) {
                        found = true;
                    }
                }
            }
            if (!found) {
                itr.remove();
            }
        }
    }

    void drawSwitches() {//show the switch

        float offsetS = (float) (scene.getWidth() / (numS + 1));

        Collections.sort(switches);

        for (int i = 0; i < switches.size(); i++) {
            switches.get(i).x = i * offsetS;
            switches.get(i).y = 23;
            switches.get(i).gp.setLayoutX(i * offsetS);
            switches.get(i).gp.setLayoutY(23);
            images.getChildren().add(switches.get(i).getImg());
        }
    }
    void drawHosts(){
        float offsetH = (float) (scene.getWidth()/(hosts.size()+1));
        Collections.sort(hosts);
        for(int i=0; i<hosts.size(); i++){
            hosts.get(i).x = i*offsetH;
            hosts.get(i).y = 114;
            hosts.get(i).gp.setLayoutX(i*offsetH);
            hosts.get(i).gp.setLayoutY(114);
            images.getChildren().add(hosts.get(i).getImg());
        }
    }

    void drawLinksS(){
        for(int i=0; i<linksS.size(); i++){
            double sX, sY, eX, eY;
            sX = getPositionXByPort(linksS.get(i).src);
            sY = getPositionYByPort(linksS.get(i).src, i);
            eX = getPositionXByPort(linksS.get(i).dst);
            eY = getPositionYByPort(linksS.get(i).dst, i);
            linksS.get(i).link.setStartX(sX);
            linksS.get(i).link.setStartY(sY);
            linksS.get(i).link.setEndX(eX);
            linksS.get(i).link.setEndY(eY);
            if( sX!=0 && sY!=0 && eX!=0 && eY!=0){
                images.getChildren().add(linksS.get(i).getImg());
            }
        }
    }
    void drawLinksH() {
        linksH.clear();
        for (Host h: hosts ) {
            for(int i =0; i< switches.size(); i++){
                for(int j =0; j<switches.get(i).ports.length; j++){
                    if(h.port.port_name.equals(switches.get(i).ports[j].port_name)){
                        Link l = new Link(h.port, switches.get(i).ports[j],this);
                        l.link.setStartX(h.x + h.port.port.getCenterX());
                        l.link.setStartY(h.y + h.port.port.getCenterY());
                        l.link.setEndX(switches.get(i).x + switches.get(i).ports[j].port.getCenterX());
                        l.link.setEndY(switches.get(i).y + switches.get(i).ports[j].port.getCenterY());
                        linksH.add(l);
                        images.getChildren().add(l.getImg());
                    }
                }
            }
        }
    }


    void drawNetwork(){

        images.getChildren().clear();
        drawSwitches();
        drawHosts();
        drawLinksS();
        drawLinksH();
    }

    void retrieveData() throws IOException, ParseException {//read and upgrade the file data

        switchAddParser();
        hostAddParser();
        linkAddParser();
    }

    void removeData() throws FileNotFoundException, ParseException {
        switchRemoveParser();
        linkRemoveParser();
        hostRemove();
    }

    void startScene() throws FileNotFoundException {

        String uristring1 = new File("C:\\Users\\sf129\\Desktop\\Granelli_Software\\musiche e gif\\Suono di avvio di Windows XP.mp3").toURI().toString();
        player1 = new MediaPlayer(new Media(uristring1));

        FileInputStream fis = new FileInputStream("C:\\Users\\sf129\\Desktop\\Granelli_Software\\musiche e gif\\inizio.gif");
        Image image1 = new Image(fis);
        imageview1.setImage(image1);
        imageview1.setFitHeight(scene.getHeight()-70);
        imageview1.setFitWidth((int)((scene.getHeight()-70)*1.13));

        root.setBottom(startTopo);
        root.setCenter(imageview1);
        root.setAlignment(startTopo, Pos.BOTTOM_RIGHT);
        root.setMargin(startTopo , new Insets(13,13,13,13));

        player1.play();
    }

    void refreshScene() throws FileNotFoundException {//refresh the scene

        secndaryStage.close();

        String uristring = new File("C:\\Users\\sf129\\Desktop\\Granelli_Software\\musiche e gif\\Sigla Stanlio e olio by iUgo.mp3").toURI().toString();
        player = new MediaPlayer(new Media(uristring));

        ImageView imageview = new ImageView();
        FileInputStream fis = new FileInputStream("C:\\Users\\sf129\\Desktop\\Granelli_Software\\musiche e gif\\load.gif");
        Image image = new Image(fis);
        imageview.setImage(image);
        imageview.setFitHeight(scene.getHeight()-70);
        imageview.setFitWidth((int)((scene.getHeight()-70)*1.77));

        player.play();

        root.getChildren().clear();

        Text text = new Text("Loading ...");
        text.setStyle("-fx-font: 23 arial;");
        text.setFill(Paint.valueOf("PURPLE"));

        root.setTop(text);
        root.setBottom(backTopo);
        root.setCenter(imageview);
        root.setAlignment(backTopo, Pos.BOTTOM_RIGHT);
        root.setAlignment(text, Pos.TOP_CENTER);
        root.setMargin(backTopo, new Insets(13,13,13,13));

    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        numS=0; numH=0; numL=0;

        exists = false;

        retrieveData();

        scene = new Scene(root, Math.max(700,Math.max(switches.size(), hosts.size()) *127),350);

        startScene();

        startTopo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                primaryStage.setWidth(Math.max(700,Math.max(switches.size(), hosts.size()) *127));
                primaryStage.setHeight(350);

                drawNetwork();

                player1.stop();

                root.getChildren().clear();
                root.setBottom(refreshTopo);
                root.setCenter(images);
                root.setAlignment(refreshTopo, Pos.BOTTOM_RIGHT);
                root.setMargin(refreshTopo, new Insets(13,13,13,13));
            }
        });

        refreshTopo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    refreshScene();
                    primaryStage.setWidth(Math.max(700,Math.max(switches.size(), hosts.size()) *127));
                    primaryStage.setHeight(350);

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });



        backTopo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    removeData();
                    retrieveData();
                    player.stop();
                } catch (FileNotFoundException e) {
                    System.out.println("Something went wrong");
                } catch (ParseException e) {
                    System.out.println("Something went wrong");
                } catch (IOException e) {
                    System.out.println("Something went wrong");
                }
                drawNetwork();

                primaryStage.setWidth(Math.max(700,Math.max(switches.size(), hosts.size()) *127));
                primaryStage.setHeight(350);

                root.getChildren().clear();
                root.setBottom(refreshTopo);
                root.setCenter(images);
                root.setAlignment(refreshTopo, Pos.BOTTOM_RIGHT);
                root.setMargin(refreshTopo, new Insets(13,13,13,13));
            }
        });

        gp = new HBox(txt);
        gp.setAlignment(Pos.CENTER);
        gp.setMargin(txt, new Insets(20,60,20,60));
        sceneTxt = new Scene(gp);
        secndaryStage.setScene(sceneTxt);
        secndaryStage.sizeToScene();
        primaryStage.setTitle("Topology");
        primaryStage.setScene(scene);
        primaryStage.show();
        secndaryStage.setX(primaryStage.getX() + scene.getWidth());
        secndaryStage.setY(primaryStage.getY());
        secndaryStage.setTitle("Information of Topolgy");
    }

    public static void main(String[] args) {
        launch();
    }
}


