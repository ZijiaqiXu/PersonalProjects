#!/bin/bash 
sleep infinity &
trap "exit 0" INT TERM

echo Starting ActiveMQ....
${ACTIVEMQ_HOME}/bin/activemq start

wait

echo Stopping ActiveMQ....
${ACTIVEMQ_HOME}/bin/activemq stop
