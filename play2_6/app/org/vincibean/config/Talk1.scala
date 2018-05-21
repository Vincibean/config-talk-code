package org.vincibean.config

import org.vincibean.config.domain.Saml
import play.api.Logger
import pureconfig.error.ConfigReaderFailures

object Talk1 {

  def provideSaml: Saml = {
    val samlConfig: Either[ConfigReaderFailures, Saml] =
      pureconfig.loadConfig[Saml]("saml")
    samlConfig.left.foreach(f =>
      Logger.warn(s"""Couldn't create a full SAML configuration due to
      the following missing keys:
      ${f.toList.map(_.description).mkString("; ")}
      Proceeding with default values."""))
    samlConfig.toOption.get
  }

}
