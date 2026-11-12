#!/usr/bin/env bash
# Downloads a release of Cubit with its dependencies into a zip: the jar from the GitHub release
# and the dependencies from Modrinth, in the versions of the release tag, each one checked by its
# hash. Without VERSION, it takes the latest release. Only public APIs, without any login.
set -euo pipefail

readonly REPOSITORY='marcelofabianov/cubit'
readonly GITHUB_API="https://api.github.com/repos/${REPOSITORY}"
readonly GITHUB_RAW="https://raw.githubusercontent.com/${REPOSITORY}"
readonly MODRINTH_API='https://api.modrinth.com/v2'
readonly USER_AGENT='cubit-bundle-script'
readonly LOADER='neoforge'
readonly OUTPUT_DIR='build/dist'
readonly REQUIRED_DIR='mods'
readonly OPTIONAL_DIR='optional'
readonly ZIP_SUFFIX='-with-dependencies.zip'

root="$(git rev-parse --show-toplevel)"
readonly root
work="$(mktemp -d)"
readonly work
trap 'rm -rf "${work}"' EXIT

fail() {
  echo "bundle: $*" >&2
  exit 1
}

fetch() {
  curl --fail --silent --show-error --location --user-agent "${USER_AGENT}" "$@"
}

# Reads one field of the JSON on standard input, as data, never as code.
json_field() {
  python3 -I -c '
import json, sys
value = json.load(sys.stdin)
for key in sys.argv[1:]:
    value = value[int(key)] if isinstance(value, list) else value.get(key)
    if value is None:
        sys.exit(1)
print(value)
' "$@"
}

release_tag() {
  if [[ -n "${VERSION:-}" ]]; then
    echo "v${VERSION#v}"
  else
    fetch "${GITHUB_API}/releases/latest" | json_field tag_name || fail 'no release found'
  fi
}

property() {
  local value
  value="$(grep -E "^$1=" "${work}/gradle.properties" | cut -d= -f2-)"
  [[ -n "${value}" ]] || fail "gradle.properties of the release has no $1"
  echo "${value}"
}

check_sha512() {
  local actual
  actual="$(shasum -a 512 "$1" | cut -d' ' -f1)"
  [[ "${actual}" == "$2" ]] || fail "wrong SHA-512 for $(basename "$1")"
}

# The primary file of a Modrinth version, by the version number the release was built with.
download_modrinth() {
  local project="$1" version="$2" minecraft="$3" target="$4" query file url sha512 name
  query="game_versions=%5B%22${minecraft}%22%5D&loaders=%5B%22${LOADER}%22%5D"
  file="$(fetch "${MODRINTH_API}/project/${project}/version?${query}" | python3 -I -c '
import json, sys
wanted = sys.argv[1]
for version in json.load(sys.stdin):
    if version["version_number"] == wanted:
        files = [f for f in version["files"] if f["primary"]] or version["files"]
        print(files[0]["url"], files[0]["hashes"]["sha512"], files[0]["filename"])
        break
else:
    sys.exit(1)
' "${version}")" || fail "Modrinth has no ${project} ${version} for ${minecraft}"
  read -r url sha512 name <<<"${file}"
  fetch --output "${target}/${name}" "${url}"
  check_sha512 "${target}/${name}" "${sha512}"
  echo "  ${project} ${version}"
}

download_cubit() {
  local tag="$1" target="$2" release asset url digest name
  release="$(fetch "${GITHUB_API}/releases/tags/${tag}")" || fail "no release ${tag}"
  asset="$(echo "${release}" | python3 -I -c '
import json, sys
for asset in json.load(sys.stdin)["assets"]:
    if asset["name"].startswith("cubit-") and asset["name"].endswith(".jar"):
        print(asset["browser_download_url"], asset.get("digest") or "none", asset["name"])
        break
else:
    sys.exit(1)
')" || fail "release ${tag} has no Cubit jar"
  read -r url digest name <<<"${asset}"
  fetch --output "${target}/${name}" "${url}"
  if [[ "${digest}" == sha256:* ]]; then
    [[ "$(shasum -a 256 "${target}/${name}" | cut -d' ' -f1)" == "${digest#sha256:}" ]] \
      || fail "wrong SHA-256 for ${name}"
  fi
  echo "  cubit ${tag#v}"
}

main() {
  local tag version bundle minecraft
  tag="$(release_tag)"
  version="${tag#v}"
  fetch --output /dev/null "${GITHUB_API}/releases/tags/${tag}" 2>/dev/null \
    || fail "no release ${tag} on GitHub"
  bundle="${work}/cubit-${version}"
  mkdir -p "${bundle}/${REQUIRED_DIR}" "${bundle}/${OPTIONAL_DIR}"
  fetch --output "${work}/gradle.properties" "${GITHUB_RAW}/${tag}/gradle.properties" \
    || fail "no gradle.properties at ${tag}"
  minecraft="$(property minecraft_version)"

  echo "bundle: Cubit ${version} for Minecraft ${minecraft}"
  download_cubit "${tag}" "${bundle}/${REQUIRED_DIR}"
  download_modrinth geckolib "$(property geckolib_version)" "${minecraft}" \
    "${bundle}/${REQUIRED_DIR}"
  download_modrinth lambdynamiclights "$(property lambdynamiclights_version)" "${minecraft}" \
    "${bundle}/${REQUIRED_DIR}"
  download_modrinth jade "$(property jade_version)" "${minecraft}" "${bundle}/${OPTIONAL_DIR}"

  mkdir -p "${root}/${OUTPUT_DIR}"
  local zip="${root}/${OUTPUT_DIR}/cubit-${version}${ZIP_SUFFIX}"
  rm -f "${zip}"
  (cd "${work}" && zip -q -r "${zip}" "cubit-${version}")
  echo "bundle: ${zip#"${root}"/}"
}

main
