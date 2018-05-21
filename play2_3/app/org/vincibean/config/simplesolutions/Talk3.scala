package org.vincibean.config.simplesolutions

import java.util.concurrent.TimeUnit

import org.pac4j.core.client.Clients
import org.pac4j.core.context.BaseConfig
import org.pac4j.play.Config
import org.pac4j.saml.client.Saml2Client
import play.api.Play

object Talk3 {

  private final case class Container(logoutUrl: String,
                                     keyStorePath: String,
                                     keyStorePassword: String,
                                     privateKeyPassword: String,
                                     idpMetadataPath: String,
                                     samlClientName: String,
                                     spEntityId: String,
                                     callbackUrl: String,
                                     maximumAuthenticationLifetime: Long)

  private def builderMethod(c: Container): Unit = {
    BaseConfig.setDefaultLogoutUrl(c.logoutUrl)

    val saml2Client = new Saml2Client()
    saml2Client.setKeystorePath(c.keyStorePath)
    saml2Client.setKeystorePassword(c.keyStorePassword)
    saml2Client.setPrivateKeyPassword(c.privateKeyPassword)
    saml2Client.setIdpMetadataPath(c.idpMetadataPath)
    saml2Client.setName(c.samlClientName)
    saml2Client.setSpEntityId(c.spEntityId)
    saml2Client.setMaximumAuthenticationLifetime(
      c.maximumAuthenticationLifetime.toInt)

    val clients = new Clients(c.callbackUrl, saml2Client)
    Config.setClients(clients)
  }

  for {
    app <- Play.maybeApplication
    conf = app.configuration
    logoutUrl <- conf.getString("saml.logout-url")
    keyStorePath <- conf.getString("saml.configs.keystore.path")
    keyStorePassword <- conf.getString("saml.configs.keystore.password")
    privateKeyPassword <- conf.getString(
      "saml.configs.keystore.private-key-password")
    idpMetadataPath <- conf.getString("saml.configs.idp-metadata-url")
    samlClientName <- conf.getString(
      "saml.configs.saml-url.enabled-domains.client-name")
    spEntityId <- conf.getString("saml.configs.sp-metadata-dir")
    callbackUrl <- conf.getString("saml.fallback-url")
    maximumAuthenticationLifetime = conf.underlying
      .getDuration("saml.configs.max-auth-lifetime", TimeUnit.MILLISECONDS)
    container = Container(logoutUrl,
                          keyStorePath,
                          keyStorePassword,
                          privateKeyPassword,
                          idpMetadataPath,
                          samlClientName,
                          spEntityId,
                          callbackUrl,
                          maximumAuthenticationLifetime)
  } builderMethod(container)

}
