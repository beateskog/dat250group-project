FROM node:16-alpine as build

WORKDIR /feedapp-frontend

COPY package*.json ./


RUN npm install


COPY . .

RUN npm run build -- --output-path=./dist/out

FROM nginx:alpine


RUN rm -rf /usr/share/nginx/html/*


COPY --from=build /feedapp-frontend/dist/out /usr/share/nginx/html

COPY ./nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
