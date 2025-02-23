<h1 align="center" style="font-family: 'GFS Didot', serif; font-weight: bold; font-size: 60px;">Sedeo</h1>


<p align="center">
  <a href="https://github.com/sedeo-organization/sedeo-backend/actions/workflows/maven.yml">
    <img src="https://github.com/sedeo-organization/sedeo-backend/actions/workflows/maven.yml/badge.svg" alt="Build Status">
  </a>
</p>

<p align="center">
    <b>Server for Sedeo financial settlements mobile application.</b>
</p>

<h2 id="introduction">Introduction</h2>

<b>"Sedeo"</b> allows users to divide group expenses and settle them in a simple and practical way.

The application enables users to maintain a list of friends, create themed settlement groups,
and manage individual settlements while considering various division scenarios. Additionally, each user possesses a profile where their settlement account balance is calculated based on settlements across all groups.
<h2 id="started">Getting started</h2>

Running the project locally requires additional steps.

<h3>Environmental variables</h2>

To run the project you need to provide a series of environmental variables. 

Environmental variables needed for the project to run locally:
```yaml
JWT_SECRET_KEY={JWT_SECRET_KEY};
```

Environmental variables required for the project to run in production environment:
```yaml
AZURE_CONNECTION_STRING={AZURE_CONNECTION_STRING};
FRONTEND_BASE_URL={FRONTEND_BASE_URL};
MAIL_SENDER_ADDRESS={MAIL_SENDER_ADDRESS};
SPRING_DATASOURCE_URL={SPRING_DATASOURCE_URL};
SPRING_DATASOURCE_USERNAME={SPRING_DATASOURCE_USERNAME};
SPRING_DATASOURCE_PASSWORD={SPRING_DATASOURCE_PASSWORD};
```

<h3>Profiles</h2>

<h4>Spring profiles</h4>

Currently, three Spring profiles are available:
<ul>
  <li><b>prod</b> - for use in production environment</li>
  <li><b>dev</b> - for use in development environment</li>
  <li><b>test</b> - for use in testing environment</li>
</ul>

<h4>Maven profiles</h4>
Currently, two Maven build profiles are available:
<ul>
  <li><b>prod</b> - for use in production environment</li>
  <li><b>dev</b> - for use in development environment</li>
</ul>

<h3>Running the server</h3>

To run the server locally you can use a Maven Wrapper:

<h4>Linux</h4>
```bash
chmod +x ./mvnw
./mvnw clean install
./mvnw -f main-web/pom.xml spring-boot:run -Pdev -Dspring-boot.run.profiles=dev
```

[build-badge]: https://github.com/sedeo-organization/sedeo-backend/actions/workflows/maven.yml/badge.svg
[build-url]: https://github.com/sedeo-organization/sedeo-backend/actions/workflows/maven.yml