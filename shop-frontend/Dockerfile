# ---------- Build Angular ----------
FROM node:20-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

# ---------- Serve with Nginx ----------
FROM nginx:alpine

# Elimina config por defecto
RUN rm /etc/nginx/conf.d/default.conf

# Copia nuestra config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copia el build de Angular
COPY --from=build /app/dist/shop-frontend /usr/share/nginx/html

EXPOSE 80
