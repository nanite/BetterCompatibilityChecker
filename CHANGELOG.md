# Changelog

All notable changes to this project will be documented in this file.

## [21.3.1]

### Changed

- Updated to 1.21.3 -> 1.21.5

## [21.1.7]

- Fixed not logging the modpack name and version after loading the metajson

## [21.1.6]

- More logging
  - Logs the modpack name and version after setup
  - Adds the modpack name and version to the crash report

## [21.1.3]

- Bump neoforge version

## [21.1.2]

- Fix: add alias to work in production
  - Thanks to pietro-lopes

## [21.1.1]

- Dropped multiloader support

## [21.1.0]

- Updated to 1.21.1

## [21.0.0]

- Ported to 1.21
- New versioning scheme
- Dropped forge support

## [5.1.2]

- Updated forge,fabric,neo versions

## [5.1.0]

- Updated forge,fabric,neo versions
- Reworked neo to use same method as fabric
  - drawForgePingInfo no longer exists

## [5.0.0]

- Ported to 1.20.4

## [4.0.8]

- Switched to using fabric mixin AP

## [4.0.7]

- Forge <-> Neo compatability changes
  - Should be compatible with each other now (thanks mapping differences)

## [4.0.6]

- Fix neoforge crashing on MP screen
  - Why are is val$p_105460_ different of neo(val$pServer)?

## [4.0.5]

- Ported for neoforge

## [4.0.4]

- Fixed version data not being replaced in mods.toml

## [4.0.3]

- Fixed various build script issues

## [4.0.2]

- Fixed CI not adding loader name to the display name on curseforge
- Added Turkish translation (alpeerkaraca)
- Re-added French translation as it went missing (Calvineries)
- Lang between fabric and forge are now in sync

## [4.0.1]
- Fix fabric server not being able to start up

## [4.0.0]

- Moved over to MultiLoader
  - Fabric and forge versions should be release at the same time now :D

## [3.0.3]

- Fix fix

## [3.0.2]

- Fix crash

## [3.0.0]

- Ported to 1.20
