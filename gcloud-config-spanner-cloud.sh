#!/usr/bin/env zsh

clear

gcloud config configurations activate default
gcloud config configurations delete spanner-cloud
gcloud config configurations create spanner-cloud
gcloud config set account dj707chen@gmail.com
gcloud config set project spannerlab
gcloud auth login

# Setup Instance
# echo "\nCreating Spanner instance test-instance ..."
# gcloud spanner instances create test-instance \
#   --config regional-us-central1 \
#   --description="Test Instance" \
#   --nodes=1

echo "\nCreating Spanner databases test-db ..."
gcloud spanner databases create test-db \
  --instance=test-instance
#  --project=spannerlab \
