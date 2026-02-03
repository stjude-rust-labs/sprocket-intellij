<p align="center">
  <h1 align="center">
    <code>sprocket-intellij</code>
  </h1>

  <p align="center">
    <a href="https://github.com/claymcleod/sprocket-intellij/actions/workflows/CI.yml"><img src="https://github.com/claymcleod/sprocket-intellij/actions/workflows/CI.yml/badge.svg" alt="CI Status" /></a>
    <a href="https://github.com/claymcleod/sprocket-intellij/blob/main/LICENSE-APACHE"><img alt="Apache 2.0" src="https://img.shields.io/badge/license-Apache%202.0-blue.svg"></a>
    <a href="https://github.com/claymcleod/sprocket-intellij/blob/main/LICENSE-MIT"><img alt="MIT" src="https://img.shields.io/badge/license-MIT-blue.svg"></a>
  </p>

  <p align="center">
    WDL language support for JetBrains IDEs via the Sprocket LSP
    <br />
    <br />
    <a href="https://github.com/claymcleod/sprocket-intellij/issues/new?labels=enhancement">Request Feature</a>
    ·
    <a href="https://github.com/claymcleod/sprocket-intellij/issues/new?labels=bug">Report Bug</a>
  </p>
</p>

## Overview

`sprocket-intellij` provides comprehensive [WDL](https://openwdl.org/) (Workflow Description Language) support for JetBrains IDEs, including IntelliJ IDEA, PyCharm, CLion, and others. It's powered by the [Sprocket](https://github.com/stjude-rust-labs/sprocket) language server. WDL is widely used in bioinformatics for defining portable, reproducible analysis workflows.

## Features

- **LSP Integration.** Completions, diagnostics, hover documentation, go-to-definition, and references powered by Sprocket
- **Syntax Highlighting.** TextMate grammar support for WDL 1.0 through 1.3
- **Document Formatting.** Format WDL files on demand via LSP
- **Status Bar Widget.** Monitor language server status at a glance
- **Configurable.** Customize behavior via Settings → Tools → Sprocket

## Getting Started

### Requirements

- JetBrains IDE 2024.2 or later
- [sprocket](https://github.com/stjude-rust-labs/sprocket/releases) binary in your PATH

### Installation

1. Download the latest `.zip` from [Releases](https://github.com/claymcleod/sprocket-intellij/releases)
2. Go to **Settings → Plugins → ⚙️ → Install Plugin from Disk...**
3. Select the downloaded `.zip` file

### Installing Sprocket

The `sprocket` binary must be available in your `PATH`. Choose one of the following methods:

**Homebrew (macOS/Linux)**
```bash
brew install sprocket
```

**Cargo (Rust)**
```bash
cargo install sprocket
```

**Pre-built binaries**

Download from [GitHub Releases](https://github.com/stjude-rust-labs/sprocket/releases) and add to your `PATH`.

## Configuration

Settings are available at **Settings → Tools → Sprocket**:

| Setting | Default | Description |
|---------|---------|-------------|
| Sprocket binary path | (auto) | Custom path to sprocket binary; leave empty to search PATH |
| Output level | Quiet | Server output verbosity (Quiet / Information / Verbose) |
| Enable lint checks | `false` | Enable additional linting via `--lint` flag |
| Maximum retry attempts | `1` | Retry attempts if server fails to start |

## Development

To build the plugin locally, you'll need JDK 17+ and Gradle 8.x. The project uses the IntelliJ Platform Gradle Plugin for building and testing.

```bash
# Build
./gradlew build

# Run sandbox IDE
./gradlew runIde

# Run tests
./gradlew test

# Package for distribution
./gradlew buildPlugin
```

The plugin `.zip` will be in `build/distributions/`.

## License

Licensed under either of

- Apache License, Version 2.0 ([LICENSE-APACHE](LICENSE-APACHE) or <http://www.apache.org/licenses/LICENSE-2.0>)
- MIT license ([LICENSE-MIT](LICENSE-MIT) or <http://opensource.org/licenses/MIT>)

at your option.

Copyright © 2026-Present [St. Jude Children's Research Hospital](https://www.stjude.org/).
