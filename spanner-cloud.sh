#!/usr/bin/env zsh

clear

export SPANNER_EMULATOR_HOST=localhost:9010

gcloud config configurations activate default
gcloud config configurations delete emulator
gcloud config configurations create emulator
# gcloud config set account dj707chen@gmail.com
gcloud config set project spanneremulatorlab
gcloud config set auth/disable_credentials true
gcloud config set api_endpoint_overrides/spanner http://localhost:9020/

# Setup Instance
echo "\nCreating Spanner instance test-instance ..."
gcloud spanner instances create test-instance \
  --config=emulator-config \
  --description="Test Instance" \
  --nodes=1

echo "\nCreating Spanner databases test-db ..."
gcloud spanner databases create test-db \
  --project=spanneremulatorlab \
  --instance=test-instance