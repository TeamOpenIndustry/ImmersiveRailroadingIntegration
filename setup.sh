BRANCH=$1
useHttps=$2

if [ -z "$BRANCH" ]; then
		BRANCH="forge_1.12.2"
fi
gitPfx="git@github.com:"
if [ ! -z "$useHttps" ]; then
	gitPfx="https://github.com/"
fi

rm -rf TrackAPI
git clone --branch $BRANCH ${gitPfx}TeamOpenIndustry/TrackAPI.git

cat>src/main/resources/pack.mcmeta<<EOT
{
  "pack": {
    "description": "IR resources",
    "pack_format": 4
  }
}
EOT

echo "apply from: 'ImmersiveRailroadingIntegration/dependencies.gradle'" >> build.gradle
