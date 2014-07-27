package net.leadware.wildfly.plugin.cli.helper;

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

import java.io.IOException;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.Operation;
import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.threads.AsyncFuture;

/**
 * Classe representant un controleur de modele client qui ne se referme pas automatiquement 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Enterprise Architect)</a>
 * @since 2014-07-27 13:15:31
 */
public class NonClosingModelControllerClient implements ModelControllerClient {
	
	/**
	 * Controlleur Delegue
	 */
	private final ModelControllerClient delegate;
	
	/**
	 * Constructeur avec initialisation du delegue
	 * @param delegate	Controleur Delegue
	 */
	public NonClosingModelControllerClient(ModelControllerClient delegate) {
		
		// Initialisation du controleur delegue
		this.delegate = delegate;
	}
	
	/**
	 * Methode d'execution de l'operation (ModelNode)
	 * @param operation	Operation a executer
	 * @return	Resultat de l'execution
	 * @throws IOException	Exception potentielle
	 */
	public ModelNode execute(ModelNode operation) throws IOException {
		
		// Execution de l'operation par le controleur delegue
		return this.delegate.execute(operation);
	}
	
	/**
	 * Methode d'execution de l'operation
	 * @param operation	Operation a executer
	 * @return	Resultat de l'execution
	 * @throws IOException	Exception potentielle
	 */
	public ModelNode execute(Operation operation) throws IOException {
		
		// Execution de l'operation par le controleur delegue
		return this.delegate.execute(operation);
	}
	
	/**
	 * Methode d'execution de l'Operation (ModelNode) avec un Gestionnaire de messages de retour
	 * @param operation	Operation a executer
	 * @param messageHandler	gestionnaire de messages
	 * @return	Resultat de l'operation
	 * @throws IOException	Exception potentielle
	 */
	public ModelNode execute(ModelNode operation, OperationMessageHandler messageHandler) throws IOException {
		
		// Execution de l'operation par le controleur delegue
		return this.delegate.execute(operation, messageHandler);
	}
	
	/**
	 * Methode d'execution de l'Operation avec un Gestionnaire de messages de retour
	 * @param operation	Operation a executer
	 * @param messageHandler	gestionnaire de messages
	 * @return	Resultat de l'operation
	 * @throws IOException	Exception potentielle
	 */
	public ModelNode execute(Operation operation, OperationMessageHandler messageHandler) throws IOException {
		
		// Execution de l'operation par le controleur delegue
		return this.delegate.execute(operation, messageHandler);
	}
	
	/**
	 * Methode d'execution asynchrone d'une Operation (ModelNode)
	 * @param operation	Operation a executer
	 * @param messageHandler	Gestionnaire de message
	 * @return	Resultat de l'execution
	 */
	public AsyncFuture<ModelNode> executeAsync(ModelNode operation, OperationMessageHandler messageHandler) {
		
		// Execution de l'operation par le controleur delegue
		return this.delegate.executeAsync(operation, messageHandler);
	}
	
	/**
	 * Methode d'execution asynchrone d'une Operation
	 * @param operation	Operation a executer
	 * @param messageHandler	Gestionnaire de message
	 * @return	Resultat de l'execution
	 */
	public AsyncFuture<ModelNode> executeAsync(Operation operation, OperationMessageHandler messageHandler) {
		
		// Execution de l'operation par le controleur delegue
		return this.delegate.executeAsync(operation, messageHandler);
	}
	
	/**
	 * Methode de fermeture du contexte d'execution des commandes
	 * @throws IOException	Exception potentielle
	 */
	public void close() throws IOException {}
}