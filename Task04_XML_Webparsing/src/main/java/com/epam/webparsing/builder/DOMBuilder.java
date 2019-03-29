package com.epam.webparsing.builder;

import com.epam.webparsing.entity.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class DOMBuilder extends ParserBuilder {
    private DocumentBuilder documentBuilder;

    public DOMBuilder() {
        super();

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory
                .newInstance();
        try {
            documentBuilder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.error("Configuration parser error.", e);
        }
    }

    @Override
    Candie buildCandie(final Candie candie, final Element candieElement) {
        candie.setProduction(candieElement.getAttribute("production"));
        candie.setName(candieElement.getAttribute("name"));
        if (candieElement.getAttribute("filling") != null) {
            candie.setFilling(candieElement.getAttribute("filling"));
        }
        candie.setEnergy(Integer.valueOf(
                getElementTextContent(candieElement, "energy")));
        candie.setDate(getElementTextContent(candieElement, "date"));
        Element ingredientsElement = (Element) candieElement.getElementsByTagName(
                "ingredients").item(0);
        Ingredients ingredients = objectFactory.createIngredients();
        ingredients.setFructose(
                Integer.valueOf(getElementTextContent(
                        ingredientsElement, "fructose")));
        ingredients.setSugar(
                Integer.valueOf(getElementTextContent(
                        ingredientsElement, "sugar")));
        ingredients.setVanillin(
                getElementTextContent(ingredientsElement, "vanillin"));
        ingredients.setWater(Integer.valueOf(getElementTextContent(
                ingredientsElement, "water")));
        candie.setIngredients(ingredients);
        Value value = objectFactory.createValue();
        value.setCarbohydrates(
                Double.valueOf(getElementTextContent(
                        candieElement, "carbohydrates")));
        value.setProtein(
                Double.valueOf(getElementTextContent(
                        candieElement, "protein")));
        value.setFats(
                Double.valueOf(getElementTextContent(
                        candieElement, "fats")));
        candie.setValue(value);
        return candie;
    }

    private Candie buildChocolateCandie(final Element candieElement) {
        ChocolateCandie candie = objectFactory.createChocolateCandie();
        buildCandie(candie, candieElement);
        candie.setChocolateType(
                ChocolateType.fromValue(
                        getElementTextContent(
                                candieElement, "chocolate-type")));
        return candie;
    }

    private Candie buildFruitCandie(final Element candieElement) {
        FruitCandie candie = objectFactory.createFruitCandie();
        buildCandie(candie, candieElement);
        candie.setFruitType(
                FruitType.fromValue(
                        getElementTextContent(
                                candieElement, "fruit-type")));
        return candie;
    }


    @Override
    public void buildCandies(final String fileName) {
        Document document;
        try {
            document = documentBuilder.parse(fileName);
            Element root = document.getDocumentElement();
            NodeList chocolateCandiesList = root.getElementsByTagName("chocolate-candie");
            NodeList fruitCandiesList = root.getElementsByTagName("fruit-candie");
            for (int i = 0; i < chocolateCandiesList.getLength(); i++) {
                Element candieElement = (Element) chocolateCandiesList.item(i);
                candies.add(buildChocolateCandie(candieElement));
            }
            for (int i = 0; i < fruitCandiesList.getLength(); i++) {
                Element candieElement = (Element) fruitCandiesList.item(i);
                candies.add(buildFruitCandie(candieElement));
            }
        } catch (SAXException e) {
            LOGGER.error("File error or I/O error.", e);
        } catch (IOException e) {
            LOGGER.error("Parsing failure.", e);
        }
    }

    private static String getElementTextContent(final Element candieElement,
                                                final String elementName) {
        return candieElement.getElementsByTagName(elementName)
                .item(0)
                .getTextContent();
    }
}