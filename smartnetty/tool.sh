#!/bin/sh

build() {
	#编译Protobuf
	cd protobuf
	protoc --java_out java --cpp_out cpp --proto_path . message.proto msgdef/*

	cd ..
	cp -rf protobuf/java/* common/src/main/java/
	cp -rf protobuf/cpp/* demo/cpp-client/

	cd common
	mvn install
	cd ..

	#编译core
	cd core
	mvn install
	cp target/backend-core-0.0.1-SNAPSHOT.jar bin/backend-core.jar
	cd ..
	return
}

clean() {
	rm -rf protobuf/java/*
	rm -rf protobuf/cpp/*
	cd common
	mvn clean
	cd ..

	cd core
	mvn clean
	cd ..
	return
}

if [ $1 == "build" ] 
	then
	build
elif [ $1 == "clean" ] 
	then
	clean
else
	echo "can't find command!"
fi