package org.vincibean.config.logging

import java.util.concurrent._

import org.vincibean.config.digression.SamlBuilder
import org.vincibean.config.digression.SamlBuilder.FullConfig
import play.api.{Logger, Play}

import scala.concurrent.duration._

object Talk1 {

  val optBuilder: Option[SamlBuilder[FullConfig]] = for {
    app <- Play.maybeApplication
    conf = app.configuration
    keyStorePath <- conf.getString("saml.configs.keystore.path")
    _ = Logger.debug(s"Keystore Path is $keyStorePath")
    keyStorePassword <- conf.getString("saml.configs.keystore.password")
    _ = Logger.debug(s"Keystore password is $keyStorePassword")
    privateKeyPassword <- conf.getString(
      "saml.configs.keystore.private-key-password")
    _ = Logger.debug(s"Private key password is $privateKeyPassword")
    idpMetadataPath <- conf.getString("saml.configs.idp-metadata-url")
    _ = Logger.debug(s"IDP metadata path is $idpMetadataPath")
    samlClientName <- conf.getString(
      "saml.configs.saml-url.enabled-domains.client-name")
    _ = Logger.debug(s"SAML client name is $samlClientName")
    spEntityId <- conf.getString("saml.configs.sp-metadata-dir")
    _ = Logger.debug(s"Service Provider entity ID is $spEntityId")
    callbackUrl <- conf.getString("saml.fallback-url")
    _ = Logger.debug(s"Callback URL is $callbackUrl")
    logoutUrl <- conf.getString("saml.logout-url")
    _ = Logger.debug(s"Logout URL is $logoutUrl")
    maxAuthLifetime = conf.underlying
      .getDuration("saml.configs.max-auth-lifetime", TimeUnit.MILLISECONDS)
    _ = Logger.debug(s"Maximum authentication lifetime is $maxAuthLifetime")
  } yield
    SamlBuilder()
      .withKeystorePath(keyStorePath)
      .withKeystorePassword(keyStorePassword)
      .withPrivateKeyPassword(privateKeyPassword)
      .withIdpMetadataPath(idpMetadataPath)
      .withSpEntityId(spEntityId)
      .withSamlClientName(samlClientName)
      .withCallbackUrl(callbackUrl)
      .withLogoutUrl(logoutUrl)
      .withMaximumAuthenticationLifetime(maxAuthLifetime.millis)
  val builder: SamlBuilder[FullConfig] = optBuilder.get
  builder.build()

}
