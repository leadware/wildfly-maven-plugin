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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.wildfly.plugin.common.PropertyNames;

/**
 * Classe de base des deploiements applicatifs 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Enterprise Architect)</a>
 * @since 2014-07-27 15:38:57
 */
public abstract class AbstractAppDeployment extends AbstractDeployment {


    /**
     * The target directory the application to be deployed is located.
     */
    @Parameter(defaultValue = "${project.build.directory}/", property = PropertyNames.DEPLOYMENT_TARGET_DIR)
    private File targetDir;

    /**
     * The file name of the application to be deployed.
     * <p>
     * The {@code filename} property does have a default of <code>${project.build.finalName}.${project.packaging}</code>.
     * The default value is not injected as it normally would be due to packaging types like {@code ejb} that result in
     * a file with a {@code .jar} extension rather than an {@code .ejb} extension.
     * </p>
     */
    @Parameter(property = PropertyNames.DEPLOYMENT_FILENAME)
    private String filename;

    /**
     * By default certain package types are ignored when processing, e.g. {@code maven-project} and {@code pom}. Set
     * this value to {@code false} if this check should be bypassed.
     */
    @Parameter(alias = "check-packaging", property = PropertyNames.CHECK_PACKAGING, defaultValue = "true")
    private boolean checkPackaging;

    private PackageType packageType;

    @Override
    protected void doExecute() throws MojoExecutionException, MojoFailureException {
        final PackageType packageType = getPackageType();
        if (checkPackaging && packageType.isIgnored()) {
            getLog().debug(String.format("Ignoring packaging type %s.", packageType.getPackaging()));
        } else {
            super.doExecute();
        }
    }

    @Override
    protected File file() {
        final PackageType packageType = getPackageType();
        final String filename;
        if (this.filename == null) {
            filename = String.format("%s.%s", project.getBuild().getFinalName(), packageType.getFileExtension());
        } else {
            filename = this.filename;
        }
        return new File(targetDir, filename);
    }

    protected final synchronized PackageType getPackageType() {
        if (packageType == null) {
            packageType = PackageType.resolve(project);
        }
        return packageType;
    }
}
