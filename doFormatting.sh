#!/usr/bin/env zsh

# Added plugin
# addSbtPlugin("org.scalameta"     % "sbt-scalafmt"   % "2.4.3")

# sbt scalafmtAll scalafmtSbt

# Added command in build.sbt
#   addCommandAlias("fmt", "core/scalafmtAll; core/scalafmtSbt")
# 
sbt fmt
