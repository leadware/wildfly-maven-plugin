/**
 * 
 */
package net.leadware.wildfly.plugin.cli;

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

import net.leadware.wildfly.plugin.cli.helper.NonClosingModelControllerClient;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.CommandLineException;
import org.wildfly.plugin.cli.Commands;
import org.wildfly.plugin.common.AbstractServerConnection;

/**
 * Classe representant le MOJO de creation d'un module Wildfly depuis un client distant
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 27 juil. 2014 - 12:14:07
 */
@Mojo(name="create-module", threadSafe=true)
public class CreateModuleMojo extends AbstractServerConnection {
	
	/**
	 * Nom du module
	 */
	@Parameter(required = true)
	private String moduleName;
	
	/**
	 * Liste des ressources du modules
	 */
	@Parameter(required = true)
	private String resources;
	
	/**
	 * Liste des dependances du module
	 */
	@Parameter(required = false)
	private String dependencies;
	
	/**
	 * Chemin de base du serveur Wildfly
	 */
	@Parameter(required = true)
	private String wildflyHome;
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		// Un log
		getLog().debug("Execution de la commande");
		
		// Si le home de wildfly est vide
		if(wildflyHome == null || wildflyHome.trim().isEmpty()) throw new RuntimeException("Execution fail, please fill Wildfly Home (wildflyHome parameter)");
		
		// Creation d'un file sur le Home
		File wildflyHomeFile = new File(wildflyHome.trim());
		
		// Si le repertoire n'existe pas
		if(!wildflyHomeFile.exists() || !wildflyHomeFile.isDirectory())  throw new RuntimeException("Execution fail, the path [" + wildflyHome + "] is not a directory.");
		
		// Synchronisation
		synchronized (CLIENT_LOCK) {
			
			// Obtention du controleur de modele
			NonClosingModelControllerClient client = new NonClosingModelControllerClient(getClient());
			
			// Creation d'un contexte de commande
			CommandContext commandContext = Commands.create(client);
			
			// Positionnement de la variable JBOSS_HOME
			commandContext.set("JBOSS_HOME", wildflyHome);
			
			// Constructeur de chaine
			StringBuilder builder = new StringBuilder("module add");
			
			// Ajout du nom du module
			builder.append(" --name=" + moduleName.trim());

			// Ajout des ressources
			builder.append(" --resources=" + resources.trim());
			
			// Si la liste des dependences est positionnee
			if(dependencies != null && !dependencies.trim().isEmpty()) builder.append(" --dependencies=" + dependencies.trim());
			
			// Commande a executer
			String command = builder.toString();
			
			// Un log
			getLog().debug("Commande a executer : " + command);
			
			try {

				// Execution de la commande
				commandContext.handle(command);
				
			} catch (CommandFormatException e) {
				
				// On leve un exception D'argument Illegal
		        throw new IllegalArgumentException(String.format("Command '%s' is invalid. %s", new Object[] { command, e.getLocalizedMessage() }), e);
		        
		    } catch (CommandLineException e) {
		    	
		    	// On leve une erreur Illegale
		        throw new IllegalArgumentException(String.format("Command execution failed for command '%s'. %s", new Object[] { command, e.getLocalizedMessage() }), e);
		    }
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.wildfly.plugin.common.AbstractServerConnection#goal()
	 */
	@Override
	public String goal() {
		
		// On retourne le nom du goal a executer
		return "create-module";
	}
}
