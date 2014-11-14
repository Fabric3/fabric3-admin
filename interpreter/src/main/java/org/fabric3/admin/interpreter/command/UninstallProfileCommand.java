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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URI;

import org.fabric3.admin.interpreter.Command;
import org.fabric3.admin.interpreter.CommandException;
import org.fabric3.admin.interpreter.communication.CommunicationException;
import org.fabric3.admin.interpreter.communication.DomainConnection;

/**
 *
 */
public class UninstallProfileCommand implements Command {
    private DomainConnection domainConnection;
    private URI profileUri;
    private String username;
    private String password;

    public UninstallProfileCommand(DomainConnection domainConnection) {
        this.domainConnection = domainConnection;
    }

    public void setProfileUri(URI uri) {
        this.profileUri = uri;
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
        removeProfile(profileUri, out);
        out.println("Uninstalled " + profileUri);
        return true;
    }

    public boolean removeProfile(URI uri, PrintStream out) {
        String path =  "/domain/contributions/profiles/profile/" + uri;
        HttpURLConnection connection = null;
        try {
            connection = domainConnection.createConnection(path, "DELETE");
            connection.connect();
            int code = connection.getResponseCode();
            if (HttpStatus.UNAUTHORIZED.getCode() == code) {
                out.println("ERROR: Not authorized");
                return false;
            } else if (HttpStatus.FORBIDDEN.getCode() == code && "http".equals(connection.getURL().getProtocol())) {
                out.println("ERROR: An attempt was made to connect using HTTP but the domain requires HTTPS.");
                return false;
            } else if (HttpStatus.NOT_FOUND.getCode() == code) {
                out.println("ERROR: Profile not found: " + uri);
                return false;
            } else if (HttpStatus.OK.getCode() != code) {
                out.println("ERROR: Error uninstalling profile: " + code);
                return false;
            }
            out.println("Profile uninstalled");
            return true;
        } catch (FileNotFoundException e) {
            out.println("ERROR: File not found:" + e.getMessage());
            return false;
        } catch (IOException e) {
            out.println("ERROR: removing profile:" + e.getMessage());
            return false;
        } catch (CommunicationException e) {
            out.println("ERROR: removing profile:" + e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


}