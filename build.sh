#!/bin/sh

# List of projects to build in the CORRECT ORDER.
projects=(\
    "AccountMessaging"\
    "bpms-service"\
)

# Create bin directory if it doesn't exist
if [ ! -d "bin" ]; then
	mkdir bin
fi 
cd bin

# Clear bin from previously built artifacts
rm -f *.jar > /dev/null
rm -f *.war > /dev/null

# Remove build.log from previous builds
rm -f ../build.log > /dev/null

for project in "${projects[@]}"
do
    cd ../$project
    echo Building $project...
    if mvn clean install | tee -a ../build.log | grep "ERROR\|BUILD SUCCESS\|BUILD FAIL"; then
        cp target/*.*ar ../bin/
    else
        echo See build.log for details
    fi
done

cd ..

echo Done. Built assets copied to $PWD/bin