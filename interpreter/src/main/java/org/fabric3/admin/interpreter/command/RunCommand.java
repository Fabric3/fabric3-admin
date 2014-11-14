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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;

import org.fabric3.admin.interpreter.Command;
import org.fabric3.admin.interpreter.CommandException;
import org.fabric3.admin.interpreter.Interpreter;
import org.fabric3.admin.interpreter.InterpreterException;

/**
 *
 */
public class RunCommand implements Command {
    private Interpreter interpreter;
    private URL url;

    public RunCommand(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public void setUsername(String username) {
    }

    public void setPassword(String password) {
    }

    public void setFile(URL url) {
        this.url = url;
    }

    public boolean execute(PrintStream out) throws CommandException {
        InputStreamReader reader;
        BufferedReader buffered = null;

        try {
            reader = new InputStreamReader(url.openStream());
            buffered = new BufferedReader(reader);
            String line;
            while ((line = buffered.readLine()) != null) {
                interpreter.process(line, out);
            }
        } catch (IOException | InterpreterException e) {
            throw new CommandException(e);
        } finally {
            if (buffered != null) {
                try {
                    buffered.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


}