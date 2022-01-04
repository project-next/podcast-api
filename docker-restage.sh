server=$1
user="ec2-user"

if [ $# -eq 0 ]
	then
		echo "Need server name"
fi

ssh -y -i ~/.ssh/podcast-api-server.pem "${user}@${server}" << EOF
	cd api
	docker-compose kill
	docker-compose rm -f
	docker-compose up --scale podcast-api=1 -d
EOF