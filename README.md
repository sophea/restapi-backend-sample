# Getting Started

This is a basic springboot project with that latest version

- springboot 2.6.x or 2.7.x
- java11 or later
- maven build

It can run JBOSS EAP / Tomcat / Jetty

```
deploy with tomcat :

mvn package && java -jar target/sample-api-1.0.1-SNAPSHOT.jar



```

#### Test API

REST-DOC-API :
https://127.0.0.1/swagger-ui/index.html

### Health check and version api

```
curl https://127.0.0.1/actuator/info

{"build":{"artifact":"finet-api","name":"finet-api","time":"2022-04-04T10:25:50.477Z","version":"0.0.6","group":"com.jtrust"}}


curl http://localhost:8080/actuator/health

{ status: "UP" }

```

### Features

Sample Java springboot latest version 2.6.x with JAVA JDK 11 or later

	- Support tomcat / jboss EAP web server
	- Can run standalone server
	- Can run as docker container
	- Can run as AWS fargate severless 
	- Can run on Win/Linux/Mac OS

==========Features

	- Generate swagger UI REST-APIs  : version 3.x.x
	- Swagger UI  with Basic Authentication  validation
	- Support actuator info/health
	- Basic REST API  controller

Apply APIs with Basic Authentication validation

#### Springboot with springfox doc rest

```
 /**
     * 2 beans below to fix with springfox version 3.x with springboot 2.6.x with application.properties
     * spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER
     *
     * Ref : https://github.com/springfox/springfox/issues/3462#issuecomment-1010721223
     * */

    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes,
                                                                         CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping);
    }



    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }

```

### check style springboot-java format

https://github.com/spring-io/spring-javaformat

### commit message git hooks

https://dwmkerr.com/conventional-commits-and-semantic-versioning-for-java/

- mkdir .githooks
- git config core.hooksPath .githooks
- nano .githooks/commit-msg

````bash
#!/usr/bin/env bash

# Create a regex for a conventional commit.
convetional_commit_regex="^(build|chore|ci|docs|feat|fix|perf|refactor|revert|style|test)(\([a-z \-]+\))?!?: .+$"

# Get the commit message (the parameter we're given is just the path to the
# temporary file which holds the message).
commit_message=$(cat "$1")

# Check the message, if we match, all good baby.
if [[ "$commit_message" =~ $convetional_commit_regex ]]; then
   echo -e "\e[32mCommit message meets Conventional Commit standards...\e[0m"
   exit 0
fi

# Uh-oh, this is not a conventional commit, show an example and link to the spec.
echo -e "\e[31mThe commit message does not meet the Conventional Commit standard\e[0m"
echo "An example of a valid message is: "
echo "  feat(login): add the 'remember me' button"
echo "More details at: https://www.conventionalcommits.org/en/v1.0.0/#summary"
exit 1
````

# Pre-push message

run this command to your local :

````
git config core.hooksPath .githooks
````

Every push commit: It will run build Test with successful result first, in case the build failed with test case it will
reject your commit.

## How to skip this validation

You can disable the validation using --no-verify flag, but should only do this if you have already run the tests on
previous commits/pushes

git push origin develop --no-verify

### Google java format

- https://google.github.io/styleguide/javaguide.html
- https://github.com/Cosium/git-code-format-maven-plugin
- https://github.com/google/google-java-format

### plugin build

- mvn clean package  : it will be automatic format your source codes (format-code)
- mvn clean install : it will validate git-code-format

```
Manual code formatting
mvn git-code-format:format-code -Dgcf.globPattern=**/*

Manual code format validation
mvn git-code-format:validate-code-format -Dgcf.globPattern=**/*

```

### Maven plugin

````xml

<plugin>
  <groupId>com.cosium.code</groupId>
  <artifactId>git-code-format-maven-plugin</artifactId>
  <version>3.4</version>
  <executions>
    <!-- On commit, format the modified java files -->
    <execution>
      <id>install-formatter-hook</id>
      <goals>
        <goal>install-hooks</goal>
      </goals>
    </execution>
    <execution>
      <id>format-code</id>
      <phase>package</phase>
      <goals>
        <goal>format-code</goal>
      </goals>
    </execution>
    <!-- On Maven verify phase, fail if any file
    (including unmodified) is badly formatted -->
    <execution>
      <id>validate-code-format</id>
      <goals>
        <goal>validate-code-format</goal>
      </goals>
    </execution>
  </executions>
  <configuration>

    <googleJavaFormatOptions>
      <aosp>true</aosp>
      <fixImportsOnly>false</fixImportsOnly>
      <skipSortingImports>false</skipSortingImports>
      <skipRemovingUnusedImports>false</skipRemovingUnusedImports>
    </googleJavaFormatOptions>
    <preCommitHookPipeline>| grep -F '[ERROR]' || exit 0 &amp;&amp; exit 1</preCommitHookPipeline>
  </configuration>
</plugin>
````
