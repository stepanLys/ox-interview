FROM node:20-alpine AS build

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM node:20-alpine

RUN npm install -g serve --silent

COPY --from=build /app/dist /app/dist

WORKDIR /app/dist

EXPOSE 5000 

CMD ["serve", "-s", "-l", "5000"]