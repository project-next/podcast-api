server=$1
user="ec2-user"

ssh -y -i "~/.ssh/podcast-api-server.pem" "${user}@${server}" << EOF
    mkdir -p api/podcast-api/build/libs
EOF

sftp -i "~/.ssh/podcast-api-server.pem" "${user}@${server}" << EOF
    cd api/podcast-api
    put docker-compose.yml
    cd build/libs
    put build/libs/podcast-api.jar
EOF