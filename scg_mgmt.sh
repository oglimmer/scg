#!/bin/sh

usage="$(basename "$0") [-b|-a] [-w] [-c] [-e] - builds or deploys scg

where:
    -h  shows this help text
    -b  build: performans a git pull and mvn package
    -a  auto build: builds/deploys only if there is a new commit in git
    -w  war: deploys the war file with a new version"

cd ${0%/*}

if [ -z "$SCG_WEBAPPS" ]; then
  SCG_WEBAPPS=/var/lib/tomcat/webapps
fi

while getopts ':hbweac' option; do
  case "$option" in
    h) echo "$usage"
       exit
       ;;
    b) BUILD=YES
       ;;
    w) WAR=YES
       ;;
    a) AUTO_BUILD=YES
       ;;
    :) printf "missing argument for -%s\n" "$OPTARG" >&2
       echo "$usage" >&2
       exit 1
       ;;
   \?) printf "illegal option: -%s\n" "$OPTARG" >&2
       echo "$usage" >&2
       exit 1
       ;;
  esac
done
shift $((OPTIND - 1))

if [ -n "$AUTO_BUILD" ]; then
  BUILD=
  git=$(git pull)
  if [ "$git" != "Already up-to-date." ]; then
    mvn clean package || exit 1    
  else
    WAR=
  fi
fi

if [ -n "$BUILD" ]; then
  git pull
  mvn clean package || exit 1
fi

if [ -n "$WAR" ]; then
  for file in `ls -1 $SCG_WEBAPPS| grep scg | sort -r| awk '{gsub(/\/.*\/|scg##|.war/,"",$1); printf("%03d\n",++$1);}'`
  do
    VERSION=$file
    break
  done
  if [ -z "$VERSION" ]; then
    VERSION=001
  fi
  if [ -d "$SCG_WEBAPPS" ]; then
  	echo "Deploying war with version = $VERSION to $SCG_WEBAPPS"
  	cp target/scg##001.war $SCG_WEBAPPS/scg##$VERSION.war
  fi
fi



