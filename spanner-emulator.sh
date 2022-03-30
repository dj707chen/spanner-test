#! /usr/bin/env bash

export SPANNER_EMULATOR_HOST=localhost:9010

# docker pull gcr.io/cloud-spanner-emulator/emulator
# docker run -p 9010:9010 -p 9020:9020 gcr.io/cloud-spanner-emulator/emulator
gcloud config configurations create emulator
gcloud config set auth/disable_credentials true
gcloud config set project your-project-id
gcloud config set api_endpoint_overrides/spanner http://localhost:9020/

# Setup Instance 
cloud spanner instances create test-instance \
   --config=emulator-config --description="Test Instance" --nodes=1

gcloud config configurations activate emulator

gcloud spanner databases create test-db --instance=test-instance --project=your-project-id