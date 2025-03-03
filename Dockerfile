FROM public.ecr.aws/amazoncorretto/amazoncorretto:17
COPY target/reactiveExample.jar reactiveExample.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","reactiveExample.jar"]