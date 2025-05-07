# Postaddress - Address Book Demo

Address Book API with CRUD Operations for postaddrees.

## Getting Started


### Prerequisites

Requirements for the software and other tools to build, test and push

- Zulu Java JDK 21
- docker-compose
- Optional: JetBrains HTTP client

### Installing

1. Create .env and .env-local files, e.g.

  ```bash
  # .env
  POSTGRES_USER=postgres
  POSTGRES_PASSWORD=postgres
  POSTGRES_DB=postaddress
  POSTGRES_URL=jdbc:postgresql://localhost:5432/${POSTGRES_DB}
  
  # .env-local with internal docker container dns
  POSTGRES_USER=postgres
  POSTGRES_PASSWORD=postgres
  POSTGRES_DB=postaddress
  POSTGRES_URL=jdbc:postgresql://postgres:5432/${POSTGRES_DB}
  ```

1. Export env variables

   ```bash
   set -o allexport; source .env; set +o allexport
   ```

1. Run `docker-compose -f compose-dev.yaml` to provide postgres database for jooq code generation
1. Run `/mvnw clean package jib:buildTar` to create a local docker image without registry
1. Import Image to docker/podman `podman load --input target/jib-image.tar`
1. Switch to local compose file.

  ```sh
  docker-compose -f compose-dev.yaml down
  docker-compose -f compose-local.yaml up
  ```

## Running the tests

Run the http files with JetBrains IntelliJ.
