package org.stjude.sprocket.settings

enum class OutputLevel(val displayName: String, val cliArg: String?, val levelFilter: String) {
    TRACE("Trace", "-vvv", "TRACE"),
    DEBUG("Debug", "-vv", "DEBUG"),
    VERBOSE("Verbose", "--verbose", "INFO"),
    INFORMATION("Information", null, "WARN"),
    QUIET("Quiet", "--quiet", "ERROR");

    override fun toString(): String = displayName
}
