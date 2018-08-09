package com.example.joseph.hw9;

/**
 * Created by Joseph on 11/27/17.
 */

import android.util.Log;

import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XMLPullParserHandler {

    private ArrayList<NewsColumn> newsColumns= new ArrayList<>();
    private NewsColumn newsColumn;
    private String text;

    public ArrayList<NewsColumn> parse(InputStream is) {
        try {
            newsColumn = new NewsColumn(" "," "," "," ");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser  parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            String ifArticle = "";
//            int count = 0;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("item")) {
                            // create a new instance of employee
                            newsColumn = new NewsColumn(" "," "," "," ");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("item")) {
                            // add employee object to list
                            if (ifArticle.contains("article")) {
                                newsColumns.add(newsColumn);
                            }
                        }else if (tagname.equalsIgnoreCase("title")) {
                            newsColumn.setTitle(text);
                        }  else if (tagname.equalsIgnoreCase("author_name")) {
                            newsColumn.setAuthor("Author: " + text);
                        } else if (tagname.equalsIgnoreCase("pubDate")) {
                            newsColumn.setDate("Date: " + text.substring(0, 26) + "PDT");
                        } else if (tagname.equalsIgnoreCase("link")) {
                            newsColumn.setLink(text);
                            ifArticle = text;
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return newsColumns;
    }
}


