/*
 * Fabric3
 * Copyright (c) 2009-2015 Metaform Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fabric3.admin.interpreter.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.fabric3.admin.interpreter.DomainConfiguration;
import org.fabric3.admin.interpreter.Settings;

/**
 * An implementation that stores settings to a properties file.
 */
public class FileSettings implements Settings {
    private File file;
    private Map<String, DomainConfiguration> domains = new HashMap<>();

    public FileSettings(File file) {
        this.file = file;
    }

    public void addConfiguration(DomainConfiguration configuration) {
        domains.put(configuration.getName(), configuration);
    }

    public DomainConfiguration getDomainConfiguration(String name) {
        return domains.get(name);
    }

    public List<DomainConfiguration> getDomainConfigurations() {
        return new ArrayList<>(domains.values());
    }

    public void save() throws IOException {
        throw new UnsupportedOperationException();
    }

    public void load() throws IOException {

        if (!file.exists()) {
            return;
        }
        InputStream stream = null;
        XMLStreamReader reader = null;
        try {
            domains.clear();
            stream = new FileInputStream(file);
            try {
                reader = XMLInputFactory.newInstance().createXMLStreamReader(stream);
                while (true) {
                    int val = reader.next();
                    switch (val) {
                    case (XMLStreamConstants.START_ELEMENT):
                        if ("domain".equals(reader.getLocalName())) {
                            String name = reader.getAttributeValue(null, "name");
                            String url = reader.getAttributeValue(null, "url");
                            String username = reader.getAttributeValue(null, "username");
                            String password = reader.getAttributeValue(null, "password");
                            DomainConfiguration configuration = new DomainConfiguration(name, url, username, password);
                            addConfiguration(configuration);
                            break;
                        }
                        break;
                    case (XMLStreamConstants.END_DOCUMENT):
                        return;
                    }
                }
            } catch (XMLStreamException e) {
                throw new IOException(e);
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }
            if (stream != null) {
                stream.close();
            }
        }
    }
}
