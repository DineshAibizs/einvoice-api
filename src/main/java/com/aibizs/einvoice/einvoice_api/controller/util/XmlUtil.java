package com.aibizs.einvoice.einvoice_api.controller.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class XmlUtil {

    public Document xmlStringToDocument(String xmlString) {

        try {
            log.debug("xmlString-->{}", xmlString);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlString)));

            log.debug("Root element = {}", document.getDocumentElement().getTagName());
            return document;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse XML", e);
        }
    }

    public String documentToString(Document document) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter writer = new StringWriter();

            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Document to String", e);
        }
    }

    public byte[] documentToBytes(Document document) {
        return documentToString(document).getBytes(StandardCharsets.UTF_8);
    }

    public Document bytesToDocument(byte[] bytes) {
        return xmlStringToDocument(new String(bytes, StandardCharsets.UTF_8)
        );
    }
}
