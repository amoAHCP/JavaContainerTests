Demo
cd /Users/amoncsek/Documents/development/vertx/openJ9/j9-demo && docker build -tj9-demo .
cd /Users/amoncsek/Documents/development/vertx/openJ9/j9-demo &&  docker run -p 7070:8080 -v /Users/amoncsek/Documents/development/vertx/openJ9/j9-demo:/app/shared --name j9-demo --rm j9-demo