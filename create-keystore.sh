# Create keystore w/ certs
echo "Creating new keystore from certs..."
source .env
cd certs || exit
openssl pkcs12 -export -name podcastapi -in certificate.crt -inkey private.key -out podcast-api.p12 -password "pass:$PK_PASSWORD"
keytool -importkeystore -deststorepass "$SSL_KEYSTORE_PASSWORD" -destkeystore podcast-api.jks \
 -srckeystore podcast-api.p12 -srcstoretype PKCS12 -srcstorepass "$PK_PASSWORD"
# update keystore created above with ca bundle
keytool -import -alias podcast-api -trustcacerts -file ca_bundle.crt -keystore podcast-api.jks -storepass "$SSL_KEYSTORE_PASSWORD"

cd ..
mv certs/podcast-api.jks src/main/resources/podcast-api.jks