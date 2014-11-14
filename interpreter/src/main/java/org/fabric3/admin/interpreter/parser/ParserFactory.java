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

import java.util.HashMap;
import java.util.Map;

import org.fabric3.admin.interpreter.CommandParser;
import org.fabric3.admin.interpreter.Interpreter;
import org.fabric3.admin.interpreter.Settings;
import org.fabric3.admin.interpreter.communication.DomainConnection;

/**
 *
 */
public class ParserFactory {

    private ParserFactory() {
    }

    public static Map<String, CommandParser> createParsers(DomainConnection domainConnection, Interpreter interpreter, Settings settings) {
        Map<String, CommandParser> parsers = new HashMap<>();
        AuthCommandParser authenticateParser = new AuthCommandParser(domainConnection);
        parsers.put("au", authenticateParser);
        parsers.put("authenticate", authenticateParser);
        BackCommandParser backParser = new BackCommandParser(domainConnection);
        parsers.put("back", backParser);
        parsers.put("b", backParser);
        FollowCommandParser followParser = new FollowCommandParser(domainConnection);
        parsers.put("follow", followParser);
        parsers.put("f", followParser);
        GetCommandParser getParser = new GetCommandParser(domainConnection);
        parsers.put("get", getParser);
        parsers.put("g", getParser);
        InstallCommandParser installParser = new InstallCommandParser(domainConnection);
        parsers.put("install", installParser);
        parsers.put("ins", installParser);
        StatCommandParser statusParser = new StatCommandParser(domainConnection);
        parsers.put("status", statusParser);
        parsers.put("st", statusParser);
        DeployCommandParser deployParser = new DeployCommandParser(domainConnection);
        parsers.put("deploy", deployParser);
        parsers.put("de", deployParser);
        UndeployCommandParser undeployParser = new UndeployCommandParser(domainConnection);
        parsers.put("undeploy", undeployParser);
        parsers.put("ude", undeployParser);
        UninstallCommandParser uninstallParser = new UninstallCommandParser(domainConnection);
        parsers.put("uninstall", uninstallParser);
        parsers.put("uin", uninstallParser);
        parsers.put("use", new UseCommandParser(domainConnection, settings));
        PostCommandParser postParser = new PostCommandParser(domainConnection);
        parsers.put("p", postParser);
        parsers.put("post", postParser);
        ProvisionCommandParser provisionParser = new ProvisionCommandParser(domainConnection);
        parsers.put("pr", provisionParser);
        parsers.put("provision", provisionParser);
        ListCommandParser listCommandParser = new ListCommandParser(domainConnection);
        parsers.put("ls", listCommandParser);
        parsers.put("list", listCommandParser);
        ProfileCommandParser profileCommandParser = new ProfileCommandParser(domainConnection);
        parsers.put("profile", profileCommandParser);
        parsers.put("pf", profileCommandParser);
        RunCommandParser runCommandParser = new RunCommandParser(interpreter);
        parsers.put("run", runCommandParser);
        parsers.put("r", runCommandParser);

        return parsers;
    }

}
