package org.stjude.sprocket.settings

enum class OutputLevel(val displayName: String, val cliArg: String?, val levelFilter: String) {
    TRACE("Trace", "-vvv", "TRACE"),
    DEBUG("Debug", "-vv", "DEBUG"),
    INFO("Info", "--verbose", "INFO"),
    WARN("Warn", null, "WARN"),
    ERROR("Error", "--quiet", "ERROR");

    override fun toString(): String = displayName
}
