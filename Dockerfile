FROM jenkins/jenkins:lts
USER 0
RUN apt update \
  && apt-get install -y --no-install-recommends autoconf \
  && rm -r /var/lib/apt/lists/*