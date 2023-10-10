FROM node:18

WORKDIR /app

RUN apt-get update && apt-get -y install curl unzip

RUN curl -LO https://github.com/redpanda-data/redpanda/releases/latest/download/rpk-linux-amd64.zip
RUN unzip rpk-linux-amd64.zip -d .
RUN rm rpk-linux-amd64.zip

COPY ./app/package.json package.json
COPY ./app/package-lock.json package-lock.json
RUN npm install

COPY ./app ./
EXPOSE 8080

CMD [ "node", "server.mjs" ]

