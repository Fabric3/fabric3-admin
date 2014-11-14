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
import java.net.URL;

import org.fabric3.admin.interpreter.Command;
import org.fabric3.admin.interpreter.CommandParser;
import org.fabric3.admin.interpreter.Interpreter;
import org.fabric3.admin.interpreter.ParseException;
import org.fabric3.admin.interpreter.command.RunCommand;

/**
 *
 */
public class RunCommandParser implements CommandParser {
    private Interpreter interpreter;

    public RunCommandParser(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public String getUsage() {
        return "run (r): Runs an admin script.\n" +
                "usage: run <script file>";
    }

    public Command parse(String[] tokens) throws ParseException {
        if (tokens.length != 1) {
            throw new ParseException("Illegal number of arguments");
        }
        RunCommand command = new RunCommand(interpreter);
        try {
            URL file = ParserHelper.parseUrl(tokens[0]);
            command.setFile(file);
            return command;
        } catch (MalformedURLException e) {
            throw new ParseException("Invalid contribution URL", e);
        }
    }

}