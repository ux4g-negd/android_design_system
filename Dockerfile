FROM node:20-alpine AS build

WORKDIR /app

# 1. Install dependencies
COPY package*.json ./
RUN npm ci

# 2. Copy full source code
COPY . .

# 3. Generate SEO and sitemap files
RUN node scripts/generate-seo-files.js

# 4. Build documentation (static export to /app/out)
RUN npm run build

FROM nginx:alpine AS runtime

RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/out /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]