/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import portscanner.entities.CommandThread;
import portscanner.entities.NetworkNode;
import portscanner.entities.Port;

/*
 *
 * @author William Deming
 *
 * SettingsManager is used to retrieve data from xml files
 * 
 */
public class SettingsManager {
    private ArrayList<NetworkNode> networkNodes = new ArrayList<NetworkNode>();
    
    public SettingsManager(){
        initializeSettings();
    }
    
    //Creates xml file for nodes if not present
    public void initializeSettings(){
        File nodesFile = new File("/home/admin/Downloads/portscanner/settings/nodes.xml");
        if(nodesFile.exists() == false){
            System.out.println("Creating xml files");
            CommandThread init = new CommandThread(new String[]{"sh", "/home/admin/Downloads/portscanner/src/portscanner/utils/init-settings.sh"});
            init.start();
        }
    }

    //Retrieve nodes from xml
    public void retrieveNodes(){
        ArrayList<Port> ports = new ArrayList<Port>();
        int number;
        String address;
        String status;
        
        try {
            File fXmlFile = new File("/home/admin/Downloads/portscanner/settings/nodes.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("node");

            //for each node element
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nNode = nodeList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element nElement = (Element) nNode;
                    
                    //retrieve the ip address
                    address = nElement.getElementsByTagName("address").item(0).getTextContent();
                    //System.out.println("address retrieved: " + nElement.getElementsByTagName("address").item(0).getTextContent()); - working
                    
                    NodeList portInfo = nElement.getElementsByTagName("port");
                    
                    //for each port in the node, get its port number and expected status
                    for(int j = 0; j < portInfo.getLength(); j++) {
                        Node pInfo = portInfo.item(j);
                        if (pInfo.getNodeType() == Node.ELEMENT_NODE) {
                            Element pElement = (Element) pInfo;
                            
                            //System.out.println("number of port:  " + Integer.parseInt(pElement.getElementsByTagName("number").item(0).getTextContent()));
                            //System.out.println("status of port:  " + pElement.getElementsByTagName("expectedstatus").item(0).getTextContent());
                            
                            number = Integer.parseInt(pElement.getElementsByTagName("number").item(0).getTextContent());
                            status = pElement.getElementsByTagName("expectedstatus").item(0).getTextContent();
                            
                            ports.add(new Port(number, status));
                        }
                    }
                    
                    //Save the port information and ip address in network node object, add to arraylist of network nodes
                    NetworkNode netNode = new NetworkNode(ports, address);
                    networkNodes.add(netNode);
                    ports = new ArrayList<Port>();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void printNodes(){
        System.out.println("\nNodes retrieved from nodes.xml:");
        for(int i = 0; i < networkNodes.size(); i++){
            networkNodes.get(i).printContents();
        }
    }
    
    public ArrayList<NetworkNode> getNetworkNodes() {
        retrieveNodes();
        return networkNodes;
    }

    public void setNetworkNodes(ArrayList<NetworkNode> networkNodes) {
        this.networkNodes = networkNodes;
    }
}
