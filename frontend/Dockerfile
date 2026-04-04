# Stage 1: Build the React application
FROM node:20-alpine AS build
WORKDIR /app

# Copy package files to install dependencies
COPY package.json package-lock.json* ./
# Use npm ci for clean, reliable installs based on package-lock.json
# Using --legacy-peer-deps or similar flags can be added here if needed
RUN npm ci

# Copy the rest of the application source code
COPY . .

# Build the React app for production 
# It handles both Vite (outputs to 'dist') and CRA (outputs to 'build')
RUN npm run build \
    && [ -d "dist" ] && mv dist build || true

# Stage 2: Serve the application using Nginx
FROM nginx:alpine

# Copy the built production files from the previous stage to Nginx's serve directory
COPY --from=build /app/build /usr/share/nginx/html

# Replace the default Nginx port 80 with port 3000 as requested
RUN sed -i 's/80/3000/g' /etc/nginx/conf.d/default.conf

# Expose port 3000
EXPOSE 3000

# Start Nginx in the foreground
CMD ["nginx", "-g", "daemon off;"]
