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
package org.fabric3.admin.interpreter.parser;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.fabric3.admin.interpreter.Command;
import org.fabric3.admin.interpreter.CommandParser;
import org.fabric3.admin.interpreter.ParseException;
import org.fabric3.admin.interpreter.command.InstallProfileCommand;
import org.fabric3.admin.interpreter.command.UninstallProfileCommand;
import org.fabric3.admin.interpreter.communication.DomainConnection;

/**
 *
 */
public class ProfileCommandParser implements CommandParser {
    private DomainConnection domainConnection;

    public ProfileCommandParser(DomainConnection domainConnection) {
        this.domainConnection = domainConnection;
    }

    public String getUsage() {
        return "profile (pf): Installs a profile to the domain repository.\n" +
                "usage: profile [install | uninstall] <profile> [-u username -p password]";
    }

    public Command parse(String[] tokens) throws ParseException {
        if (tokens.length != 2 && tokens.length != 6) {
            throw new ParseException("Illegal number of arguments");
        }
        if ("install".equals(tokens[0])) {
            return install(tokens);
        } else if ("uninstall".equals(tokens[0])) {
            return uninstall(tokens);
        } else {
            throw new ParseException("Unknown profile command: " + tokens[1]);
        }
    }

    private Command uninstall(String[] tokens) throws ParseException {
        UninstallProfileCommand command = new UninstallProfileCommand(domainConnection);
        try {
            command.setProfileUri(new URI(tokens[1]));
        } catch (URISyntaxException e) {
            throw new ParseException("Invalid profile name", e);
        }
        if (tokens.length == 6) {
            ParserHelper.parseAuthorization(command, tokens, 2);
        }
        return command;
    }

    private Command install(String[] tokens) throws ParseException {
        InstallProfileCommand command = new InstallProfileCommand(domainConnection);
        try {
            URL url = ParserHelper.parseUrl(tokens[1]);
            command.setProfile(url);
        } catch (MalformedURLException e) {
            throw new ParseException("Invalid profile URL", e);
        }
        if (tokens.length == 6) {
            ParserHelper.parseAuthorization(command, tokens, 2);
        }
        return command;
    }

}