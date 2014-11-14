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

import org.fabric3.admin.interpreter.Command;
import org.fabric3.admin.interpreter.CommandParser;
import org.fabric3.admin.interpreter.DomainConfiguration;
import org.fabric3.admin.interpreter.ParseException;
import org.fabric3.admin.interpreter.Settings;
import org.fabric3.admin.interpreter.command.UseCommand;
import org.fabric3.admin.interpreter.communication.DomainConnection;

/**
 *
 */
public class UseCommandParser implements CommandParser {
    private DomainConnection domainConnection;
    private Settings settings;

    public UseCommandParser(DomainConnection domainConnection, Settings settings) {
        this.domainConnection = domainConnection;
        this.settings = settings;
    }

    public String getUsage() {
        return "use: Sets the working domain.\n usage: use <domain>";
    }

    public Command parse(String[] tokens) throws ParseException {
        if (tokens.length != 0 && tokens.length != 1) {
            throw new ParseException("Illegal number of arguments");
        }

        if (tokens.length == 0) {
            return new UseCommand(domainConnection);
        } else {
            UseCommand command = new UseCommand(domainConnection);
            String domain = tokens[0];
            DomainConfiguration configuration = settings.getDomainConfiguration(domain);
            if (configuration == null) {
                throw new UnknownDomainException("The domain has not been configured: " + domain);
            }
            command.setAlias(domain);
            command.setAddress(configuration.getAddress());
            command.setUsername(configuration.getUsername());
            command.setPassword(configuration.getPassword());
            return command;
        }
    }

}