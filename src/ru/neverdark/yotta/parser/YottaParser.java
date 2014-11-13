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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import ru.neverdark.yotta.parser.ArrayConfig;
import ru.neverdark.yotta.parser.ArrayConfig.Array;
import ru.neverdark.yotta.parser.Config;

public class YottaParser {
    public static class Disk {
        private String mSlot;
        private String mUsage;
        private String mCapacity;
        private String mModel;

        public String getModel() {
            return mModel;
        }

        public void setModel(String model) {
            mModel = model;
        }

        public String getSlot() {
            return mSlot;
        }

        public void setSlot(String slot) {
            mSlot = slot;
        }

        public String getUsage() {
            return mUsage;
        }

        public void setUsage(String usage) {
            mUsage = usage;
        }

        public String getCapacity() {
            return mCapacity;
        }

        public void setCapacity(String capacity) {
            mCapacity = capacity;
        }
    }

    private Map<String, List<Disk>> mEnclosuresDisk;
    private final Map<String, Map<String, List<Disk>>> mArrays = new LinkedHashMap<String, Map<String, List<Disk>>>();

    public void parseAll(String configFile) throws ParserConfigurationException, SAXException,
            IOException {

        ArrayConfig arrayConf = Config.getInstance(configFile).getArrayConfig();
        for (Array array : arrayConf.getArrays()) {
            mEnclosuresDisk = new LinkedHashMap<String, List<Disk>>();
            parse(array);
            mArrays.put(array.getName(), mEnclosuresDisk);
        }
    }

    private void parse(Array array) {
        final String URL = String.format("http://%s/hierarch.htm", array.getIp());
        final StringBuffer result = new StringBuffer();

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(array.getIp(), 80),
                new UsernamePasswordCredentials(array.getUser(), array.getPassword()));
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider).build();
        try {
            HttpGet httpget = new HttpGet(URL);
            CloseableHttpResponse response = httpClient.execute(httpget);
            System.err.printf("%s\t%s\n", array.getIp(), response.getStatusLine());
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity()
                        .getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

                Document doc = Jsoup.parse(result.toString());
                Elements tables = doc.getElementsByAttribute("vspace");
                // skip first
                for (int i = 1; i < tables.size(); i++) {
                    parseTable(tables.get(i), array.getType());
                }

            } finally {
                response.close();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void parseTable(Element table, String arrayType) {
        String array = null;

        if (arrayType.equals("YB-16S3EF8")) {
            array = table.getElementsByAttributeValue("colspan", "6").get(0).text();
        } else if (arrayType.equals("Y3-24S6DF8")) {
            array = table.getElementsByAttributeValue("colspan", "9").get(0).text();
        } else if (arrayType.equals("Y3-16S6SF8p")) {
            array = table.getElementsByAttributeValue("colspan", "10").get(0).text();
        }

        List<Disk> disks = new ArrayList<Disk>();

        Elements trs = table.getElementsByAttributeValue("bgcolor", "FFFFDB");
        for (Element tr : trs) {
            Elements tds = tr.getElementsByTag("td");
            String slot = tds.get(0).text();
            String usage = tds.get(1).text();
            String capacity = tds.get(2).text();
            String model = tds.get(3).text();

            Disk disk = new Disk();
            disk.setSlot(slot);
            disk.setUsage(usage);
            disk.setCapacity(capacity);
            disk.setModel(model);

            disks.add(disk);
        }
        mEnclosuresDisk.put(array, disks);
    }

    public Map<String, List<Disk>> getEnclosuresDisk(String ip) {
        return mArrays.get(ip);
    }

    public Map<String, Map<String, List<Disk>>> getArrays() {
        return mArrays;
    }
}
