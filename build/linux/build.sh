javac -d ../../bin ../../src/*.java
cd ../../bin
jar -cfe ../build/linux/file_processing.jar Main *.class
