#!/bin/bash

DIR_NAME=`dirname $0`
PARENT_NAME=`realpath "${DIR_NAME}/.."`

docker pull debian
docker run -i -t -d \
	-v "${PARENT_NAME}:/data:ro" \
	--name uphpcas-tests \
	debian

set -e

docker exec -i -t uphpcas-tests /data/tests/install_root.sh
docker exec -i -t uphpcas-tests apt-get -y install php php-xml
docker exec -i -t uphpcas-tests chown www-data:www-data /var/www
docker exec -i -t --user www-data --workdir /var/www uphpcas-tests cp -Rfv /data .
docker exec -i -t --user www-data --workdir /var/www/data uphpcas-tests ./tests/install.sh
docker exec -i -t --user www-data --workdir /var/www/data uphpcas-tests ./tests/script.sh

docker stop uphpcas-tests
docker rm -v uphpcas-tests
