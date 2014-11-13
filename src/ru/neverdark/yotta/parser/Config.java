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

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Config {
    private static Config mConfigParser;
    private final String mConfigFile;
    private ApplicationConfig mApplicationConfig;
    private ArrayConfig mArrayConfig;

    public static Config getInstance(String configFile) throws ParserConfigurationException,
            SAXException, IOException {
        if (mConfigParser == null) {
            mConfigParser = new Config(configFile);
        }

        return mConfigParser;
    }

    private Config(String configFile) throws ParserConfigurationException, SAXException,
            IOException {
        mConfigFile = configFile;
        mArrayConfig = new ArrayConfig();
        mApplicationConfig = new ApplicationConfig();
        readConfig();
    }

    private void readConfig() throws ParserConfigurationException, SAXException, IOException {
        File config = new File(mConfigFile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(config);
        document.getDocumentElement().normalize();

        mApplicationConfig.readConfig(document);
        mArrayConfig.readConfig(document);
    }

    public ApplicationConfig getApplicationConfig() {
        return mApplicationConfig;
    }

    public ArrayConfig getArrayConfig() {
        return mArrayConfig;
    }
}
