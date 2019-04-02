#!/bin/bash

echo "generate release messages"
protoc --cpp_out=../../src msgdef/login.proto
protoc --cpp_out=../../src msgdef/event.proto
protoc --cpp_out=../../src msgdef/log.proto
protoc --cpp_out=../../src msgdef/sensor.proto
protoc --cpp_out=../../src msgdef/command.proto
protoc --cpp_out=../../src msgdef/gwinfo.proto
protoc --cpp_out=../../src message.proto
