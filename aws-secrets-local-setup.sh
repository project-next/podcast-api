aws secretsmanager get-secret-value --secret-id "/project-next/podcast-api/users" --region us-east-2 \
  | jq -r '.SecretString' | jq -r "to_entries|map(\"export \(.key)=\\\"\(.value|tostring)\\\"\")|.[]" > .env

aws secretsmanager get-secret-value --secret-id "/project-next/podcast-api/db-creds" --region us-east-2 \
  | jq -r '.SecretString' | jq -r "to_entries|map(\"export \(.key)=\\\"\(.value|tostring)\\\"\")|.[]" >> .env

aws secretsmanager get-secret-value --secret-id "/project-next/podcast-api/ssl" --region us-east-2 \
  | jq -r '.SecretString' \
  | jq -r "with_entries(select(.key | startswith(\"SSL_KEYSTORE_PASSWORD\") or startswith(\"PK\"))) | to_entries|map(\"export \(.key)=\\\"\(.value|tostring)\\\"\")|.[]" >> .env

source .env

#########################################################################################

# doppler is still needed to store/retrieve SSL certs/private key
ENV="prod"
echo -e "Downloading latest certs and creating truststore\n"
doppler setup -p podcast-api -c ${ENV} --no-interactive

mkdir -p certs
doppler secrets get --plain SSL_PRIVATE_KEY > certs/private.key
doppler secrets get --plain SSL_CA_BUNDLE_CRT > certs/ca_bundle.crt
doppler secrets get --plain SSL_CERTIFICATE_CRT > certs/certificate.crt

# Create keystore w/ certs
cd certs || exit
openssl pkcs12 -export -name podcastapi -in certificate.crt -inkey private.key -out podcast-api.p12 -password "pass:$PK_PASSWORD"
keytool -importkeystore -deststorepass "$SSL_KEYSTORE_PASSWORD" -destkeystore podcast-api.jks \
 -srckeystore podcast-api.p12 -srcstoretype PKCS12 -srcstorepass "$PK_PASSWORD"
# update keystore created above with ca bundle
keytool -import -alias podcast-api -trustcacerts -file ca_bundle.crt -keystore podcast-api.jks -storepass "$SSL_KEYSTORE_PASSWORD"

cd ..
mv certs/podcast-api.jks src/main/resources/podcast-api.jks
