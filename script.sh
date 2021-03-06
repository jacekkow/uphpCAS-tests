#!/bin/bash

set -e

cd `dirname $0`

stunnel4 etc/stunnel.conf
php -S 127.0.0.1:8080 -t portal/ -d include_path=.:../../.. &
php -S 127.0.0.1:8081 -t cas-mockup/ &

export TERM=dumb
exec gradle --no-daemon test
