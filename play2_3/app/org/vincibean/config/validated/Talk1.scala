package org.vincibean.config.validated

import java.util.concurrent.TimeUnit

import cats.data.{NonEmptyVector, Validated}
import com.typesafe.config.ConfigFactory
import org.vincibean.config.digression.SamlBuilder.FullConfig
import play.api.{Configuration, Logger}

import scala.concurrent.duration._
import cats.syntax.apply._
import org.vincibean.config.digression.SamlBuilder

object Talk1 {

  val conf: Option[Configuration] = Option(Configuration(ConfigFactory.load()))
  val logoutUrl: Validated[NonEmptyVector[String], String] =
    Validated.fromOption[NonEmptyVector[String], String](
      conf.flatMap(_.getString("saml.logout-url")),
      NonEmptyVector.of("Missing saml.logout-url"))
  val keyStorePath: Validated[NonEmptyVector[String], String] =
    Validated.fromOption[NonEmptyVector[String], String](
      conf.flatMap(_.getString("saml.configs.keystore.path")),
      NonEmptyVector.of("Missing saml.configs.keystore.path"))
  val keyStorePassword: Validated[NonEmptyVector[String], String] =
    Validated.fromOption[NonEmptyVector[String], String](
      conf.flatMap(_.getString("saml.configs.keystore.password")),
      NonEmptyVector.of("Missing saml.configs.keystore.password"))
  val privateKeyPassword: Validated[NonEmptyVector[String], String] =
    Validated.fromOption[NonEmptyVector[String], String](
      conf.flatMap(_.getString("saml.configs.keystore.private-key-password")),
      NonEmptyVector.of("Missing saml.configs.keystore.private-key-password"))
  val idpMetadataPath: Validated[NonEmptyVector[String], String] =
    Validated.fromOption[NonEmptyVector[String], String](
      conf.flatMap(_.getString("saml.configs.idp-metadata-url")),
      NonEmptyVector.of("Missing saml.configs.idp-metadata-url"))
  val samlClientName: Validated[NonEmptyVector[String], String] =
    Validated.fromOption[NonEmptyVector[String], String](
      conf.flatMap(
        _.getString("saml.configs.saml-url.enabled-domains.client-name")),
      NonEmptyVector.of(
        "Missing saml.configs.saml-url.enabled-domains.client-name")
    )
  val spEntityId: Validated[NonEmptyVector[String], String] =
    Validated.fromOption[NonEmptyVector[String], String](
      conf.flatMap(_.getString("saml.configs.sp-metadata-dir")),
      NonEmptyVector.of("Missing saml.configs.sp-metadata-dir"))
  val callbackUrl: Validated[NonEmptyVector[String], String] =
    Validated.fromOption[NonEmptyVector[String], String](
      conf.flatMap(_.getString("saml.fallback-url")),
      NonEmptyVector.of("Missing saml.fallback-url"))
  val maxAuthLifetime: Validated[NonEmptyVector[String], FiniteDuration] =
    Validated.fromOption[NonEmptyVector[String], FiniteDuration](
      conf.map(
        _.underlying
          .getDuration("saml.configs.max-auth-lifetime", TimeUnit.MILLISECONDS)
          .millis),
      NonEmptyVector.of("Missing saml.configs.max-auth-lifetime")
    )

  val valBuilder: Validated[NonEmptyVector[String], SamlBuilder[FullConfig]] =
    (keyStorePath,
     keyStorePassword,
     privateKeyPassword,
     idpMetadataPath,
     spEntityId,
     samlClientName,
     callbackUrl,
     logoutUrl,
     maxAuthLifetime).mapN(
      (ksPath, ksPsw, pkPsw, idpPath, spId, name, cbUrl, loUrl, lfTime) =>
        SamlBuilder()
          .withKeystorePath(ksPath)
          .withKeystorePassword(ksPsw)
          .withPrivateKeyPassword(pkPsw)
          .withIdpMetadataPath(idpPath)
          .withSpEntityId(spId)
          .withSamlClientName(name)
          .withCallbackUrl(cbUrl)
          .withLogoutUrl(loUrl)
          .withMaximumAuthenticationLifetime(lfTime))

  val eBuilder: Either[NonEmptyVector[String], SamlBuilder[FullConfig]] =
    valBuilder.toEither
  eBuilder.left.foreach(nev => Logger.error(nev.toVector.mkString(", ")))

  val builder: SamlBuilder[FullConfig] = eBuilder.right.get
  builder.build()

}
