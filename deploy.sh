if [ $# -eq 0 ]
	then
		echo "Need server name"
		exit 1
fi

SERVER=$1
USER="ec2-user"
DIR_ON_SERVER="api/podcast-api"

echo "Using server $SERVER and directory $DIR_ON_SERVER to sync prod API"

echo "Uploading API files"
rsync -avz -e "ssh -i ~/.ssh/podcast-api-server.pem" docker-compose.yml "${USER}@${SERVER}:${DIR_ON_SERVER}/"
rsync -avz -e "ssh -i ~/.ssh/podcast-api-server.pem" -R "build/libs/podcast-api.jar" "${USER}@${SERVER}:${DIR_ON_SERVER}/"

echo "Restaging API"
ssh -i ~/.ssh/podcast-api-server.pem "${USER}@${SERVER}" << EOF
	cd "$DIR_ON_SERVER"
	docker-compose kill
	docker-compose rm -f
	docker-compose pull
	docker-compose up --scale podcast-api=1 -d
EOF

bash aws-cert-update.sh