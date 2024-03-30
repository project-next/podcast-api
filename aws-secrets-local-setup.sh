aws secretsmanager get-secret-value --secret-id "/prod/project-next/podcast-api/users" --region us-east-2 \
  | jq -r '.SecretString' | jq -r "to_entries|map(\"export \(.key)=\\\"\(.value|tostring)\\\"\")|.[]" > .env

aws secretsmanager get-secret-value --secret-id "/prod/project-next/podcast-api/db-creds" --region us-east-2 \
  | jq -r '.SecretString' \
  | jq -r "with_entries(select(.key | startswith(\"dbInstanceIdentifier\") or startswith(\"engine\") | not)) | {DB_USERNAME: .username, DB_PASSWORD: .password, DB_HOST: .host, DB_PORT: .port, DB_NAME: .dbname} | to_entries|map(\"export \(.key)=\\\"\(.value|tostring)\\\"\")|.[]" \
  >> .env

aws secretsmanager get-secret-value --secret-id "/prod/project-next/podcast-api/ssl" --region us-east-2 \
  | jq -r '.SecretString' \
  | jq -r "with_entries(select(.key | startswith(\"SSL_KEYSTORE_PASSWORD\") or startswith(\"PK\"))) | to_entries|map(\"export \(.key)=\\\"\(.value|tostring)\\\"\")|.[]" >> .env

source .env
cat .env

#########################################################################################

ENV="prod"
echo -e "Downloading latest certs and creating truststore\n"

mkdir -p certs
aws secretsmanager get-secret-value --secret-id "/prod/project-next/podcast-api/ssl" --region us-east-2 \
  | jq -r '.SecretString' \
  | jq -r "with_entries(select(.key | startswith(\"SSL\")))" > certs/base64-certs-json
jq -r ".SSL_PRIVATE_KEY" < certs/base64-certs-json | base64 -d > certs/private.key
jq -r ".SSL_CA_BUNDLE_CRT" < certs/base64-certs-json | base64 -d > certs/ca_bundle.crt
jq -r ".SSL_CERTIFICATE_CRT" < certs/base64-certs-json | base64 -d > certs/certificate.crt
rm certs/base64-certs-json

# Create keystore w/ certs
cd certs || exit
openssl pkcs12 -export -name podcastapi -in certificate.crt -inkey private.key -out podcast-api.p12 -password "pass:$PK_PASSWORD"
keytool -importkeystore -deststorepass "$SSL_KEYSTORE_PASSWORD" -destkeystore podcast-api.jks \
 -srckeystore podcast-api.p12 -srcstoretype PKCS12 -srcstorepass "$PK_PASSWORD"
# update keystore created above with ca bundle
keytool -import -alias podcast-api -trustcacerts -file ca_bundle.crt -keystore podcast-api.jks -storepass "$SSL_KEYSTORE_PASSWORD"

cd ..
mv certs/podcast-api.jks src/main/resources/podcast-api.jks
