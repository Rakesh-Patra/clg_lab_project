#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
command -v java >/dev/null 2>&1 || { echo "Install JDK 17 or newer." >&2; exit 1; }
command -v mvn >/dev/null 2>&1 || { echo "Install Apache Maven 3.9 or newer." >&2; exit 1; }
mvn clean compile exec:java
