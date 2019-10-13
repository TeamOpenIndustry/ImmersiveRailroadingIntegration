BRANCH=$1

if [ -z "$BRANCH" ]; then
		BRANCH="forge_1.12.2"
fi
rm -rf TrackAPI
git clone --branch $BRANCH git@github.com:TeamOpenIndustry/TrackAPI.git

echo "include 'TrackAPI'" >> settings.gradle

sed -i src/main/java/cam72cam/immersiverailroading/Mod.java -e 's/required-before:modcore/\0; required-after:trackapi@[1.1,)/'
