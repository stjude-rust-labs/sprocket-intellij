# Release Process

This document describes how to release the Sprocket IntelliJ plugin.

## Creating a Release

1. Update the version in `build.gradle.kts`:
   ```kotlin
   version = "0.1.0"
   ```

2. Update `CHANGELOG.md`:
   - Rename `[Unreleased]` to `[0.1.0] - YYYY-MM-DD`
   - Add a new empty `[Unreleased]` section above it
   - Update the links at the bottom of the file

3. Commit version changes:
   ```bash
   git add build.gradle.kts CHANGELOG.md
   git commit -m "release: v0.1.0"
   ```

4. Create and push a tag:
   ```bash
   git tag v0.1.0
   git push origin main
   git push origin v0.1.0
   ```

5. Create a GitHub release:
   - Go to **Releases** → **Create a new release**
   - Select the tag
   - Copy the changelog section as release notes
   - Publish the release

The GitHub Action will automatically build the plugin and attach the distribution zip to the release.

## Checklist

- [ ] Update version in `build.gradle.kts`
- [ ] Update `CHANGELOG.md`
- [ ] Run tests: `./gradlew test`
- [ ] Build plugin: `./gradlew buildPlugin`
- [ ] Test installation in a fresh IDE
- [ ] Commit all changes
- [ ] Create and push git tag
- [ ] Create GitHub release
