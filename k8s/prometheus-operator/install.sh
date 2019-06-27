#!/usr/bin/env bash

helm upgrade --install  --wait -f values.yaml prometheus-operator stable/prometheus-operator
