setlocal
javac -d ../bin ../src/*.java

cd ../bin

jar -cfe ../build/file_processing.jar Main *.class

pause
