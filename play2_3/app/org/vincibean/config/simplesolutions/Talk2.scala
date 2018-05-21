package org.vincibean.config.simplesolutions

import java.util.concurrent.TimeUnit

import org.pac4j.core.client.Clients
import org.pac4j.core.context.BaseConfig
import org.pac4j.play.Config
import org.pac4j.saml.client.Saml2Client
import play.api.Play

object Talk2 {

  private def builderMethod(logoutUrl: String,
                            keyStorePath: String,
                            keyStorePassword: String,
                            privateKeyPassword: String,
                            idpMetadataPath: String,
                            samlClientName: String,
                            spEntityId: String,
                            callbackUrl: String,
                            maximumAuthenticationLifetime: Long): Unit = {
    BaseConfig.setDefaultLogoutUrl(logoutUrl)

    val saml2Client = new Saml2Client()
    saml2Client.setKeystorePath(keyStorePath)
    saml2Client.setKeystorePassword(keyStorePassword)
    saml2Client.setPrivateKeyPassword(privateKeyPassword)
    saml2Client.setIdpMetadataPath(idpMetadataPath)
    saml2Client.setName(samlClientName)
    saml2Client.setSpEntityId(spEntityId)
    saml2Client.setMaximumAuthenticationLifetime(
      maximumAuthenticationLifetime.toInt)

    val clients = new Clients(callbackUrl, saml2Client)
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
  } builderMethod(logoutUrl,
                  keyStorePath,
                  keyStorePassword,
                  privateKeyPassword,
                  idpMetadataPath,
                  samlClientName,
                  spEntityId,
                  callbackUrl,
                  maximumAuthenticationLifetime)

}
