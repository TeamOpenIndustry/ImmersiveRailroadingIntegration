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

sed -i src/main/java/cam72cam/immersiverailroading/Mod.java -e 's/required-before:universalmodcore/\0; required-after:trackapi@[1.1,)/'

sed -i build.gradle -e "s,^dependencies {,apply from: 'ImmersiveRailroadingIntegration/dependencies.gradle'\n\0,"
