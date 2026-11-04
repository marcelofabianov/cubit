#!/usr/bin/env bash
# Fails when the data generation changes src/generated: those files are never edited by hand.
set -euo pipefail

readonly GENERATED_DIR='src/generated'

root="$(git rev-parse --show-toplevel)"
readonly root

"${root}/gradlew" --project-dir "${root}" runData

changes="$(git -C "${root}" status --porcelain -- "${GENERATED_DIR}")"
if [[ -n "${changes}" ]]; then
  {
    echo "check-generated: ${GENERATED_DIR} differs from what the data generation produces:"
    echo "${changes}"
    echo "Commit the regenerated files, and never edit them by hand."
  } >&2
  exit 1
fi
