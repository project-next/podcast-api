server=$1
user="ec2-user"

ssh -i "~/Downloads/podcast-api.pem" "${user}@${server}" << EOF
    mkdir -p api/build/libs
EOF

sftp -i "~/Downloads/podcast-api.pem" "${user}@${server}" << EOF
    cd api
    put .env-local.txt
    put docker-compose.yml
    cd build/libs
    put build/libs/*
EOF