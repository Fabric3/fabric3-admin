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

import java.net.URI;
import java.net.URISyntaxException;

import org.fabric3.admin.interpreter.Command;
import org.fabric3.admin.interpreter.CommandParser;
import org.fabric3.admin.interpreter.ParseException;
import org.fabric3.admin.interpreter.command.UninstallCommand;
import org.fabric3.admin.interpreter.communication.DomainConnection;

/**
 *
 */
public class UninstallCommandParser implements CommandParser {
    private DomainConnection domainConnection;

    public UninstallCommandParser(DomainConnection domainConnection) {
        this.domainConnection = domainConnection;
    }

    public String getUsage() {
        return "uninstall (uin): Removes a stored contribution.\n" +
                "usage: uninstall <contribution file> [-u username -p password]";
    }

    public Command parse(String[] tokens) throws ParseException {
        if (tokens.length != 1 && tokens.length != 5) {
            throw new ParseException("Illegal number of arguments");
        }
        UninstallCommand command = new UninstallCommand(domainConnection);
        try {
            command.setContributionUri(new URI(tokens[0]));
        } catch (URISyntaxException e) {
            throw new ParseException("Invalid contribution name", e);
        }
        if (tokens.length == 5) {
            ParserHelper.parseAuthorization(command, tokens, 1);
        }
        return command;
    }

}