#!/bin/bash

# Generate 32 random bytes (256 bits)
key_bytes=$(head -c 32 /dev/random)

# Base64 encode the key
key_base64=$(echo -n "$key_bytes" | base64)

# Print the base64 encoded key
echo "$key_base64"
