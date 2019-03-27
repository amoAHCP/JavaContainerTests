# Demo
mvn clean package && docker build -t j9-demo .
docker run -p 7070:8080 -v /Users/andy.moncsek/Documents/development/JavaContainerTests/openJ9/j9-demo:/app/shared --name j9-demo --rm j9-demo