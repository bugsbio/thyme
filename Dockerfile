# VERSION 0.1.0

FROM clojure

RUN mkdir -p /code
WORKDIR /code

COPY project.clj /code/
RUN lein deps

COPY . /code/

RUN lein uberjar && mv target/uberjar/thyme.jar .
CMD ["java", "-jar", "thyme.jar"]
