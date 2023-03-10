# Topology-Discover
Project of Virtualized and Softwarize Mobile Network

## Requirements
### Mininet
to install mininet, you can simply follow the link
(http://mininet.org/download/)

### Ryu Controller
to install ryu controller, you can simply follow the link
(https://ryu.readthedocs.io/en/latest/getting_started.html)

<p float="left">
  <img src="photo_read/ryu_cover2.jpg" alt="ryu-opf-mn" width="300" height="116">
</p>

### Java IDE
we personally used Intellij as IDE
<p float="left">
  <img src="photo_read/download.jpeg" alt="ryu-opf-mn" width="113" height="113">
</p>

## Project Layout 
    Granelli_Software
    ├── immagini host e switch                   # Images used inside java code   
    ├── musiche e gif                            # Sounds and gifs used inside java code
    ├── programma                                # Java code for topology display 
    ├── python per creazione txt                 # python code to launch ryu controller 
    ├── python per creazione topologia           # python code to create the topology
    ├── topologia1_remove.txt                    # txt generaed after removing the first topology
    ├── topologia1_preremove.txt                 # txt generaed after creating the first topology
    ├── topologia2_remove.txt                    # txt generaed after removing the second topology
    ├── topologia2_preremove.txt                 # txt generaed after creating the second topology
    ├── topologia3_remove.txt                    # txt generaed after removing the third topology
    ├── topologia3_preremove.txt                 # txt generaed after creating the third topology
    ├── Power.pptx                               # power point 
    └── README.md 

## Running the Code
To run the code, you have to open two different terminals. <br> On the first one you should launch the ryu controller by: 
```gradle
cd python\ per\ creazione\ txt
ryu-manager --observe-links ryuDiscovery.py
```

On the second terminal you should launch the topology (in this example I'll launch the first topo1.py, you can launch whatever costume topology you have):
```gradle
cd python\ per\ creazione\ topologia
sudo python3 topo1.py
```
Now to display the topology, all you have to do is to launch the java code (situated in the programma folder)

## Team Members
<tr>
  <th>
    COMPONENTS:
      - SARA FERRARI
      - HAFSA HAMZAOUI
  </th>
  <th>
      <img align="right" src="photo_read/collaboratori.jpg" alt="ryu-opf-mn" width="113" height="113">
  </th>
</tr>
