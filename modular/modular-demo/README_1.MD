Demo
cd /Users/amoncsek/Documents/development/vertx/modular/modular-demo && docker build -t modular-demo .
cd /Users/amoncsek/Documents/development/vertx/modular/modular-demo && docker run -p 8080:8080 --name modular-demo --rm modular-demo

// docker run -m32M -p 8080:8080 --name modular-demo --rm modular-demo

hare:on -XX:SharedArchiveFile=app-cds.jsa --module-path target/modular-demo-1.2-SNAPSHOT.jar:target/modules --module vxms.core.demo/org.jacpfx.vxms.verticle.Runner 