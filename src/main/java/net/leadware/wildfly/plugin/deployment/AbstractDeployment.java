package net.leadware.wildfly.plugin.deployment;

/*
 * #%L
 * wildfly-maven-plugin Mojo
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2013 - 2014 Leadware
 * %%
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
 * #L%
 */

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.domain.DomainClient;
import org.wildfly.plugin.cli.Commands;
import org.wildfly.plugin.common.AbstractServerConnection;
import org.wildfly.plugin.common.DeploymentExecutionException;
import org.wildfly.plugin.common.DeploymentFailureException;
import org.wildfly.plugin.deployment.Deployment;
import org.wildfly.plugin.deployment.Deployment.Status;
import org.wildfly.plugin.deployment.MatchPatternStrategy;
import org.wildfly.plugin.deployment.domain.Domain;
import org.wildfly.plugin.deployment.domain.DomainDeployment;
import org.wildfly.plugin.deployment.standalone.StandaloneDeployment;

/**
 * Classe de base des MOJO de deploiement 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Enterprise Architect)</a>
 * @since 2014-07-27 15:35:04
 */
public abstract class AbstractDeployment extends AbstractServerConnection {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    /**
     * Specifies the configuration for a domain server.
     */
    @Parameter
    private Domain domain;

    /**
     * Specifies the name used for the deployment.
     */
    @Parameter
    protected String name;

    /**
     * Commands to run before the deployment
     */
    @Parameter(alias = "before-deployment")
    private Commands beforeDeployment;

    /**
     * Executions to run after the deployment
     */
    @Parameter(alias = "after-deployment")
    private Commands afterDeployment;

    /**
     * Set to {@code true} if you want the deployment to be skipped, otherwise {@code false}.
     */
    @Parameter(defaultValue = "false")
    private boolean skip;

    /**
     * The archive file.
     *
     * @return the archive file.
     */
    protected abstract File file();

    /**
     * The goal of the deployment.
     *
     * @return the goal of the deployment.
     */
    public abstract String goal();

    /**
     * The type of the deployment.
     *
     * @return the deployment type.
     */
    public abstract Deployment.Type getType();

    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().debug(String.format("Skipping deployment of %s:%s", project.getGroupId(), project.getArtifactId()));
            return;
        }
        doExecute();
    }

    protected final Status executeDeployment(final ModelControllerClient client, final Deployment deployment)
            throws DeploymentExecutionException, DeploymentFailureException, IOException {
        // Execute before deployment commands
        if (beforeDeployment != null)
            beforeDeployment.execute(client);
        // Deploy the deployment
        getLog().debug("Executing deployment");
        final Status status = deployment.execute();
        // Execute after deployment commands
        if (afterDeployment != null)
            afterDeployment.execute(client);
        return status;
    }

    /**
     * Executes additional processing.
     *
     * @see #execute()
     */
    protected void doExecute() throws MojoExecutionException, MojoFailureException {
        try {
            synchronized (CLIENT_LOCK) {
                validate();
                final ModelControllerClient client = getClient();
                final String matchPattern = getMatchPattern();
                final MatchPatternStrategy matchPatternStrategy = getMatchPatternStrategy();
                final Deployment deployment;
                if (isDomainServer()) {
                    deployment = DomainDeployment.create((DomainClient) client, domain, file(), name, getType(), matchPattern, matchPatternStrategy);
                } else {
                    deployment = StandaloneDeployment.create(client, file(), name, getType(), matchPattern, matchPatternStrategy);
                }
                switch (executeDeployment(client, deployment)) {
                    case REQUIRES_RESTART: {
                        getLog().info("Server requires a restart");
                        break;
                    }
                    case SUCCESS:
                        break;
                }
            }
        } catch (MojoFailureException e) {
            throw e;
        } catch (MojoExecutionException e) {
            throw e;
        } catch (Exception e) {
            throw new MojoExecutionException(String.format("Could not execute goal %s on %s. Reason: %s", goal(), file(),
                    e.getMessage()), e);
        } finally {
            close();
        }
    }

    /**
     * Returns the matching pattern for undeploy and redeploy goals. By default {@code null} is returned.
     *
     * @return the pattern or {@code null}
     */
    protected String getMatchPattern() {
        return null;
    }

    /**
     * Returns the matching pattern strategy to use if more than one deployment matches the {@link #getMatchPattern()
     * pattern} returns more than one instance of a deployment. By default {@code null} is returned.
     *
     * @return the matching strategy or {@code null}
     */
    protected MatchPatternStrategy getMatchPatternStrategy() {
        return null;
    }

    /**
     * Validates the deployment.
     *
     * @throws DeploymentFailureException if the deployment is invalid.
     */
    protected void validate() throws DeploymentFailureException {
        if (isDomainServer()) {
            if (domain == null || domain.getServerGroups().isEmpty()) {
                throw new DeploymentFailureException(
                        "Server is running in domain mode, but no server groups have been defined.");
            }
        } else if (domain != null && !domain.getServerGroups().isEmpty()) {
            throw new DeploymentFailureException("Server is running in standalone mode, but server groups have been defined.");
        }
    }
}
