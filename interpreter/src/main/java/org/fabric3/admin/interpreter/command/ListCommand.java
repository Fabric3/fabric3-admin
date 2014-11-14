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
package org.fabric3.admin.interpreter.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import org.fabric3.admin.interpreter.Command;
import org.fabric3.admin.interpreter.CommandException;
import org.fabric3.admin.interpreter.communication.CommunicationException;
import org.fabric3.admin.interpreter.communication.DomainConnection;

/**
 *
 */
public class ListCommand implements Command {
    private DomainConnection domainConnection;
    private String path;
    private String username;
    private String password;

    public ListCommand(DomainConnection domainConnection) {
        this.domainConnection = domainConnection;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean execute(PrintStream out) throws CommandException {
        if (username != null) {
            domainConnection.setUsername(username);
        }
        if (password != null) {
            domainConnection.setPassword(password);
        }
        HttpURLConnection connection = null;
        try {
            if (path == null) {
                connection = domainConnection.createConnection("/domain/components", "GET");
            } else {
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
                connection = domainConnection.createConnection("/domain/components" + path, "GET");
            }
            connection.connect();
            int code = connection.getResponseCode();
            if (HttpStatus.UNAUTHORIZED.getCode() == code) {
                out.println("ERROR: Not authorized");
                return false;
            } else if (HttpStatus.FORBIDDEN.getCode() == code && "http".equals(connection.getURL().getProtocol())) {
                out.println("ERROR: Attempt made to connect using HTTP but the domain requires HTTPS.");
                return false;
            } else if (HttpStatus.NOT_FOUND.getCode() == code) {
                out.println("No components found");
                return false;
            } else if (HttpStatus.OK.getCode() != code) {
                out.println("ERROR: Server error: " + code);
                return false;
            }
            InputStream stream = connection.getInputStream();
            Map<String, List<Map<String, String>>> value = domainConnection.parse(Object.class, stream);

            if (value.get("components") == null) {
                // single component returned
                out.println("   " + value.get("uri") + " [Zone: " + value.get("zone") + "]");
            } else {
                List<Map<String, String>> components = value.get("components");
                if (components.isEmpty()) {
                    out.println("No components found");
                } else {
                    out.println("Components:\n");
                    for (Map<String, String> component : components) {
                        out.println("   " + component.get("uri") + " [Zone: " + component.get("zone") + "]");

                    }
                }
            }
            return true;
        } catch (CommunicationException e) {
            out.println("ERROR: Error connecting to domain");
            e.printStackTrace(out);
            return false;
        } catch (IOException e) {
            out.println("ERROR: Error connecting to domain");
            e.printStackTrace(out);
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


}