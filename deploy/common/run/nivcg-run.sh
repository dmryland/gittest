#!/bin/bash
# $Id: //IT/main/code/apps/web/soa/nivcg/dev/1.7/deploy/common/run/nivcg-run.sh#1 $
# NAME: nivcg-run.sh
# CREATED: May 01, 2012
# AUTHOR: NINJIUNN
# CONTACT: soa.technical@ni.com
# DESCRIPTION: nivcg run script
# ===================================================================
#
# Nivcg additional classpaths
LOCATION_SERVICE_ENDPOINT=http://ws-serverlocator-prod:404/servers/
TIER_SERVICE_ENDPOINT=http://ws-serverlocator-prod:404/servers/gettier/
NIVCG_LIBRARY=/opt/apps/standalone/nivcg/nivcg_lib
NIVCG_DATA_DIRECTORY=/webnfs/webfiles/data/nivcg
NIJAVALIB_PROPERTIES=/opt/apps/standalone/global/properties
NIVCG_PROPERTIES=/opt/apps/standalone/nivcg/nivcg_properties
NIVCG_JAR=/opt/apps/standalone/nivcg/bin/nivcg.jar

CLASSPATH=$NIVCG_LIBRARY:$NIJAVALIB_PROPERTIES:$NIVCG_PROPERTIES:$NIVCG_JAR

for jarfile in $NIVCG_LIBRARY/*.jar; do
	CLASSPATH=$CLASSPATH:$jarfile
done

echo Classpath=$CLASSPATH

eval `/opt/apps/autodeploy/bin/keychain --eval ~/.ssh/id_rsa --quiet`

/opt/apps/java/jdk/current/bin/java -Xmx512m -classpath $CLASSPATH com.ni.apps.soa.nivcg.main.Command all

chmod 644 *.vcl

# rm -rf /opt/apps/standalone/nivcg/run/dev
if [ ! -d "/opt/apps/standalone/nivcg/run/dev" ]; then
	mkdir /opt/apps/standalone/nivcg/run/dev
fi
mv *-dev.vcl /opt/apps/standalone/nivcg/run/dev

# rm -rf /opt/apps/standalone/nivcg/run/dev2
if [ ! -d "/opt/apps/standalone/nivcg/run/dev2" ]; then
	mkdir /opt/apps/standalone/nivcg/run/dev2
fi
mv *-dev2.vcl /opt/apps/standalone/nivcg/run/dev2

# rm -rf /opt/apps/standalone/nivcg/run/test
if [ ! -d "/opt/apps/standalone/nivcg/run/test" ]; then
	mkdir /opt/apps/standalone/nivcg/run/test
fi
mv *-test.vcl /opt/apps/standalone/nivcg/run/test

# rm -rf /opt/apps/standalone/nivcg/run/test2
if [ ! -d "/opt/apps/standalone/nivcg/run/test2" ]; then
	mkdir /opt/apps/standalone/nivcg/run/test2
fi
mv *-test2.vcl /opt/apps/standalone/nivcg/run/test2

# rm -rf /opt/apps/standalone/nivcg/run/prod
if [ ! -d "/opt/apps/standalone/nivcg/run/prod" ]; then
	mkdir /opt/apps/standalone/nivcg/run/prod
fi
mv *-prod.vcl /opt/apps/standalone/nivcg/run/prod

if [ -d $NIVCG_DATA_DIRECTORY ]; then
	if [ ! -L $NIVCG_DATA_DIRECTORY ]; then
		cp -p -R /opt/apps/standalone/nivcg/run/dev $NIVCG_DATA_DIRECTORY
		cp -p -R /opt/apps/standalone/nivcg/run/dev2 $NIVCG_DATA_DIRECTORY
		cp -p -R /opt/apps/standalone/nivcg/run/test $NIVCG_DATA_DIRECTORY
		cp -p -R /opt/apps/standalone/nivcg/run/test2 $NIVCG_DATA_DIRECTORY
		cp -p -R /opt/apps/standalone/nivcg/run/prod $NIVCG_DATA_DIRECTORY
#	else
#		rm $NIVCG_DATA_DIRECTORY
	fi
else
	mkdir $NIVCG_DATA_DIRECTORY
	cp -p -R /opt/apps/standalone/nivcg/run/dev $NIVCG_DATA_DIRECTORY
	cp -p -R /opt/apps/standalone/nivcg/run/dev2 $NIVCG_DATA_DIRECTORY
	cp -p -R /opt/apps/standalone/nivcg/run/test $NIVCG_DATA_DIRECTORY
	cp -p -R /opt/apps/standalone/nivcg/run/test2 $NIVCG_DATA_DIRECTORY
	cp -p -R /opt/apps/standalone/nivcg/run/prod $NIVCG_DATA_DIRECTORY
fi

HOSTNAME=`hostname -s`
export TIER=`wget -t 2 -T 1 -q -O - $TIER_SERVICE_ENDPOINT/$HOSTNAME`
for COVERAGE in int ext; do
	NODES=`wget -t 2 -T 1 -q -O - $LOCATION_SERVICE_ENDPOINT/getedge/$TIER/$COVERAGE`

	for NODE in $NODES; do
		ssh varnish@$NODE "/opt/apps/varnish/support/varnish.initrc reload"

		STATUS=$?
		if [ $STATUS == 1 ]; then
			echo "varnish config reload failure; tier: $TIER, node: $NODE"
		elif [ $STATUS -gt 1 ]; then
			echo "unexpected error during varnish reload execution; tier: $TIER, node: $NODE"
		fi
		#varnish-conduit reloads should be only on ext
		if [ $COVERAGE == ext ]; then 
			 ssh varnish@$NODE "/opt/apps/varnish-conduit/support/varnish.initrc reload"
			 STATUS=$?
			 if [ $STATUS == 1 ]; then
				 echo "varnish-conduit config reload failure; tier: $TIER, node: $NODE"
			 elif [ $STATUS -gt 1 ]; then
				 echo "unexpected error during varnish-conduit reload execution; tier: $TIER, node: $NODE"
			 fi
		fi
	done
done

# tar -cvf vcl.tar /opt/apps/standalone/nivcg/run/dev/ /opt/apps/standalone/nivcg/run/dev2/ /opt/apps/standalone/nivcg/run/test/ /opt/apps/standalone/nivcg/run/test2/ /opt/apps/standalone/nivcg/run/prod/
# We no longer need to send emails
# uuencode vcl.tar vcl.tar | mail -s "$TIER : nivcg run notification" jiunn.chang@ni.com

# rm -rf /opt/apps/standalone/nivcg/run/dev
# rm -rf /opt/apps/standalone/nivcg/run/dev2
# rm -rf /opt/apps/standalone/nivcg/run/test
# rm -rf /opt/apps/standalone/nivcg/run/test2
# rm -rf /opt/apps/standalone/nivcg/run/prod
# rm -rf /opt/apps/standalone/nivcg/run/vcl.tar