package com.jacjos.qvs.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Optional;

public class Util {

    private static Logger log = LoggerFactory.getLogger(Util.class);
    private static String splitKey = ":";

    public static String extractServiceIdentifierFromSoapXML(String reqBody){

        String svcIdentifier="default";
        String elementName = "";

        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(reqBody)));
            document.getDocumentElement().normalize();

            Element root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i=0; i<nodeList.getLength();i++){

                Node node = nodeList.item(i);
                if (node.getNodeName().endsWith(":Body")){

                    NodeList bodyNodes = node.getChildNodes();
                    for (int j=0; j<bodyNodes.getLength();j++){

                        elementName = bodyNodes.item(j).getNodeName();
                        if (elementName.startsWith("#")) continue;
                        svcIdentifier = elementName.split(splitKey,2)[1];
                        break;
                    }
                    break;
                }
            }
        }  catch (Exception e) {
            log.error("Exception while extracting service identifier from SOAP XML",e);
        }
        return svcIdentifier;
    }

    public static String extractServiceIdentifierFromURI(String uri, String httpMethod){
        return httpMethod+"_"+uri.replace("/", "_");
    }

    public static Optional<String> getConfiguredProxy(String[] proxyServices, String serviceIdentifier){
        return Arrays.stream(proxyServices)
                .filter(key -> {
                    String[] tokens = key.split(splitKey,2);
                    return serviceIdentifier.equalsIgnoreCase(tokens[tokens.length-1]);})
                .findFirst()
                .map(key -> {
                    String[] tokens = key.split(splitKey,2);
                    return Optional.ofNullable(tokens.length==2?tokens[0]:"default");})
                .orElse(Optional.empty());
    }

    public static <T> T getBean(String beanName, BeanManager beanManager, Class<T> type){
        Bean processorBean = beanManager.getBeans(beanName).iterator().next();
        CreationalContext context = beanManager.createCreationalContext(processorBean);
        return type.cast(beanManager.getReference(processorBean, type, context));
    }

}
