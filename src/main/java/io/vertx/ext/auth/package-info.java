/**
 *
 * = Vert.x Auth Service
 *
 * This Vert.x service provides authentication and authorisation functionality for use in your Vert.x applications.
 *
 * The auth service provides a common interface that can be backed by different auth providers. Vert. ships with a
 * default implementation that uses http://shiro.apache.org/[Apache Shiro] but you can provide your own implementation
 * by implementing the {@link io.vertx.ext.auth.spi.AuthProvider} interface. The Vert.x Apache Shiro implementation
 * currently allows user/role/permission information to be accessed from simple properties files or LDAP servers.
 *
 * The auth service can either be used directly in your application, but it also integrated with the auth functionality
 * in Vert.x Apex.
 *
 * == Basic concepts
 *
 * _Authentication_ (aka _log in_) means verifying the identity of a user.
 *
 * _Authorisation_ means verifying a user has the correct access rights for some resource.
 *
 * The service uses a familiar user/role/permission model that you will probably know already:
 *
 * Users can have zero or more roles, e.g. "manager", "developer".
 *
 * Roles can have zero or more permissions, e.g. a manager might have permission "approve expenses", "conduct_reviews",
 * and a developer might have a permission "commit_code".
 *
 * == Setting up the service
 *
 * As with other services you can use the service either by deploying it as a verticle somewhere on your network and
 * interacting with it over the event bus, either directly by sending messages, or using a service proxy, e.g.
 *
 * Somewhere you deploy it:
 *
 * [source,java]
 * ----
 * {@link examples.Examples#example0_1}
 * ----
 *
 * Now you can either send messages to it directly over the event bus, or you can create a proxy to the service
 * from wherever you are and just use that:
 *
 * [source,java]
 * ----
 * {@link examples.Examples#example0_2}
 * ----
 *
 * Alternatively you can create an instance of the service directly and just use that locally:
 *
 * [source,java]
 * ----
 * {@link examples.Examples#example0_3}
 * ----
 *
 * If you create an instance this way you should make sure you start it with {@link io.vertx.ext.auth.AuthService#start}
 * before you use it.
 *
 * However you do it, once you've got your service you can start using it.
 *
 * == Shiro Auth Service
 *
 * As previously mentioned we provide an implementation of the Auth service that uses Apache Shiro to perform the
 * actual auth.
 *
 * To use this, you should use {@link io.vertx.ext.auth.shiro.ShiroAuthService}.
 *
 * This currently supports properties file based user/role/permission information and using LDAP, and you can also pass
 * in a pre-existing Shiro `Realm` instance or implement {@link io.vertx.ext.auth.shiro.impl.ShiroAuthRealm} to implement
 * a different method of auth using Shiro.
 *
 * == Shiro Auth Service Verticle
 *
 * As with most services you can deploy the service somewhere on your network and interact with it via a proxy, here's
 * an example of deploying a Shiro auth service verticle:
 *
 * [source,java]
 * ----
 * {@link examples.Examples#example0_3_1}
 * ----
 *
 * === Properties auth realm
 *
 * The properties auth realm gets user/role/permission information from a properties file.
 *
 * Here's an example that uses the out of the box properties auth realm:
 *
 * [source,java]
 * ----
 * {@link examples.Examples#example0_4}
 * ----
 *
 * The properties auth realm will, by default, look for a file called `vertx-users.properties`
 * on the classpath.
 *
 * If you want to change this, you can use the `properties_path` configuration element to define how the properties
 * file is found.
 *
 * The default value is `classpath:vertx-users.properties`.
 *
 * If the value is prefixed with `classpath:` then the classpath will be searched for a properties file of that name.
 *
 * If the value is prefixed with `file:` then it specifies a file on the file system.
 *
 * If the value is prefixed with `url:` then it specifies a URL from where to load the properties.
 *
 * The properties file should have the following structure:
 *
 * Each line should either contain the username, password and roles for a user or the permissions in a role.
 *
 * For a user line it should be of the form:
 *
 *  user.{username}={password},{roleName1},{roleName2},...,{roleNameN}
 *
 * For a role line it should be of the form:
 *
 *  role.{roleName}={permissionName1},{permissionName2},...,{permissionNameN}
 *
 * Here's an example:
 * ----
 * user.tim = mypassword,administrator,developer
 * user.bob = hispassword,developer
 * user.joe = anotherpassword,manager
 * role.administrator=*
 * role.manager=play_golf,say_buzzwords
 * role.developer=do_actual_work
 * ----
 *
 * When describing roles a wildcard `*` can be used to indicate that the role has all permissions
 *
 * === LDAP auth realm
 *
 * The LDAP auth realm gets user/role/permission information from an LDAP server.
 *
 * The following configuration properties are used to configure the LDAP realm:
 *
 * `ldap-user-dn-template`:: this is used to determine the actual lookup to use when looking up a user with a particular
 * id. An example is `uid={0},ou=users,dc=foo,dc=com` - the element `{0}` is substituted with the user id to create the
 * actual lookup. This setting is mandatory.
 * `ldap_url`:: the url to the LDAP server. The url must start with `ldap://` and a port must be specified.
 * An example is `ldap:://myldapserver.mycompany.com:10389`
 * `ldap-authentication-mechanism`:: TODO
 * `ldap-context-factory-class-name`:: TODO
 * `ldap-pooling-enabled`:: TODO
 * `ldap-referral`:: TODO
 * `ldap-system-username`:: TODO
 * `ldap-system-password`:: TODO
 *
 * == Using non Shiro Auth implementations
 *
 * If you want to use a different auth provider with the Auth service, you should implement {@link io.vertx.ext.auth.spi.AuthProvider}.
 *
 * You can then create a local instance of the AuthService with:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example0_5}
 * ----
 *
 * Or to to deploy an verticle instance:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example0_6}
 * ----
 *
 * == Using the API
 *
 * The auth service API is described with {@link io.vertx.ext.auth.AuthService}.
 *
 * It contains method to login and check roles and permissions.
 *
 * === Authentication - login / logout
 *
 * You use {@link io.vertx.ext.auth.AuthService#login} to login a user. The argument to log-in is a {@link io.vertx.core.json.JsonObject}
 * representing the _credentials_ of the user.
 *
 * Often the credentials will just be a `username` string field and a `password` string field - and this is what is
 * expected by the out of the box Apache Shiro provider, but other providers might use other data for credentials that's
 * why we keep it as a general JSON object.
 *
 * The result of the login is returned in the result handler. If the login is successful a string login-ID will be returned
 * as the result. This is a unique secure UUID that identifies the login session. The login ID should be used if you
 * later want to authorise the user, i.e. check whether they have permissions or roles.
 *
 * Here's an example of a login:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example1}
 * ----
 *
 * The login session ID provided at login will be valid as long as the login hasn't timed out or been explicitly
 * logged out.
 *
 * The default time it remains valid is 30 minutes. If you want to use a different value of timeout you can specify that
 * by calling {@link io.vertx.ext.auth.AuthService#loginWithTimeout(io.vertx.core.json.JsonObject, long, io.vertx.core.Handler)}.
 *
 * To prevent a login timing out, you can call {@link io.vertx.ext.auth.AuthService#refreshLoginSession} specifying
 * the login ID. The login will timeout if it remains unrefreshed for greater than the timeout period.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example2}
 * ----
 *
 * You can explicitly logout a user with {@link io.vertx.ext.auth.AuthService#logout} specifying the login ID:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example3}
 * ----
 *
 * === Authorisation
 *
 * Authorisation means checking whether the user has the right roles or permissions.
 *
 * In order to check roles or permissions the user must first be logged-in and you must have a valid login session ID
 * as described in the previous section.
 *
 * To check if a user has a specific role you use {@link io.vertx.ext.auth.AuthService#hasRole} specifying the login ID
 * and the role.
 *
 * The result of the check is returned in the handler. If the check didn't occur - e.g. the login ID is not valid, a
 * failure will be returned in the handler, otherwise it will return a boolean - true if the user has the role
 * or false if they don't have the role.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example4}
 * ----
 *
 * You can also check multiple roles at the same time with {@link io.vertx.ext.auth.AuthService#hasRoles}. In this
 * case you will return a true result only if the user has _all_ the specified roles.
 *
 * In the same way as checking roles, you can check permissions too. To this you use
 * {@link io.vertx.ext.auth.AuthService#hasPermission} and
 * {@link io.vertx.ext.auth.AuthService#hasPermissions} in the exact same way as roles.
 *
 * Authorisations are cached for the length of the login. This means that the first time you do authorisation for a user
 * it will go the auth provider, but the second time you do it with the same roles and permissions it will not call the
 * auth provider but will return the cached value.
 *
 * This allows better performance but bear in mind that if the roles
 * or permissions for a user change in the provider while the login session is valid and when they have already been
 * cached in the auth service, then the auth service won't see the changes in the provider until a new login session
 * is started.
 *
 *
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
@Document(fileName = "index.adoc")
@GenModule(name = "vertx-auth")
package io.vertx.ext.auth;

import io.vertx.codegen.annotations.GenModule;
import io.vertx.docgen.Document;