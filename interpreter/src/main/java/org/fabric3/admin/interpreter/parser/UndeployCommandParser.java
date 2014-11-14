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
import org.fabric3.admin.interpreter.command.UndeployCommand;
import org.fabric3.admin.interpreter.communication.DomainConnection;

/**
 *
 */
public class UndeployCommandParser implements CommandParser {
    private static final String FORCE = "-force";
    private static final String FORCE_ABBREVIATED = "-f";

    private DomainConnection domainConnection;

    public UndeployCommandParser(DomainConnection domainConnection) {
        this.domainConnection = domainConnection;
    }

    public String getUsage() {
        return "undeploy (ude): Undeploys a contribution.\n" +
                "usage: undeploy <contribution> [-force (-f)] [-u username -p password]";
    }

    public Command parse(String[] tokens) throws ParseException {
        if (tokens.length != 1 && tokens.length != 2 && tokens.length != 5 && tokens.length != 6) {
            throw new ParseException("Illegal number of arguments");
        }
        UndeployCommand command = new UndeployCommand(domainConnection);
        try {
            command.setContributionUri(new URI(tokens[0]));
        } catch (URISyntaxException e) {
            throw new ParseException("Invalid contribution name", e);
        }
        if (tokens.length == 2) {
            parseForce(tokens[1], command);
        } else if (tokens.length == 5) {
            ParserHelper.parseAuthorization(command, tokens, 1);
        } else if (tokens.length == 6) {
            parseForce(tokens[1], command);
            ParserHelper.parseAuthorization(command, tokens, 2);
        }
        return command;
    }

    private void parseForce(String token, UndeployCommand command) throws ParseException {
        if (FORCE.equals(token) || FORCE_ABBREVIATED.equals(token)) {
            command.setForce(true);
        } else {
            throw new ParseException("Unrecognized option: " + token);
        }

    }

}