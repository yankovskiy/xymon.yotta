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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ru.neverdark.yotta.parser.YottaParser;
import ru.neverdark.yotta.parser.YottaParser.Disk;

public class Xymon {

    private String executeShellCommand(String command) {
        String[] cmd = { "/bin/bash", "-c", command };
        StringBuilder builder = new StringBuilder();
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(cmd);

            proc.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    builder.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public void run(YottaParser parser) throws ParserConfigurationException, SAXException,
            IOException {
        boolean isBadDisk = false;

        for (String name : parser.getArrays().keySet()) {
            String pageColor = "green";
            StringBuilder html = new StringBuilder();

            for (String enclosure : parser.getEnclosuresDisk(name).keySet()) {
                html.append(String.format(
                        "<div align='center' style='font-size:12pt;'><strong>%s</strong></div>",
                        enclosure));
                html.append("<table width='100%' cellpadding='10' border='0'>");
                html.append("<tr align=center><th></th><th>Slot</th><th>Usage</th><th>Capacity</th><th>Model</th></tr>");
                for (Disk disk : parser.getEnclosuresDisk(name).get(enclosure)) {
                    html.append("<tr align=center>");
                    isBadDisk = disk.getUsage().equals("Failed");
                    if (isBadDisk) {
                        html.append("<td>&red</td>");
                        pageColor = "red";
                    } else {
                        html.append("<td>&green</td>");
                    }

                    html.append(String.format("<td>%s</td>", disk.getSlot()));
                    html.append(String.format("<td>%s</td>", disk.getUsage()));
                    html.append(String.format("<td>%s</td>", disk.getCapacity()));
                    html.append(String.format("<td>%s</td>", disk.getModel()));
                    html.append("<tr>");
                }
                html.append("</table>");
            }
            sendData(html.toString(), name, pageColor);
        }
    }

    private void sendData(String html, String machine, String color)
            throws ParserConfigurationException, SAXException, IOException {
        String command = String.format(
                "$BB $BBDISP \"status %s.disk_status %s `date` Disk status <br><br><br> %s\"",
                machine, color, html);
        executeShellCommand(command);
    }

}
