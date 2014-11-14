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

import java.io.PrintStream;
import java.net.URI;
import java.net.URL;

import org.fabric3.admin.interpreter.Command;
import org.fabric3.admin.interpreter.CommandException;
import org.fabric3.admin.interpreter.communication.DomainConnection;

/**
 *
 */
public class ProvisionCommand implements Command {
    private DeployCommand deployCommand;
    private InstallCommand installCommand;

    public ProvisionCommand(DomainConnection domainConnection) {
        installCommand = new InstallCommand(domainConnection);
        deployCommand = new DeployCommand(domainConnection);
    }

    public void setUsername(String username) {
        installCommand.setUsername(username);
    }

    public void setPassword(String password) {
        installCommand.setPassword(password);
    }

    public void setContribution(URL contribution) {
        installCommand.setContribution(contribution);
        URI contributionUri = CommandHelper.parseContributionName(contribution);
        installCommand.setContributionUri(contributionUri);
        deployCommand.setContributionUri(contributionUri);
    }

    public boolean execute(PrintStream out) throws CommandException {
        return installCommand.execute(out) && deployCommand.execute(out);
    }


}