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
public class StatCommand implements Command {
    private DomainConnection domainConnection;
    private String username;
    private String password;

    public StatCommand(DomainConnection domainConnection) {
        this.domainConnection = domainConnection;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
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
            connection = domainConnection.createConnection("/domain/contributions", "GET");
            connection.connect();
            int code = connection.getResponseCode();
            if (HttpStatus.UNAUTHORIZED.getCode() == code) {
                out.println("ERROR: Not authorized");
                return false;
            } else if (HttpStatus.FORBIDDEN.getCode() == code && "http".equals(connection.getURL().getProtocol())) {
                out.println("ERROR: Attempt made to connect using HTTP but the domain requires HTTPS.");
                return false;
            } else if (HttpStatus.NOT_FOUND.getCode() == code) {
                out.println("ERROR: Invalid domain address: " + connection.getURL());
                return false;
            } else if (HttpStatus.OK.getCode() != code) {
                out.println("ERROR: Server error: " + code);
                return false;
            }
            InputStream stream = connection.getInputStream();
            Map<String, List<Map<String, String>>> value = domainConnection.parse(Map.class, stream);

            out.println("Contributions:\n");
            List<Map<String, String>> contributions = value.get("contributions");
            for (Map<String, String> contribution : contributions) {
                out.printf("%-65s %s \n", contribution.get("uri"), contribution.get("state"));
            }
            return true;
        } catch (IOException e) {
            out.println("ERROR: Error connecting to domain controller");
            e.printStackTrace(out);
            return false;
        } catch (CommunicationException e) {
            out.println("ERROR: Error connecting to domain controller");
            e.printStackTrace(out);
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}