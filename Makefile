#The classpath is the libjars folder
CLASSPATH = libjars/*.jar
SRC_DIR = "src"
BUILD_DIR = "build"
#Where to unzip the jars in the classpath to
LIB_DIR = "lib"
MAIN_CLASS = "cs4303.p2.Main"
JAR = "CS4303P2.jar"

clean:
	rm -rf $(BUILD_DIR) $(JAR)

mkBuild: clean
	mkdir $(BUILD_DIR)

compile: mkBuild
	cd $(SRC_DIR); find . -name "*.java" -print0 | xargs -0 javac --class-path ../$(CLASSPATH) -d ../$(BUILD_DIR)

mkLib:
	mkdir -p $(LIB_DIR)

#Unzip the classpath into the lib folder, but only if its empty
unzipCore: mkLib
	(find $(LIB_DIR) -mindepth 1 -maxdepth 1 | read) || unzip -o $(CLASSPATH) -d $(LIB_DIR)

#Copy the lib folder into the build folder, to include in the jar
cpLib: unzipCore
	cp -r $(LIB_DIR)/* $(BUILD_DIR)

jar: mkBuild cpLib compile
	cd $(BUILD_DIR); jar cfe ../$(JAR) $(MAIN_CLASS) .

run: jar
	java -jar $(JAR)