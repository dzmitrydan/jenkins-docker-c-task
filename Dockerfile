RUN apt update \
  && apt-get install -y --no-install-recommends autoconf make build-essential libtool cmake \
  && rm -r /var/lib/apt/lists/*