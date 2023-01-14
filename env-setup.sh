echo "Setting up environment variables retrieved from Doppler"
echo

# DB Creds
PODCAST_API_DB_PASSWORD="`doppler secrets get DB_PASSWORD --plain`"
PODCAST_API_DB_USERNAME="`doppler secrets get DB_USERNAME --plain`"
PODCAST_API_DB_URI="`doppler secrets get DB_URI --plain`"

# Users for in memory DB
ADMIN_USERNAME="`doppler secrets get ADMIN_USERNAME --plain`"
ADMIN_PASSWORD="`doppler secrets get ADMIN_PASSWORD --plain`"
GENERIC_USER_USERNAME="`doppler secrets get GENERIC_USER_USERNAME --plain`"
GENERIC_USER_PASSWORD="`doppler secrets get GENERIC_USER_PASSWORD --plain`"

echo "Environment set up!"
echo "PODCAST_API_DB_USERNAME=$PODCAST_API_DB_USERNAME"
echo "PODCAST_API_DB_PASSWORD=$PODCAST_API_DB_PASSWORD"
echo "PODCAST_API_DB_URI=$PODCAST_API_DB_URI"
echo "ADMIN_USERNAME=$ADMIN_USERNAME"
echo "ADMIN_PASSWORD=$ADMIN_PASSWORD"
echo "GENERIC_USER_USERNAME=$GENERIC_USER_USERNAME"
echo "GENERIC_USER_PASSWORD=$GENERIC_USER_PASSWORD"

######################################

# DB Creds
export PODCAST_API_DB_PASSWORD
export PODCAST_API_DB_USERNAME
export PODCAST_API_DB_URI

# Users for in memory DB
export ADMIN_USERNAME
export ADMIN_PASSWORD
export GENERIC_USER_USERNAME
export GENERIC_USER_PASSWORD