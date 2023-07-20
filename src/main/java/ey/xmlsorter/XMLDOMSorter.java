package ey.xmlsorter;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class XMLDOMSorter {

    public static String handleFile(File fileToProcess) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fileToProcess);
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();

        NodeList elements = root.getElementsByTagName("InsurerRatingFactor");
        List<Element> sortedElements = sortElements(elements);

        for (Element element : sortedElements) {
            root.removeChild(element);
        }

        for (Element element : sortedElements) {
            root.appendChild(doc.importNode(element, true));
        }

        String resultFileName = "sorted_raw_file.xml";
        TransformerFactory tfFactory = TransformerFactory.newInstance();
        Transformer transformer = tfFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(resultFileName));
        transformer.transform(source, result);

        return resultFileName;
    }

    private static List<Element> sortElements(NodeList elements) {
        List<Element> elementList = new ArrayList<>();
        for (int i = 0; i < elements.getLength(); i++) {
            elementList.add((Element) elements.item(i));
        }

        elementList.sort(Comparator.comparing(e -> ((Element) e.getElementsByTagName("InsurerRatingFactor_Code").item(0)).getAttribute("Val")));

        return elementList;
    }
}
