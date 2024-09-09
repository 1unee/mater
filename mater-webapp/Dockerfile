FROM node:20-alpine

WORKDIR /app

COPY . /app

RUN npm install -g @angular/cli
RUN npm install

CMD ["ng", "serve", "--host", "0.0.0.0", "--port", "4200", "--ssl", "--ssl-key", "./oneune.duckdns.org.key", "--ssl-cert", "./oneune.duckdns.org.crt", "--configuration", "prod"]
