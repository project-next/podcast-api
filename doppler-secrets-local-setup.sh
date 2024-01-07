ENV="prod"
echo -e "Downloading latest certs and creating truststore\n"
doppler setup -p podcast-api -c ${ENV} --no-interactive

mkdir -p certs
doppler secrets get --plain SSL_PRIVATE_KEY > certs/private.key
doppler secrets get --plain SSL_CA_BUNDLE_CRT > certs/ca_bundle.crt
doppler secrets get --plain SSL_CERTIFICATE_CRT > certs/certificate.crt

PK_PASSWORD=$(doppler secrets get --plain PK_PASSWORD)
SSL_KEYSTORE_PASSWORD=$(doppler secrets get --plain SSL_KEYSTORE_PASSWORD)
export PK_PASSWORD
export SSL_KEYSTORE_PASSWORD

# Create keystore w/ certs
cd certs || exit
openssl pkcs12 -export -name podcastapi -in certificate.crt -inkey private.key -out podcast-api.p12 -password "pass:$PK_PASSWORD"
keytool -importkeystore -deststorepass "$SSL_KEYSTORE_PASSWORD" -destkeystore podcast-api.jks \
 -srckeystore podcast-api.p12 -srcstoretype PKCS12 -srcstorepass "$PK_PASSWORD"
# update keystore created above with ca bundle
keytool -import -alias podcast-api -trustcacerts -file ca_bundle.crt -keystore podcast-api.jks -storepass "$SSL_KEYSTORE_PASSWORD"

cd ..
mv certs/podcast-api.jks src/main/resources/podcast-api.jks

#########################################
echo -e "###############################################\n"
echo -e "Setting up environment variables retrieved from Doppler\n"
JQ_EXPRESSION='with_entries(select(.key | startswith("DOPPLER") or startswith("PK") or startswith("SSL_C") or startswith("SSL_PRIVATE") | not)) | to_entries[] | "\(.key)=\"\(.value)\""'
# passwords
doppler secrets download --no-file --format json | jq -r "$JQ_EXPRESSION"