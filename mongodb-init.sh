#!/bin/bash

# THIS SCRIPT IS NOT SUPPOSED TO BE MANUELY RUNED
# Docker will use it after creating a mongodb container

set -e

mongosh <<EOF
use $MONGODB_DB
db.createUser({
  user: '$MONGODB_USERNAME',
  pwd: '$MONGODB_PASSWORD',
  roles: [{ role: 'readWrite', db: '$MONGODB_DB' }]
})
EOF
