## run Docker demo
docker build -t modular-demo .
docker run -p 8080:8080 --name modular-demo --rm modular-demo

## run without CDS
mvn clean package
java --module-path target/modular-demo-1.2-SNAPSHOT.jar:target/modules --module vxms.core.demo/org.jacpfx.vxms.verticle.Runner  

## create AppCDS
java -XX:DumpLoadedClassList=classes.lst --module-path target/modular-demo-1.2-SNAPSHOT.jar:target/modules --module vxms.core.demo/org.jacpfx.vxms.verticle.Runner
java -Xshare:dump -XX:SharedClassListFile=classes.lst -XX:SharedArchiveFile=app-cds.jsa --module-path target/modular-demo-1.2-SNAPSHOT.jar:target/modules --module vxms.core.demo/org.jacpfx.vxms.verticle.Runner
 
 
 
## run with CDS

java -Xshare:on -XX:SharedArchiveFile=app-cds.jsa --module-path target/modular-demo-1.2-SNAPSHOT.jar:target/modules --module vxms.core.demo/org.jacpfx.vxms.verticle.Runner 