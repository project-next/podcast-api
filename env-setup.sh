echo "Setting up environment variables retrieved from Doppler"
echo

export PODCAST_API_DB_PASSWORD="`doppler secrets get DB_PASSWORD --plain`"
export PODCAST_API_DB_USERNAME="`doppler secrets get DB_USERNAME --plain`"
export PODCAST_API_DB_URI="`doppler secrets get DB_URI --plain`"

echo "Environment set up!"
echo "PODCAST_API_DB_PASSWORD=$PODCAST_API_DB_PASSWORD"
echo "PODCAST_API_DB_PASSWORD=$PODCAST_API_DB_PASSWORD"
echo "PODCAST_API_DB_URI=$PODCAST_API_DB_URI"