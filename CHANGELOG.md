# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.1.1] - 2026-02-03

### Fixed

- WDL file type now registered in `Settings > Editor > File Types`
- `Open Settings` action navigates directly to Sprocket settings
- Settings now persist correctly
- TextMate syntax highlighting loads immediately on startup
- Server restarts properly when settings change
- Server restart no longer crashes on failure

### Removed

- Unused `checkForUpdates` setting
- Unused `maxRetries` setting

## [0.1.0] - 2026-02-03

### Added

- WDL syntax highlighting via TextMate grammar
- LSP integration with Sprocket language server via LSP4IJ
- Automatic `sprocket` binary detection from PATH
- Manual binary path configuration in settings
- Code completion, hover, go-to-definition, find references
- Document formatting
- Configurable output level (quiet, information, verbose)
- Optional lint diagnostics

[Unreleased]: https://github.com/claymcleod/sprocket-intellij/compare/v0.1.1...HEAD
[0.1.1]: https://github.com/claymcleod/sprocket-intellij/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/claymcleod/sprocket-intellij/releases/tag/v0.1.0
