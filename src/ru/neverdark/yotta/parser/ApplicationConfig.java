/*******************************************************************************
 * Copyright (C) 2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *  
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *  
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package ru.neverdark.yotta.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ApplicationConfig extends AbstractConfig {
    private static final String BBDISP = "bbdisp";
    private static final String BBPORT = "bbport";
    private static final String BB = "bb";
    private static final String TAG_NAME = "application";
    private String mBbdisp;
    private String mBbport;
    private String mBb;

    @Override
    public void readConfig(Document doc) {
        NodeList nodes = doc.getElementsByTagName(TAG_NAME);

        Node node = nodes.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            mBbdisp = getValue(BBDISP, element);
            mBbport = getValue(BBPORT, element);
            mBb = getValue(BB, element);
        }
    }

    public String getBbdisp() {
        return mBbdisp;
    }

    public String getBbport() {
        return mBbport;
    }

    public String getBb() {
        return mBb;
    }

}
