#!/usr/bin/env bash
set -euo pipefail

echo "==> Building DynamisCore"
mvn clean install
echo "==> Build complete"
