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

import java.io.InputStream;
import java.io.PrintStream;

/**
 *
 */
public interface Interpreter {

    /**
     * Processes an instruction.
     *
     * @param command the instruction
     * @param out     the PrintStream where command output is sent
     * @throws InterpreterException if an error occurs processing the instruction
     */
    public void process(String command, PrintStream out) throws InterpreterException;

    /**
     * Provides an interactive command prompt for issuing commands to the DomainController.
     *
     * @param in  the InputStream where instructions are received
     * @param out the PrintStream where command output is sent
     */
    void processInteractive(InputStream in, PrintStream out);

}
