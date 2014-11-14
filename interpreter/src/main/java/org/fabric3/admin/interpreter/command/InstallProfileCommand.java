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
import java.net.URL;

import org.fabric3.admin.interpreter.Command;
import org.fabric3.admin.interpreter.CommandException;
import org.fabric3.admin.interpreter.communication.CommunicationException;
import org.fabric3.admin.interpreter.communication.DomainConnection;

/**
 *
 */
public class InstallProfileCommand implements Command {
    private DomainConnection domainConnection;
    private URL profile;
    private URI profileUri;
    private String username;
    private String password;

    public InstallProfileCommand(DomainConnection domainConnection) {
        this.domainConnection = domainConnection;
    }

    public void setProfile(URL profile) {
        this.profile = profile;
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
        if (profileUri == null) {
            profileUri = CommandHelper.parseContributionName(profile);
        }
        storeProfile(profile, profileUri, out);
        installProfile(profileUri);
        return true;
    }

    public boolean storeProfile(URL profile, URI uri, PrintStream out) {
        HttpURLConnection connection = null;
        try {
            String path =  "/domain/contributions/profiles/profile/" + uri;
            connection = domainConnection.put(path, profile);
            int code = connection.getResponseCode();
            if (HttpStatus.UNAUTHORIZED.getCode() == code) {
                out.println("ERROR: Not authorized");
                return false;
            } else if (HttpStatus.FORBIDDEN.getCode() == code && "http".equals(connection.getURL().getProtocol())) {
                out.println("ERROR: An attempt was made to connect using HTTP but the domain requires HTTPS.");
                return false;
            } else if (HttpStatus.CONFLICT.getCode() == code) {
                out.println("ERROR: A profile already exists for " + uri);
                return false;
            } else if (HttpStatus.CREATED.getCode() != code) {
                out.println("ERROR: Error storing profile: " + code);
                return false;
            }
            out.println("Installed " + profileUri);
            return true;
        } catch (FileNotFoundException e) {
            out.println("ERROR: File not found:" + e.getMessage());
            return false;
        }catch (CommunicationException e) {
            out.println("ERROR: Error connecting to domain");
            e.printStackTrace(out);
            return false;
        } catch (IOException e) {
            out.println("ERROR: Error installing profile");
            out.println("       " + e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    public boolean installProfile(URI uri) {
        // NO-OP for now
        return true;
    }

}