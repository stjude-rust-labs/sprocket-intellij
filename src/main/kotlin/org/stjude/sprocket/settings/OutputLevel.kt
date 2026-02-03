package org.stjude.sprocket.settings

enum class OutputLevel(val displayName: String, val cliArg: String?) {
    VERBOSE("Verbose", "--verbose"),
    INFORMATION("Information", null),
    QUIET("Quiet", "--quiet");

    override fun toString(): String = displayName
}
