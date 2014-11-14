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
import org.fabric3.admin.interpreter.ParseException;
import org.fabric3.admin.interpreter.command.BackCommand;
import org.fabric3.admin.interpreter.communication.DomainConnection;

/**
 *
 */
public class BackCommandParser implements CommandParser {
    private DomainConnection domainConnection;

    public BackCommandParser(DomainConnection domainConnection) {
        this.domainConnection = domainConnection;
    }

    public String getUsage() {
        return "use: Pops the management address.\n usage: back";
    }

    public Command parse(String[] tokens) throws ParseException {
        if (tokens.length != 0) {
            throw new ParseException("Illegal number of arguments");
        }
        return new BackCommand(domainConnection);
    }

}