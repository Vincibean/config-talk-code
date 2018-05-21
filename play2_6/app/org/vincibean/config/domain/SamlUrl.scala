package org.vincibean.config.domain

case class SamlUrl(callback: String, enabledDomains: Set[EnabledDomain])
