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

import org.apache.maven.plugins.annotations.Mojo;

/**
 * Classe representant le MOJO d'aide sur le plugin 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Enterprise Architect)</a>
 * @since 2014-07-27 15:52:25
 */
@Mojo(name="help", requiresProject=false, threadSafe=true)
public class HelpMojo extends org.wildfly.plugin.generated.HelpMojo {}
