POM_VERSION = $(shell mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
version:
	echo $(POM_VERSION)
clean:
	mvn -Dmaven.artifact.threads=10 clean

compile:
	mvn compile

local-verify: clean
	mvn verify
