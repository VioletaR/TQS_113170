FROM oven/bun:1

WORKDIR /frontend

COPY package.json ./
COPY bun.lock ./

RUN bun install

EXPOSE 3000
CMD ["bun", "--bun", "run", "dev"]