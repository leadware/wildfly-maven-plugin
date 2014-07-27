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

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@Component(role = ArtifactResolver.class)
class DefaultArtifactResolver extends AbstractArtifactResolver<Object> implements ArtifactResolver {

    @Requirement
    private PlexusContainer container;

    @Override
    public File resolve(final MavenProject project, final String artifact) {
        try {
            String hint = isUsingEclipseAether() ? "eclipse" : "sonatype";
            final ArtifactResolver resolver = container.lookup(ArtifactResolver.class, hint);
            return resolver.resolve(project, artifact);
        } catch (ComponentLookupException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    protected Object constructArtifact(final String groupId, final String artifactId, final String classifier, final String packaging, final String version) {
        throw new UnsupportedOperationException("Not available in the default implementation");
    }
}
