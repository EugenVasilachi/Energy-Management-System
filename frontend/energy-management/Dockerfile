# Stage 1: Build the React app
FROM node:18-alpine AS build

WORKDIR /app

COPY package.json .

# Install application dependencies.
RUN npm install

COPY . .

# Build the application.
RUN npm run build

# Stage 2: Create an Nginx image
FROM nginx:alpine

# Copy the built React app to the Nginx HTML directory.
COPY --from=build /app/build /usr/share/nginx/html

# Copy your nginx.conf to the nginx configuration directory.
COPY nginx.conf /etc/nginx/nginx.conf

# Expose the port.
EXPOSE 80