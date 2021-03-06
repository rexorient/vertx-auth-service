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

package examples;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthService;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.auth.shiro.ShiroAuthService;
import io.vertx.ext.auth.spi.AuthProvider;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Examples {

  public void example0_1(Vertx vertx, JsonObject config) {

    // Deploy service - can be anywhere on your network
    DeploymentOptions options = new DeploymentOptions().setConfig(config);

    vertx.deployVerticle("io.vertx:shiro-auth-service", options, res -> {
      if (res.succeeded()) {
        // Deployed ok
      } else {
        // Failed to deploy
      }
    });
  }

  public void example0_2(Vertx vertx, JsonObject credentials) {

    AuthService proxy = AuthService.createEventBusProxy(vertx, "vertx.auth");

    // Now do stuff with it:

    proxy.login(credentials, res -> {

      // etc

    });
  }

  public void example0_3(Vertx vertx, JsonObject credentials) {

    JsonObject config = new JsonObject();
    config.put("properties_path", "classpath:test-auth.properties");

    AuthService authService = ShiroAuthService.create(vertx, ShiroAuthRealmType.PROPERTIES, config);

    authService.start();

    // Now do stuff with it:

    authService.login(credentials, res -> {

      // etc

    });

  }

  public void example0_3_1(Vertx vertx, JsonObject credentials) {

    vertx.deployVerticle("service:io.vertx:shiro-auth-service");

  }

  public void example0_4(Vertx vertx, JsonObject credentials) {

    JsonObject config = new JsonObject();
    config.put("properties_path", "classpath:test-auth.properties");

    AuthService authService = ShiroAuthService.create(vertx, ShiroAuthRealmType.PROPERTIES, config);

    authService.start();

    // Now do stuff with it:

    authService.login(credentials, res -> {

      // etc

    });

  }

  public void example0_5(Vertx vertx, AuthProvider myAuthProvider) {

    JsonObject config = new JsonObject();
    config.put("your_config_property", "blah");

    AuthService authService = AuthService.create(vertx, myAuthProvider, config);

    authService.start();

  }

  public void example0_6(Vertx vertx, AuthProvider myAuthProvider) {

    JsonObject config = new JsonObject();
    config.put("provider_class_name", "com.mycompany.myproject.MyAuthProviderClass");
    config.put("your_config_property", "blah");

    DeploymentOptions options = new DeploymentOptions().setConfig(config);

    vertx.deployVerticle("service:io.vertx:auth-service", options);

  }

  public void example1(AuthService authService) {

    JsonObject credentials = new JsonObject().put("username", "tim").put("password", "wibble");

    authService.login(credentials, res -> {

      if (res.succeeded()) {

        // Login successful!

        // The login ID is needed if you later want to authorise a user

        String loginID = res.result();

      } else {

        // Login failed.

        String reason = res.cause().getMessage();

      }
    });
  }

  public void example2(AuthService authService, String loginID) {

    authService.refreshLoginSession(loginID, res -> {

      if (res.succeeded()) {

        // Refreshed ok

      } else {

        // Not refreshed ok - probably the login has already timed out or doesn't exist.
      }
    });
  }

  public void example3(AuthService authService, String loginID) {

    authService.logout(loginID, res -> {

      if (res.succeeded()) {

        // Logged out ok

      } else {

        // Failed to logout - probably the login has already timed out or doesn't exist.

      }
    });
  }

  public void example4(AuthService authService, String loginID) {

    authService.hasRole(loginID, "manager", res -> {

      if (res.succeeded()) {

        boolean hasRole = res.result();

        if (hasRole) {

          // do something

        } else {

          // do something else

        }

      } else {

        // Something went wrong - maybe the user is not logged in?
      }

    });
  }
}
