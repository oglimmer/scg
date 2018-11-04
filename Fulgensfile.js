module.exports = {

  config: {
    Name: "SimpleCardGame",
    Vagrant: {
      Box: 'ubuntu/xenial64',
      Install: 'maven openjdk-8-jdk-headless docker.io'
    },
  },

  software: {
    "scg": {
      Source: "mvn",      
      Artifact: "target/scg##001.war"
    },

    "tomcat": {
      Source: "tomcat",
      Deploy: "scg"
    }
  }
}
