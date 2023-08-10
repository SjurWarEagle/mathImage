docker build .. -f Dockerfile -t mathimage:latest
docker run --rm -p 80:80 mathimage:latest