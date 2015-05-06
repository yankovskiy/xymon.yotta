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
package ru.neverdark.yotta;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import ru.neverdark.yotta.parser.YottaParser;

public class Main {

    public static void main(String[] argv) throws ClientProtocolException, IOException,
            ParserConfigurationException, SAXException {
        String configFile = "/tmp/config.xml";

        if (argv.length > 0) {
            configFile = argv[0];
        }

        YottaParser parser = new YottaParser();
        parser.parseAll(configFile);

        Xymon xymonClient = new Xymon();
        xymonClient.run(parser);

    }

}
