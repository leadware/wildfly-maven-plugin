/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.wildfly.plugin.common.DeploymentFailureException;
import org.wildfly.plugin.common.PropertyNames;
import org.wildfly.plugin.deployment.Deployment.Type;

/**
 * Undeploys (removes) an arbitrary artifact to the WildFly application server
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@Mojo(name = "undeploy-artifact", requiresDependencyResolution = ResolutionScope.RUNTIME, threadSafe = true)
public final class UndeployArtifactMojo extends AbstractDeployment {

    /**
     * The artifact to deploys groupId
     */
    @Parameter
    private String groupId;


    /**
     * The artifact to deploys artifactId
     */
    @Parameter
    private String artifactId;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * Indicates whether undeploy should ignore the undeploy operation if the deployment does not exist.
     */
    @Parameter(defaultValue = "true", property = PropertyNames.IGNORE_MISSING_DEPLOYMENT)
    private boolean ignoreMissingDeployment;

    /**
     * The resolved dependency file
     */
    private File file;


    @Override
    public void validate() throws DeploymentFailureException {
        super.validate();
        if (artifactId == null) {
            throw new DeploymentFailureException("undeploy-artifact must specify the artifactId");
        }
        if (groupId == null) {
            throw new DeploymentFailureException("undeploy-artifact must specify the groupId");
        }
        final Set<Artifact> dependencies = project.getDependencyArtifacts();
        Artifact artifact = null;
        for (final Artifact a : dependencies) {
            if (a.getArtifactId().equals(artifactId) &&
                    a.getGroupId().equals(groupId)) {
                artifact = a;
                break;
            }
        }
        if (artifact == null) {
            throw new DeploymentFailureException("Could not resolve artifact to deploy %s:%s", new Object[] { groupId, artifactId });
        }
        file = artifact.getFile();
    }

    @Override
    protected File file() {
        return file;
    }

    @Override
    public String goal() {
        return "undeploy-artifact";
    }

    @Override
    public Type getType() {
        return (ignoreMissingDeployment ? Type.UNDEPLOY_IGNORE_MISSING : Type.UNDEPLOY);
    }
}
