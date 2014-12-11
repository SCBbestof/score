/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package org.eclipse.score.engine.node.entities;

import org.eclipse.score.api.nodes.WorkerStatus;

import java.util.List;

/**
 * User:
 * Date: 08/11/2O12
 */
//TODO: should be replaced with a class
public interface Worker {

	String getUuid();

	boolean isActive();

    WorkerStatus getStatus();

	String getHostName();

	String getInstallPath();

	String getDescription();

	String getOs();

	String getJvm();

	String getDotNetVersion();

	List<String> getGroups();

    boolean isDeleted();
}