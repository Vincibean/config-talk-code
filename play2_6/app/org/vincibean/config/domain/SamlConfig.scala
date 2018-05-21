package org.vincibean.config.domain

import scala.concurrent.duration.FiniteDuration

case class SamlConfig(partner: String,
                      keystore: Keystore,
                      samlUrl: SamlUrl,
                      idpMetadataUrl: String,
                      spMetadataDir: String,
                      maxAuthLifetime: FiniteDuration)
