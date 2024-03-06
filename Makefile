CLASSPATH = "/cs/home/ejs35/Documents/CS4303/processing-4.3-linux-x64/processing-4.3/core/library/core.jar"

clean:
	rm -rf build W05.jar

mkBuild: clean
	mkdir build

compile: mkBuild
	cd AStarEx; javac --class-path $(CLASSPATH) `find . -name "*.java" -print0 | xargs -0` -d ../build

unzipCore: mkBuild
	unzip -o $(CLASSPATH) -d build

jar: unzipCore compile
	cd build; jar cfe ../W05.jar p.AStarEx .

run: jar
	java -jar W05.jar