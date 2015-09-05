#!/bin/bash

sudo apt-get update
sudo apt-get -y install gradle openssl stunnel

openssl genrsa -out /tmp/correct.key 1024
openssl req -new -key /tmp/correct.key -out /tmp/correct.crt -subj '/CN=127.0.0.1/' -x509
cat /tmp/correct.crt /tmp/correct.key > /tmp/correct.pem

openssl genrsa -out /tmp/wrongcn.key 1024
openssl req -new -key /tmp/wrongcn.key -out /tmp/wrongcn.crt -subj '/CN=127.0.0.2/' -x509
cat /tmp/wrongcn.crt /tmp/wrongcn.key > /tmp/wrongcn.pem 
