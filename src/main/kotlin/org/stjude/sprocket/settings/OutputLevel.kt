package org.stjude.sprocket.settings

enum class OutputLevel(val displayName: String, val cliArg: String?, val levelFilter: String) {
    TRACE("Trace", "-vvv", "TRACE"),
    DEBUG("Debug", "-vv", "DEBUG"),
    VERBOSE("Info", "--verbose", "INFO"),
    INFORMATION("Warn", null, "WARN"),
    QUIET("Error", "--quiet", "ERROR");

    override fun toString(): String = displayName
}
