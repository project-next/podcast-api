ssh -i "~/Downloads/podcast-api.pem" ec2-user@ec2-18-224-140-226.us-east-2.compute.amazonaws.com << EOF
    mkdir -p api/build/libs
EOF

sftp -i "~/Downloads/podcast-api.pem" ec2-user@ec2-18-224-140-226.us-east-2.compute.amazonaws.com << EOF
    cd api
    put .env-local.txt
    put docker-compose.yml
    cd build/libs
    put build/libs/*
EOF