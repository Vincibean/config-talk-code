package org.vincibean.config.domain

case class Saml(configs: Set[SamlConfig],
                fallbackUrl: String,
                logoutUrl: String)
