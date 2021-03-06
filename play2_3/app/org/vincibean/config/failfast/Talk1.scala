package org.vincibean.config.failfast

import java.util.concurrent.TimeUnit

import org.vincibean.config.digression.SamlBuilder
import org.vincibean.config.digression.SamlBuilder.FullConfig
import play.api.Play

import scala.concurrent.duration._

object Talk1 {

  val optBuilder: Option[SamlBuilder[FullConfig]] = for {
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
    logoutUrl <- conf.getString("saml.logout-url")
    maxAuthLifetime = conf.underlying
      .getDuration("saml.configs.max-auth-lifetime", TimeUnit.MILLISECONDS)
      .millis
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
      .withMaximumAuthenticationLifetime(maxAuthLifetime)
  val builder: SamlBuilder[FullConfig] = optBuilder.get
  builder.build()

}
