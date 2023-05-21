package com.yaniv.bookshelf.bookutils;

import lombok.SneakyThrows;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FB2Utils {

    private FB2Utils() {
    }

    @SneakyThrows
    public static List<Chapter> getTitles(InputStream resourse) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(resourse);
        boolean isBody = false;
        boolean isTitle = false;
        boolean isSection = false;
        List<Chapter> chapters = new ArrayList<>();
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                try {
                    switch (startElement.getName().getLocalPart()) {
                        case "body" -> isBody = true;
                        case "section" -> {
                            if (isBody) {
                                isSection = true;
                            }
                        }
                        case "title" -> {
                            if (isSection) {
                                isTitle = true;
                                chapters.add(new Chapter());
                            }
                        }
                        case "p" -> {
                            nextEvent = reader.nextEvent();
                            Chapter lastChapter = chapters.get(chapters.size() - 1);
                            String nextLine = nextEvent.asCharacters().getData();
                            if (isTitle) {
                                if (nextLine == null) {
                                    chapters.remove(lastChapter);
                                } else {
                                    lastChapter.setTitle(nextLine);
                                }
                            } else {
                                lastChapter.getParagraphs().add(nextLine);
                            }
                        }
                    }
                } catch (Exception ignored){}
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                switch (endElement.getName().getLocalPart()) {
                    case "body" -> isBody = false;
                    case "section" -> isSection = false;
                    case "title" -> isTitle = false;
                }
            }
        }
        return chapters;
    }


    //index starts from 1
    @SneakyThrows
    public static Chapter getChapter(int index, InputStream resourse) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(resourse);
        boolean isBody = false;
        boolean isTitle = false;
        boolean isSection = false;
        Chapter chapter = new Chapter();
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                try {
                    switch (startElement.getName().getLocalPart()) {
                        case "body" -> isBody = true;
                        case "section" -> {
                            if (isBody) {
                                isSection = true;
                            }
                        }
                        case "title" -> {
                            if (isSection) {
                                isTitle = true;
                                index--;
                            }
                        }
                        case "p" -> {
                            nextEvent = reader.nextEvent();
                            if (isTitle) {
                                String nextLine = nextEvent.asCharacters().getData();
                                if (nextLine == null) {
                                    index++;
                                } else if (index == 0) {
                                    chapter.setTitle(nextLine);
                                }
                            } else {
                                if (index == 0) {
                                    chapter.getParagraphs().add(nextEvent.asCharacters().getData());
                                }
                            }
                        }
                    }
                } catch (Exception ignored) { }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                switch (endElement.getName().getLocalPart()) {
                    case "body" -> isBody = false;
                    case "section" -> isSection = false;
                    case "title" -> isTitle = false;
                }
            }
        }
        return chapter;
    }
}

