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
package org.fabric3.admin.interpreter;

import java.io.PrintStream;

/**
 * Commands are constructed by a CommandParser that walks the AST generated from a instructions submitted to the Interpreter. Typically, Commands
 * operate against the DomainController.
 */
public interface Command {

    void setUsername(String username);

    void setPassword(String password);

    /**
     * Executes the command.
     *
     * @param out the PrintStream where command output is sent
     * @return true if the command completed successfully, false of there was an error
     * @throws CommandException if  there is an exception executing the command
     */
    boolean execute(PrintStream out) throws CommandException;

}
