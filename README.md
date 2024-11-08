# CONSIDERACIONES
    1 - LENGUAJE JAVA 17
    2 - MAVEN
    3 - CREAR S3 PARA CONTENER EL ZIP DE LA CONSTRUCCION DEL PROYECTO
    4 - Reemplzar variables de las conexiones a la base de datos y al servicio SNS

# CONSTRUCCION BACK JAVA LAMBDA EN AWS

    1. DESCARGAR EL PROYECTO
        
    
    2. Ejecutar mvn clean
       Descripción: mvn clean elimina todos los archivos generados durante compilaciones anteriores, principalmente en la 
       carpeta target, que es donde Maven guarda los resultados de las compilaciones, incluyendo archivos .class, archivos 
       JAR o WAR y ZIP.
    
       
    3. mvn install
       Descripción: mvn install compila el proyecto y todos sus módulos, ejecuta pruebas, y, si todo es exitoso, empaqueta 
       el código (en un JAR, WAR o ZIP) y lo instala en el repositorio local de Maven (usualmente en el directorio .target en tu máquina).
    
    4. agregar el .zip AL S3

    5. EJECUTAR EL ARCHIVO YAML EN CLOUDFORMATION QUE SE DEJA A CONTINUACION  

- [CLOUDFORMATION](/java-back-lambda-stack-v2.yaml)

    6. obtener la url del apigateway y reeemplazar en el servicio del front


# btg-lambda serverless API


The btg-lambda project, created with [`aws-serverless-java-container`](https://github.com/aws/serverless-java-container).

The starter project defines a simple `/ping` resource that can accept `GET` requests with its tests.

The project folder also includes a `template.yml` file. You can use this [SAM](https://github.com/awslabs/serverless-application-model) file to deploy the project to AWS Lambda and Amazon API Gateway or test in local with the [SAM CLI](https://github.com/awslabs/aws-sam-cli). 

## Pre-requisites
* [AWS CLI](https://aws.amazon.com/cli/)
* [SAM CLI](https://github.com/awslabs/aws-sam-cli)
* [Gradle](https://gradle.org/) or [Maven](https://maven.apache.org/)

## Building the project
You can use the SAM CLI to quickly build the project
```bash
$ mvn archetype:generate -DartifactId=btg-lambda -DarchetypeGroupId=com.amazonaws.serverless.archetypes -DarchetypeArtifactId=aws-serverless-jersey-archetype -DarchetypeVersion=2.0.3 -DgroupId=org.btg -Dversion=1.0-SNAPSHOT -Dinteractive=false
$ cd btg-lambda
$ sam build
Building resource 'BtgLambdaFunction'
Running JavaGradleWorkflow:GradleBuild
Running JavaGradleWorkflow:CopyArtifacts

Build Succeeded

Built Artifacts  : .aws-sam/build
Built Template   : .aws-sam/build/template.yaml

Commands you can use next
=========================
[*] Invoke Function: sam local invoke
[*] Deploy: sam deploy --guided
```

## Testing locally with the SAM CLI

From the project root folder - where the `template.yml` file is located - start the API with the SAM CLI.

```bash
$ sam local start-api

...
Mounting com.amazonaws.serverless.archetypes.StreamLambdaHandler::handleRequest (java11) at http://127.0.0.1:3000/{proxy+} [OPTIONS GET HEAD POST PUT DELETE PATCH]
...
```

Using a new shell, you can send a test ping request to your API:

```bash
$ curl -s http://127.0.0.1:3000/ping | python -m json.tool

{
    "pong": "Hello, World!"
}
``` 

## Deploying to AWS
To deploy the application in your AWS account, you can use the SAM CLI's guided deployment process and follow the instructions on the screen

```
$ sam deploy --guided
```

Once the deployment is completed, the SAM CLI will print out the stack's outputs, including the new application URL. You can use `curl` or a web browser to make a call to the URL

```
...
-------------------------------------------------------------------------------------------------------------
OutputKey-Description                        OutputValue
-------------------------------------------------------------------------------------------------------------
BtgLambdaApi - URL for application            https://xxxxxxxxxx.execute-api.us-west-2.amazonaws.com/Prod/pets
-------------------------------------------------------------------------------------------------------------
```

Copy the `OutputValue` into a browser or use curl to test your first request:

```bash
$ curl -s https://xxxxxxx.execute-api.us-west-2.amazonaws.com/Prod/ping | python -m json.tool

{
    "pong": "Hello, World!"
}
```
