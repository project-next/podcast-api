server=$1
user="ec2-user"

ssh -y -i "~/.ssh/podcast-api-server.pem" "${user}@${server}" << EOF
    mkdir -p api/build/libs
EOF

sftp -y -i "~/.ssh/podcast-api-server.pem" "${user}@${server}" << EOF
    cd api
    put .env-local.txt
    put docker-compose.yml
    cd build/libs
    put build/libs/*
EOF