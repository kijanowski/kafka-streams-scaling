#!/usr/bin/env bash

helm upgrade --install  --wait -f values.yaml prometheus-adapter stable/prometheus-adapter
