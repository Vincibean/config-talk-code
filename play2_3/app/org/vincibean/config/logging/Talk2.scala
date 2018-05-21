package org.vincibean.config.logging

import java.util.concurrent.TimeUnit

import org.vincibean.config.digression.SamlBuilder
import org.vincibean.config.digression.SamlBuilder.FullConfig
import play.api.{Configuration, Play}

import scala.concurrent.duration._

object Talk2 {

  val conf: Configuration = Play.maybeApplication.map(_.configuration).get
  val logoutUrl: String = conf.getString("saml.logout-url").get
  val keyStorePath: String = conf.getString("saml.configs.keystore.path").get
  val keyStorePassword: String =
    conf.getString("saml.configs.keystore.password").get
  val privateKeyPassword: String =
    conf.getString("saml.configs.keystore.private-key-password").get
  val idpMetadataPath: String =
    conf.getString("saml.configs.idp-metadata-url").get
  val samlClientName: String =
    conf.getString("saml.configs.saml-url.enabled-domains.client-name").get
  val spEntityId: String = conf.getString("saml.configs.sp-metadata-dir").get
  val callbackUrl: String = conf.getString("saml.fallback-url").get
  val maxAuthLifetime: Long = conf.underlying
    .getDuration("saml.configs.max-auth-lifetime", TimeUnit.MILLISECONDS)
  val builder: SamlBuilder[FullConfig] = SamlBuilder()
    .withKeystorePath(keyStorePath)
    .withKeystorePassword(keyStorePassword)
    .withPrivateKeyPassword(privateKeyPassword)
    .withIdpMetadataPath(idpMetadataPath)
    .withSpEntityId(spEntityId)
    .withSamlClientName(samlClientName)
    .withCallbackUrl(callbackUrl)
    .withLogoutUrl(logoutUrl)
    .withMaximumAuthenticationLifetime(maxAuthLifetime.millis)
  builder.build()

}
