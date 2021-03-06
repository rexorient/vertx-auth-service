/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.auth.shiro.impl;

import io.vertx.core.json.JsonObject;

/**
 * Built-in implementations for Shiro auth Realms should implelken
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public interface ShiroAuthRealm {

  void init(JsonObject config);

  Object login(JsonObject credentials);

  boolean hasRole(Object principal, String role);

  boolean hasPermission(Object principal, String permission);
}
