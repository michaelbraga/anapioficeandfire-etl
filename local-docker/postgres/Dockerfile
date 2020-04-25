FROM debian:jessie-slim

ENV REP_LINE "deb http://apt.postgresql.org/pub/repos/apt/ trusty-pgdg main 9.4"

RUN apt-get update -qq && \
    apt-get upgrade -y && \
    apt-get install wget -qq && \
    echo $REP_LINE > /etc/apt/sources.list.d/pgdg.list && \
    wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add - && \
    apt-get update && \
    apt-get install postgresql-11 -y && \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN echo "/etc/init.d/postgresql start && exit 0" > /etc/rc.local && \
    /etc/init.d/postgresql start && \
    su postgres -c "psql --command \"CREATE USER etl with password 'etlpassword';\" " && \
    su postgres -c "psql --command \"CREATE DATABASE asoiaf with owner etl;\" " && \
    echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/11/main/pg_hba.conf && \
    echo "listen_addresses='*'" >> /etc/postgresql/11/main/postgresql.conf

USER postgres

EXPOSE 5432

CMD ["/usr/lib/postgresql/11/bin/postgres", "-D", "/var/lib/postgresql/11/main", "-c", "config_file=/etc/postgresql/11/main/postgresql.conf"]

