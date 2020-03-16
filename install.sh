#!/bin/bash

set -e

function genAndSign() {
	local cn=$1
	local file=$2
	openssl genrsa -out "/tmp/${file}.key" 2048
	openssl req -new -key "/tmp/${file}.key" -out "/tmp/${file}.csr" -subj "/CN=${cn}/"
	openssl x509 -req -in "/tmp/${file}.csr" -out "/tmp/${file}.crt" \
		-CA /tmp/ca.crt -CAkey /tmp/ca.key -CAcreateserial
	cat "/tmp/${file}.crt" "/tmp/${file}.key" > "/tmp/${file}.pem"
}

openssl genrsa -out /tmp/ca.key 2048
openssl req -new -key /tmp/ca.key -out /tmp/ca.crt -subj '/CN=Test CA/' -x509

genAndSign "127.0.0.1" "correct"
genAndSign "127.0.0.2" "wrongcn"
