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

package io.vertx.ext.auth.test;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthService;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class AuthServiceTest extends VertxTestBase {

  protected volatile AuthService authService;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    authService = AuthService.create(vertx, new JsonObject());
    authService.start();
  }

  @Override
  protected void tearDown() throws Exception {
    authService.stop();
    super.tearDown();
  }

  @Test
  public void testSimpleLogin() {
    JsonObject credentials = new JsonObject().put("username", "tim").put("password", "sausages");
    authService.login(credentials, onSuccess(res -> {
      testComplete();
    }));
    await();
  }

  @Test
  public void testSimpleLoginFail() {
    JsonObject credentials = new JsonObject().put("username", "tim").put("password", "wrongpassword");
    authService.login(credentials, onFailure(res -> {
      testComplete();
    }));
    await();
  }
}