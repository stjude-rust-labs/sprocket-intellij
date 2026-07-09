# Release Process

This document describes how to release the Sprocket IntelliJ plugin.

## Creating a Release

1. Update the release version in `gradle.properties`:
   ```properties
   pluginVersion = 0.1.0
   ```

2. Find the latest IntelliJ Platform release or EAP supported by the plugin.
   Check the [IntelliJ Platform build number ranges](https://plugins.jetbrains.com/docs/intellij/build-number-ranges.html)
   and the current IntelliJ IDEA release notes to map the latest IDE version
   to its build number prefix. For example, IntelliJ IDEA 2026.2 uses the
   `262` build prefix.

3. Verify the supported IntelliJ Platform build range in `gradle.properties`.
   Set `pluginUntilBuild` to the latest supported build prefix:
   ```properties
   pluginUntilBuild = <build-prefix>.*
   ```

4. Update `CHANGELOG.md`:
   - Rename `[Unreleased]` to `[0.1.0] - YYYY-MM-DD`
   - Add a new empty `[Unreleased]` section above it
   - Update the links at the bottom of the file

5. Commit version changes:
   ```bash
   git add gradle.properties CHANGELOG.md
   git commit -m "release: v0.1.0"
   ```

6. Create and push a tag:
   ```bash
   git tag v0.1.0
   git push origin main
   git push origin v0.1.0
   ```

7. Create a GitHub release:
   - Go to **Releases** → **Create a new release**
   - Select the tag
   - Copy the changelog section as release notes
   - Publish the release

The GitHub Action will automatically build the plugin and attach the distribution zip to the release.

## Checklist

- [ ] Update `pluginVersion` in `gradle.properties`
- [ ] Find the latest IntelliJ Platform release or EAP build prefix
- [ ] Verify `pluginUntilBuild` in `gradle.properties`
- [ ] Update `CHANGELOG.md`
- [ ] Run tests: `./gradlew test`
- [ ] Build plugin: `./gradlew buildPlugin`
- [ ] Test installation in a fresh IDE
- [ ] Commit all changes
- [ ] Create and push git tag
- [ ] Create GitHub release
