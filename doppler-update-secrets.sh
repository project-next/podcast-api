ENV="prod"

cat certs/private.key | doppler secrets set -p podcast-api -c ${ENV} SSL_PRIVATE_KEY
cat certs/ca_bundle.crt | doppler secrets set -p podcast-api -c ${ENV} SSL_CA_BUNDLE_CRT
cat certs/certificate.crt | doppler secrets set -p podcast-api -c ${ENV} SSL_CERTIFICATE_CRT