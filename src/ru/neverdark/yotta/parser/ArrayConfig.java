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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ArrayConfig extends AbstractConfig {
    private static final String TAG_NAME = "array";
    private static final String NAME = "name";
    private static final String IP = "ip";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final String TYPE = "type";

    public static class Array {
        private String mName;
        private String mIp;
        private String mUser;
        private String mPassword;
        private String mType;

        public String getType() {
            return mType;
        }

        public void setType(String type) {
            mType = type;
        }

        public String getName() {
            return mName;
        }

        public String getIp() {
            return mIp;
        }

        public String getUser() {
            return mUser;
        }

        public String getPassword() {
            return mPassword;
        }

        public void setPassword(String password) {
            mPassword = password;
        }

        public void setUser(String user) {
            mUser = user;
        }

        public void setIp(String ip) {
            mIp = ip;
        }

        public void setName(String name) {
            mName = name;
        }
    }

    private List<Array> mArrays = new ArrayList<Array>();;

    public List<Array> getArrays() {
        return mArrays;
    }

    @Override
    public void readConfig(Document doc) {
        NodeList nodes = doc.getElementsByTagName(TAG_NAME);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                Array array = new Array();
                array.setIp(getValue(IP, element));
                array.setName(getValue(NAME, element));
                array.setPassword(getValue(PASSWORD, element));
                array.setUser(getValue(USER, element));
                array.setType(getValue(TYPE, element));
                mArrays.add(array);
            }
        }
    }

}
