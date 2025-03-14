# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

### [3.1.2](https://github.com/Tomut0/SCDenizenBridge/compare/v3.0.1...v3.1.2) (2025-03-07)


### Features

* ClanPlayer is adjustable now! ([193e83c](https://github.com/Tomut0/SCDenizenBridge/commit/193e83cca94eb982234507cad21a5ae768ddeb90))


### Bug Fixes

* Depenizen#loadBridge breaking change ([8d3d5a1](https://github.com/Tomut0/SCDenizenBridge/commit/8d3d5a167317b10a680f5d19c54ea324efa39d55))
* missing issuer for ComponentClickEvent ([0b85400](https://github.com/Tomut0/SCDenizenBridge/commit/0b854001ee2e9553329d0863a7846b0869477c61))
* support for 1.17 and upper ([d0d8019](https://github.com/Tomut0/SCDenizenBridge/commit/d0d80191f56c94be5324374396022bcc7b16991b))

## [3.1.0](https://github.com/Tomut0/SCDenizenBridge/compare/v3.0.1...v3.1.0) (2022-12-07)


### Features

* ClanPlayer is adjustable now! ([193e83c](https://github.com/Tomut0/SCDenizenBridge/commit/193e83cca94eb982234507cad21a5ae768ddeb90))


### Bug Fixes

* missing issuer for ComponentClickEvent ([0b85400](https://github.com/Tomut0/SCDenizenBridge/commit/0b854001ee2e9553329d0863a7846b0869477c61))
* support for 1.17 and upper ([d0d8019](https://github.com/Tomut0/SCDenizenBridge/commit/d0d80191f56c94be5324374396022bcc7b16991b))

### [3.0.1](https://github.com/Tomut0/SCDenizenBridge/compare/v3.0.0...v3.0.1) (2022-12-02)


### Bug Fixes

* add ClanBalanceUpdateEvent.Cause half compatibility ([794c75d](https://github.com/Tomut0/SCDenizenBridge/commit/794c75d413d4b300e0c1aa816e4d766515dab625))

## [3.0.0](https://github.com/Tomut0/SCDenizenBridge/compare/v2.1.0...v3.0.0) (2022-12-02)


### ⚠ BREAKING CHANGES

* Dynamic loading of SimpleClans events

### Features

* Dynamic loading of SimpleClans events ([71c848b](https://github.com/Tomut0/SCDenizenBridge/commit/71c848b7d240b26c227d2b87de608d1daab96234))

## [2.1.0](https://github.com/Tomut0/SCDenizenBridge/compare/v2.0.0...v2.1.0) (2022-11-28)


### Features

* FrameTag + FrameOpenScriptEvent ([72028e5](https://github.com/Tomut0/SCDenizenBridge/commit/72028e53765b59624550afb4c98afa00946aaf07))


### Bug Fixes

* update events naming convention ([95d4e17](https://github.com/Tomut0/SCDenizenBridge/commit/95d4e1737cbb255e93f34d30ede3f3f5dd2bc445))

## 2.0.0 (2022-09-12)


### ⚠ BREAKING CHANGES

* add locale to the package name

### Features

* add bb command ([1d336a7](https://github.com/Tomut0/SCDenizenBridge/commit/1d336a7523770505b0b9da5a5756dbacace60222))
* add disband command ([33010df](https://github.com/Tomut0/SCDenizenBridge/commit/33010dff2dfa75d04030ec4357a213db92cbe86e))
* add properties to retrieve player clan / server clans ([bf98198](https://github.com/Tomut0/SCDenizenBridge/commit/bf98198a8a56a4a83bc5cfc3cf5b46b37d4de93f))
* let ClanTag be adjustable ([2a94092](https://github.com/Tomut0/SCDenizenBridge/commit/2a94092b4f75edf8e3cff5cc5ff27298d3af9c8c))
* promote/demote commands ([97f0e02](https://github.com/Tomut0/SCDenizenBridge/commit/97f0e020dea2cdddd17d2f9dc51020473f53de71))


### Bug Fixes

* add locale to the package name ([c14008e](https://github.com/Tomut0/SCDenizenBridge/commit/c14008e1fb6edfc7830daf6e09a69fed7c857d15))
* add more events, using reflection for registering ([2ab804a](https://github.com/Tomut0/SCDenizenBridge/commit/2ab804aa899635dc360f3660ad04083299d1ee95))
* add more tags to ClanPlayerTag ([5ed6feb](https://github.com/Tomut0/SCDenizenBridge/commit/5ed6feb3a916bd303cad68d45170613b68957951))
* clanplayer identify / tags init ([fc9c36c](https://github.com/Tomut0/SCDenizenBridge/commit/fc9c36c896cba7fd32b444f311bcd3829aeefa10))
* ClanPlayerTag#valueOf ([78dc7fb](https://github.com/Tomut0/SCDenizenBridge/commit/78dc7fb5aa187aa17dbc545f18217bb182ed16a6))
* context didn't load ([d1dc94b](https://github.com/Tomut0/SCDenizenBridge/commit/d1dc94bdc11d99de5207c6461ec01a44bb68ccee))
* merge promote/demote/invite into one command ([1b3c8c7](https://github.com/Tomut0/SCDenizenBridge/commit/1b3c8c7149a3ea00bbd338a3673afed911bc2981))
* reflection to instantiate commands as well ([8f76755](https://github.com/Tomut0/SCDenizenBridge/commit/8f767559768f2832a279c443609ae19feb7ccdcb))
* rename tags to java convention ([e5bda3b](https://github.com/Tomut0/SCDenizenBridge/commit/e5bda3b9260609e405ddea8bd2edcecfcd4fa5dc))
* retrieve ClanTag from ClanPlayerTag ([d9993a6](https://github.com/Tomut0/SCDenizenBridge/commit/d9993a6e8d1263130581fd83b291b6e40240f6e1))
* use ClanScriptEntryData where it possible ([db1d260](https://github.com/Tomut0/SCDenizenBridge/commit/db1d26090e3d36d36dda8d747523d3d73c4dce81))
