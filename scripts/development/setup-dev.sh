#!/usr/bin/env bash
# Installs the pinned development tools inside the project and enables the git hooks.
set -euo pipefail

readonly GITLEAKS_VERSION="8.30.1"
readonly GITLEAKS_ASSET="gitleaks_${GITLEAKS_VERSION}_linux_x64.tar.gz"
readonly GITLEAKS_SHA256="551f6fc83ea457d62a0d98237cbad105af8d557003051f41f3e7ca7b3f2470eb"
readonly GITLEAKS_URL="https://github.com/gitleaks/gitleaks/releases/download/v${GITLEAKS_VERSION}/${GITLEAKS_ASSET}"

root="$(git rev-parse --show-toplevel)"
readonly root
readonly tools_dir="${root}/.tools"
readonly gitleaks_bin="${tools_dir}/gitleaks"

install_gitleaks() {
  if [[ "$(uname -sm)" != "Linux x86_64" ]]; then
    echo "setup-dev: only Linux x86_64 is supported; got $(uname -sm)" >&2
    exit 1
  fi

  local download_dir
  download_dir="$(mktemp -d)"
  trap 'rm -rf "${download_dir}"' RETURN

  curl -fsSL "${GITLEAKS_URL}" -o "${download_dir}/${GITLEAKS_ASSET}"
  echo "${GITLEAKS_SHA256}  ${download_dir}/${GITLEAKS_ASSET}" | sha256sum --check --quiet

  mkdir -p "${tools_dir}"
  tar -xzf "${download_dir}/${GITLEAKS_ASSET}" -C "${tools_dir}" gitleaks
  chmod +x "${gitleaks_bin}"
}

if [[ ! -x "${gitleaks_bin}" ]] || [[ "$("${gitleaks_bin}" version)" != "${GITLEAKS_VERSION}" ]]; then
  install_gitleaks
fi

git -C "${root}" config core.hooksPath scripts/development/git-hooks

echo "setup-dev: gitleaks $("${gitleaks_bin}" version) installed and git hooks enabled"
