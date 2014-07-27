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

package net.leadware.wildfly.plugin.server;

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

import org.wildfly.plugin.common.ConnectionInfo;
import org.wildfly.plugin.common.Files;

/**
 * Server configuration information.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
class ServerInfo {
    private final ConnectionInfo connectionInfo;
    private final File jbossHome;
    private final String modulesDir;
    private final String[] jvmArgs;
    private final String javaHome;
    private final String serverConfig;
    private final String propertiesFile;
    private final long startupTimeout;

    private ServerInfo(final ConnectionInfo connectionInfo, final String javaHome, final File jbossHome, final String modulesDir, final String[] jvmArgs, final String serverConfig, final String propertiesFile, final long startupTimeout) {
        this.connectionInfo = connectionInfo;
        this.javaHome = javaHome;
        this.jbossHome = jbossHome;
        this.modulesDir = (modulesDir == null ? Files.createPath(jbossHome.getAbsolutePath(), "modules") : modulesDir);
        this.jvmArgs = jvmArgs;
        this.serverConfig = serverConfig;
        this.propertiesFile = propertiesFile;
        this.startupTimeout = startupTimeout;
    }

    /**
     * Creates the server information.
     *
     * @param connectionInfo the connection information for the management client
     * @param javaHome       the Java home directory
     * @param jbossHome      the home directory for the JBoss Application Server
     * @param modulesDir     the directory for the modules to use
     * @param jvmArgs        the JVM arguments
     * @param serverConfig   the path to the servers configuration file
     * @param startupTimeout the startup timeout
     *
     * @return the server configuration information
     */
    public static ServerInfo of(final ConnectionInfo connectionInfo, final String javaHome, final File jbossHome, final String modulesDir, final String[] jvmArgs, final String serverConfig, final String propertiesFile, final long startupTimeout) {
        return new ServerInfo(connectionInfo, javaHome, jbossHome, modulesDir, jvmArgs, serverConfig, propertiesFile, startupTimeout);
    }

    /**
     * The connection information for the management operations.
     *
     * @return the connection information
     */
    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    /**
     * The JBoss Application Server home directory.
     *
     * @return the home directory
     */
    public File getJbossHome() {
        return jbossHome;
    }

    /**
     * The directory for all the modules.
     *
     * @return the modules directory
     */
    public String getModulesDir() {
        return modulesDir;
    }

    /**
     * The optional JVM arguments.
     *
     * @return the JVM arguments or {@code null} if there are none
     */
    public String[] getJvmArgs() {
        return jvmArgs;
    }

    /**
     * The Java home directory.
     *
     * @return the Java home directory
     */
    public String getJavaHome() {
        return javaHome;
    }

    /**
     * The path to the server configuration file to use.
     *
     * @return the path to the configuration file or {@code null} if the default configuration file is being used
     */
    public String getServerConfig() {
        return serverConfig;
    }

    /**
     * The path to the system properties file to load.
     *
     * @return the path to the properties file or {@code null} if no properties should be loaded.
     */
    public String getPropertiesFile() {
        return propertiesFile;
    }

    /**
     * The timeout to use for the server startup.
     *
     * @return the server startup timeout
     */
    public long getStartupTimeout() {
        return startupTimeout;
    }
}
