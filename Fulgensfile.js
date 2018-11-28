module.exports = {

  config: {
    SchemaVersion: "1.0.0",
    Name: "SimpleCardGame",
    Vagrant: {
      Box: 'ubuntu/xenial64',
      Install: 'maven openjdk-8-jdk-headless docker.io'
    },
  },

  versions: {
    scg: {
      TestedWith: "3-jre-11"
    },
    tomcat: {
      Docker: "tomcat9-openjdk11-openj9",
      TestedWith: "7 & 9"
    }
  },

  software: {
    scg: {
      Source: "mvn",      
      Artifact: "target/scg##001.war"
    },

    tomcat: {
      Source: "tomcat",
      DockerImage: "oglimmer/adoptopenjdk-tomcat",
      DockerMemory: "70M",
      Deploy: "scg"
    }
  }
}
