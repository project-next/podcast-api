SERVER=$1
USER="ec2-user"

if [ $# -eq 0 ]
	then
		echo "Need server name"
		exit -1
fi

echo $SERVER

rsync -avz -e "ssh -i ~/.ssh/podcast-api-server.pem" docker-compose.yml "${USER}@${SERVER}:api/podcast-api/"
rsync -avz -e "ssh -i ~/.ssh/podcast-api-server.pem" -R "build/libs/podcast-api.jar" "${USER}@${SERVER}:api/podcast-api/"