#!/bin/bash

set -e

apt-get -y update
apt-get -y install \
	--no-install-recommends \
	--no-install-suggests \
	gradle openssl stunnel
